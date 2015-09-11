--CREATE DATABASE economy;
--DROP DATABASE economy;

CREATE TABLE familia (
	idfamilia serial PRIMARY KEY,
	nome varchar(150) NOT NULL,
	email varchar(150) NOT NULL,
	login varchar(100) NOT NULL,
	senha varchar(30) NOT NULL
);

CREATE TABLE nivel_acesso(
	idnivel_acesso serial PRIMARY KEY,
	descricao varchar(50)
);

CREATE TABLE usuario (
	idusuario serial PRIMARY KEY,
	nome varchar(255) NOT NULL,
	pin integer NOT NULL,
	idfamilia integer NOT NULL,
	idnivel_acesso integer NOT NULL,
	CONSTRAINT fk_idfamilia FOREIGN KEY (idfamilia)
		REFERENCES familia (idfamilia),
	CONSTRAINT fk_idnivel_acesso FOREIGN KEY (idnivel_acesso)
		REFERENCES nivel_acesso (idnivel_acesso)
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
	saldo numeric(10,2) NOT NULL,
	CONSTRAINT fk_idconta FOREIGN KEY (idconta)
		REFERENCES conta (idconta)
);

CREATE TABLE definicao(
	iddefinicao serial PRIMARY KEY,
	descricao varchar(50) NOT NULL
);

CREATE TABLE categoria(
	idcategoria serial PRIMARY KEY,
	descricao varchar(100) NOT NULL
);

CREATE TABLE receita(  
	idreceita serial PRIMARY KEY, 
	idconta integer NOT NULL,
	iddefreceita integer NOT NULL,
	idcategoria integer NOT NULL,
	idusuario integer NOT NULL,
	
	descricao varchar(150) NOT NULL, 
	valor numeric NOT NULL, 
	diautil integer,  
	repetir integer,  
	datainicial date,

	CONSTRAINT fk_idconta FOREIGN KEY (idconta)
		REFERENCES conta (idconta),
	CONSTRAINT fk_idreceita_definicao FOREIGN KEY (iddefreceita)
      		REFERENCES definicao (iddefinicao),
	CONSTRAINT fk_idcategoria FOREIGN KEY (idcategoria)
		REFERENCES categoria (idcategoria),
	CONSTRAINT fk_idusuario FOREIGN KEY (idusuario)
		REFERENCES usuario (idusuario)
);

CREATE TABLE despesa(
 	iddespesa serial PRIMARY KEY,
 	iddefdespesa integer NOT NULL,
 	idconta integer NOT NULL,
 	idcategoria integer NOT NULL,
 	idusuario integer NOT NULL,
	
  	descricao varchar(100) NOT NULL,
 	valor numeric NOT NULL,
  	diautil integer,
 	repetir integer,
 	datainicial date,

 	CONSTRAINT fk_idconta FOREIGN KEY (idconta)
 		REFERENCES conta (idconta),
	CONSTRAINT fk_iddespesa_definicao FOREIGN KEY (iddefdespesa)
      		REFERENCES definicao (iddefinicao),
	CONSTRAINT fk_idcategoria FOREIGN KEY (idcategoria)
		REFERENCES categoria (idcategoria),
	CONSTRAINT fk_idusuario FOREIGN KEY (idusuario)
		REFERENCES usuario (idusuario)
);

CREATE TABLE movimentacao_despesa(
	idmovimentacao serial PRIMARY KEY,
	iddespesa integer NOT NULL,
	idconta integer NOT NULL,
	
	datamov date NOT NULL,
	valor numeric NOT NULL,
	pago boolean NOT NULL,

	CONSTRAINT fk_idconta FOREIGN KEY (idconta)
		REFERENCES conta (idconta),
	CONSTRAINT fk_iddespesa FOREIGN KEY (iddespesa)
		REFERENCES despesa (iddespesa)
);

CREATE TABLE movimentacao_receita(
	idmovimentacao serial PRIMARY KEY,
	idreceita integer NOT NULL,
	idconta integer NOT NULL,
	
	datamov date NOT NULL,
	valor numeric NOT NULL,
	recebido boolean NOT NULL,

	CONSTRAINT fk_idconta FOREIGN KEY (idconta)
		REFERENCES conta (idconta),
	CONSTRAINT fk_idreceita FOREIGN KEY (idconta)
		REFERENCES receita (idreceita)
);



--------------------------------------------
------------------FUNÇÕES-------------------

-- Function: sp_lanca_despesas_fixas()

-- DROP FUNCTION sp_lanca_despesas_fixas();

CREATE OR REPLACE FUNCTION sp_lanca_despesas_fixas()
  RETURNS character varying AS
$BODY$
DECLARE 
  rDespesa record;
  sDespesa varchar;
  nexiste  integer;
  dtutil   date;
