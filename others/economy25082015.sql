--CREATE DATABASE economy;
--DROP DATABASE economy;
CREATE TABLE usuario (
	idusuario serial PRIMARY KEY,
	nome varchar(150) NOT NULL,
	login varchar(150) NOT NULL,
	senha varchar(50) NOT NULL
);

CREATE TABLE conta(
	idconta serial PRIMARY KEY,
	idusuario integer NOT NULL,	
	descricao varchar(150) NOT NULL,
	CONSTRAINT fk_idusuario FOREIGN KEY (idusuario)
		REFERENCES usuario (idusuario)
);

CREATE TABLE saldo(
	idsaldo serial PRIMARY KEY,
	idconta integer NOT NULL,	
	mes integer NOT NULL,
	ano integer NOT NULL,
	saldo numeric(18,2) NOT NULL,
	CONSTRAINT fk_idconta FOREIGN KEY (idconta)
		REFERENCES conta (idconta)
);

CREATE TABLE categoria(
	idcategoria serial PRIMARY KEY,
	descricao varchar(100) NOT NULL
);

CREATE TABLE lancamento(  
	idlancamento serial PRIMARY KEY, 
	idconta integer NOT NULL,
	idcategoria integer NOT NULL,
	idusuario integer NOT NULL,
	
	descricao varchar(150) NOT NULL, 
	valor numeric NOT NULL, 

	definacao varchar(10) NOT NULL,
	tipo varchar(10) NOT NULL,
	diautil integer,
	repetir integer,
	
	CONSTRAINT fk_idconta FOREIGN KEY (idconta)
		REFERENCES conta (idconta),
	CONSTRAINT fk_idcategoria FOREIGN KEY (idcategoria)
		REFERENCES categoria (idcategoria),
	CONSTRAINT fk_idusuario FOREIGN KEY (idusuario)
		REFERENCES usuario (idusuario)
);


CREATE TABLE lancamento_mov(
	idlancamentomov serial PRIMARY KEY,
	idlancamento integer NOT NULL,
	idconta integer NOT NULL,
	
	datamov date NOT NULL,
	valor numeric NOT NULL,
	pago boolean NOT NULL,

	CONSTRAINT fk_idconta FOREIGN KEY (idconta)
		REFERENCES conta (idconta),
	CONSTRAINT fk_idlancamento FOREIGN KEY (idlancamento)
		REFERENCES lancamento (idlancamento)
);



--------------------------------------------
------------------FUNÇÕES-------------------

-- Function: sp_lanca_despesas_fixas()

-- DROP FUNCTION sp_lanca_despesas_fixas();

CREATE OR REPLACE FUNCTION sp_lancamentos_fixos()
  RETURNS character varying AS
$BODY$
DECLARE 
  rLancamento record;
  sLancamento varchar;
  nexiste  integer;
  dtutil   date;
BEGIN
  sLancamento = 'SELECT * FROM  lancamento
               WHERE definicao = ''FIXA'' ';

  FOR rLancamento IN EXECUTE sLancamento LOOP

    SELECT (rLancamento.diautil||'/'||
           (SELECT EXTRACT( 'Month' from now()) AS X)||'/'||
           (SELECT EXTRACT( 'Year' from now()) AS X))::date INTO dtutil;

    SELECT count(*) INTO nexiste FROM lancamento_mov WHERE 
        EXTRACT( 'Month' from datamov) = EXTRACT( 'Month' from now()) AND
        EXTRACT( 'Year' from datamov) = EXTRACT( 'Year' from now()) AND 
        idlancamento  = rLancamento.idlancamento;

    IF nexiste = 1 THEN
      UPDATE lancamento_mov 
         SET data = dtutil, 
             idconta = rLancamento.idconta,
             valor = rLancamento.valor
      WHERE EXTRACT( 'Month' from datamov) = EXTRACT( 'Month' from now()) AND
            EXTRACT( 'Year' from datamov) = EXTRACT( 'Year' from now()) AND 
            iddespesa  = rDespesa.iddespesa;
    ELSIF nexiste > 1 THEN
         RAISE EXCEPTION 'Alteração não permitida, alterar manualmente as movimentações';       
    ELSE
      INSERT INTO lancamento_mov(
              idlancamento, datamov, idconta, valor, pago)
      VALUES ( rLancamento.idlancamento, dtutil, rLancamento.idconta, rLancamento.valor, false);
    END IF;
  END LOOP;
  
  RETURN  '';
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION sp_lancamentos_fixos()
  OWNER TO postgres;
  


  
-- Function: sp_lanca_saldo_inicial(integer)