BEGIN
  sDespesa = 'SELECT * FROM  despesa  des
              JOIN definicao_desprec dd ON dd.iddefinicao = des.iddefdespesa
              WHERE dd.descricao = ''FIXA'' ';

  FOR rDespesa IN EXECUTE sDespesa LOOP

    SELECT (rDespesa.diautil||'/'||
           (SELECT EXTRACT( 'Month' from now()) AS X)||'/'||
           (SELECT EXTRACT( 'Year' from now()) AS X))::date INTO dtutil;

    SELECT count(*) INTO nexiste FROM movimentacao_despesa WHERE 
        EXTRACT( 'Month' from data) = EXTRACT( 'Month' from now()) AND
        EXTRACT( 'Year' from data) = EXTRACT( 'Year' from now()) AND 
        iddespesa  = rDespesa.iddespesa;

    IF nexiste = 1 THEN
      UPDATE movimentacao_despesa 
         SET data = dtutil, 
             idconta = rDespesa.idconta,
             valor = rDespesa.valor
      WHERE EXTRACT( 'Month' from data) = EXTRACT( 'Month' from now()) AND
            EXTRACT( 'Year' from data) = EXTRACT( 'Year' from now()) AND 
            iddespesa  = rDespesa.iddespesa;
    ELSIF nexiste > 1 THEN
         RAISE EXCEPTION 'Alteração não permitida, alterar manualmente as movimentações';       
    ELSE
      INSERT INTO movimentacao_despesa(
              iddespesa, data, idconta, valor, pago)
      VALUES ( rDespesa.iddespesa, dtutil, rDespesa.idconta, rDespesa.valor, false);
    END IF;
  END LOOP;
  
  RETURN  '';
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION sp_lanca_despesas_fixas()
  OWNER TO postgres;
  
  
-- Function: sp_lanca_receitas_fixas()

-- DROP FUNCTION sp_lanca_receitas_fixas();

CREATE OR REPLACE FUNCTION sp_lanca_receitas_fixas()
  RETURNS character varying AS
$BODY$
DECLARE 
  rReceita record;
  sReceita varchar;
  nexiste  integer;
  dtutil   date;
BEGIN
  sReceita = 'SELECT * FROM  receita  rec
              JOIN definicao_desprec dd ON dd.iddefinicao = rec.iddefreceita
              WHERE dd.descricao = ''FIXA'' ';

  FOR rReceita IN EXECUTE sReceita LOOP

    SELECT (rReceita.diautil||'/'||
           (SELECT EXTRACT( 'Month' from now()) AS X)||'/'||
           (SELECT EXTRACT( 'Year' from now()) AS X))::date INTO dtutil;

    SELECT count(*) INTO nexiste FROM movimentacao_receita WHERE 
        EXTRACT( 'Month' from data) = EXTRACT( 'Month' from now()) AND
        EXTRACT( 'Year' from data) = EXTRACT( 'Year' from now()) AND 
        idreceita  = rReceita.idreceita;

    IF nexiste = 1 THEN
      UPDATE movimentacao_receita 
         SET data = dtutil, 
             idconta = rReceita.idconta,
             valor = rReceita.valor
      WHERE EXTRACT( 'Month' from data) = EXTRACT( 'Month' from now()) AND
            EXTRACT( 'Year' from data) = EXTRACT( 'Year' from now()) AND 
            iddespesa  = rDespesa.iddespesa;
    ELSIF nexiste > 1 THEN
         RAISE EXCEPTION 'Alteração não permitida, alterar manualmente as movimentações';       
    ELSE
      INSERT INTO movimentacao_receita(
              idreceita, data, idconta, valor, recebido)
      VALUES ( rReceita.idreceita, dtutil, rReceita.idconta, rReceita.valor, false);
    END IF;
  END LOOP;
  
  RETURN  '';
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION sp_lanca_receitas_fixas()
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

CREATE OR REPLACE FUNCTION ftgr_lanca_despesa()
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
ALTER FUNCTION ftgr_lanca_despesa()
  OWNER TO postgres;


-- Function: ftgr_lanca_receita()

-- DROP FUNCTION ftgr_lanca_receita();

CREATE OR REPLACE FUNCTION ftgr_lanca_receita()
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
    bmovimenta  = NEW.recebido;
    dmes = EXTRACT( 'Month' from new.datamov);
    dano = EXTRACT( 'Year' from new.datamov);
    cconta = new.idconta;
    nvalor = new.valor;
  ELSIF TG_OP = 'UPDATE' THEN
    bmovimenta  = NEW.recebido;
    dmes = EXTRACT( 'Month' from new.datamov);
    dano = EXTRACT( 'Year' from new.datamov);
    cconta = old.idconta;
    IF old.recebido = 'true' AND 
       new.recebido = 'false' THEN
       nvalor = old.valor*(-1);
       bmovimenta = old.recebido;
    ELSE
      nvalor = new.valor - old.valor;
    END IF;

  ELSIF TG_OP = 'DELETE' THEN
    bmovimenta  = OLD.recebido;
    dmes = EXTRACT( 'Month' from old.datamov);
    dano = EXTRACT( 'Year' from old.datamov);
    cconta = old.idconta;
    nvalor = old.valor * (-1);
  END IF;

  IF bmovimenta = 'true' THEN
      UPDATE saldo SET saldo = saldo + nvalor 
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
ALTER FUNCTION ftgr_lanca_receita()
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

CREATE TRIGGER tgr_lanca_receita
  BEFORE INSERT OR UPDATE OR DELETE
  ON movimentacao_receita
  FOR EACH ROW
  EXECUTE PROCEDURE ftgr_lanca_receita();
  

-- Trigger: tgr_lanca_saldo on conta

-- DROP TRIGGER tgr_lanca_saldo ON conta;

CREATE TRIGGER tgr_lanca_saldo
  AFTER INSERT
  ON conta
  FOR EACH ROW
  EXECUTE PROCEDURE ftgr_lanca_saldo();
  
  
-- Trigger: tgr_lanca_despesa on movimentacao_despesa

-- DROP TRIGGER tgr_lanca_despesa ON movimentacao_despesa;

CREATE TRIGGER tgr_lanca_despesa
  BEFORE INSERT OR UPDATE OR DELETE
  ON movimentacao_despesa
  FOR EACH ROW
  EXECUTE PROCEDURE ftgr_lanca_despesa();
  
  