-- DROP FUNCTION sp_lanca_saldo_inicial(integer);

CREATE OR REPLACE FUNCTION sp_lanca_saldo_inicial(integer)
  RETURNS character varying AS
$BODY$
DECLARE 
  nconta ALIAS FOR $1;
  dexist date;
  nsaldo numeric;
BEGIN
  SELECT max(('01'||'/'||mes||'/'||ano)::date)  INTO dexist
  FROM saldo WHERE idconta  = nconta ;

  SELECT saldo INTO nsaldo FROM saldo 
  WHERE mes =  (SELECT EXTRACT( 'Month' from dexist)) AND 
        ano =  (SELECT EXTRACT( 'Year' from dexist)) AND
        idconta = nconta;
  
  IF dexist IS NULL THEN
    INSERT INTO saldo(idconta, mes, ano, saldo)
    VALUES (nconta, (SELECT EXTRACT( 'Month' from now())), 
                    (SELECT EXTRACT( 'Year' from now())), 0);
  ELSE
    WHILE dexist < date_trunc('month', current_date) LOOP
      dexist = dexist + INTERVAL'1 month';
      INSERT INTO saldo(idconta, mes, ano, saldo)
      VALUES (nconta, (SELECT EXTRACT( 'Month' from dexist)), 
                      (SELECT EXTRACT( 'Year' from dexist)), nsaldo);
    END LOOP;
  END IF;
  RETURN  '';
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION sp_lanca_saldo_inicial(integer)
  OWNER TO postgres;

  

----------------------------------------------------]
-----------------TRIGGER FUNCTION-------------------

CREATE OR REPLACE FUNCTION ftgr_lancamentomov()
  RETURNS trigger AS
$BODY$
DECLARE
  bmovimenta boolean;
  dmes integer;
  dano integer;
  cconta integer;
  nvalor numeric;
BEGIN
  IF TG_OP = 'INSERT' THEN
    bmovimenta  = NEW.pago;
    dmes = EXTRACT( 'Month' from new.datamov);
    dano = EXTRACT( 'Year' from new.datamov);
    cconta = new.idconta;
    nvalor = new.valor;
  ELSIF TG_OP = 'UPDATE' THEN
    bmovimenta  = NEW.pago;
    dmes = EXTRACT( 'Month' from new.datamov);
    dano = EXTRACT( 'Year' from new.datamov);
    cconta = old.idconta;

    IF old.pago = 'true' AND 
       new.pago = 'false' THEN
       nvalor = old.valor*(-1);
       bmovimenta = old.pago;
    ELSE
      nvalor = new.valor - old.valor;
    END IF;

  ELSIF TG_OP = 'DELETE' THEN
    bmovimenta  = OLD.pago;
    dmes = EXTRACT( 'Month' from old.datamov);
    dano = EXTRACT( 'Year' from old.datamov);
    cconta = old.idconta;
    nvalor = old.valor * (-1);
  END IF;

  IF bmovimenta = 'true' THEN
      UPDATE saldo SET saldo = saldo - nvalor 
      WHERE mes = dmes AND ano = dano AND idconta = cconta;
  END IF;

  IF TG_OP = 'INSERT' OR TG_OP = 'UPDATE' THEN
    RETURN NEW;
  ELSE
    RETURN OLD;
  END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION ftgr_lancamentomov()
  OWNER TO postgres;


-- Function: ftgr_lanca_saldo()

-- DROP FUNCTION ftgr_lanca_saldo();

CREATE OR REPLACE FUNCTION ftgr_lanca_saldo()
  RETURNS trigger AS
$BODY$
DECLARE
BEGIN
  PERFORM sp_lanca_saldo_inicial(NEW.idconta);
  RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION ftgr_lanca_saldo()
  OWNER TO postgres;




  ----------------------------------------------------------
  --------------------------TRIGGERS------------------------
-- Trigger: tgr_lanca_receita on movimentacao_receita

-- DROP TRIGGER tgr_lanca_receita ON movimentacao_receita;

CREATE TRIGGER tgr_lancamentomov
  BEFORE INSERT OR UPDATE OR DELETE
  ON lancamento_mov
  FOR EACH ROW
  EXECUTE PROCEDURE ftgr_lancamentomov();
  

-- Trigger: tgr_lanca_saldo on conta

-- DROP TRIGGER tgr_lanca_saldo ON conta;

CREATE TRIGGER tgr_lanca_saldo
  AFTER INSERT
  ON conta
  FOR EACH ROW
  EXECUTE PROCEDURE ftgr_lanca_saldo();
  
 