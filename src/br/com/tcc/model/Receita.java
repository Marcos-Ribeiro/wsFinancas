package br.com.tcc.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Receita {

	/***
	  idreceita serial NOT NULL,
	  idconta integer NOT NULL,
	  iddefreceita integer NOT NULL,
	  idcategoria integer NOT NULL,
	  idusuario integer NOT NULL,
	  descricao character varying(150) NOT NULL,
	  valor numeric NOT NULL,
	  diautil integer,
	  repetir integer,
	  datainicial date,
	 */

	private int id;
	private int idcategoria;
	private int idusuario;
	private String descricao;
	private int iddefreceita;
	private int diautil;
	private int repetir;
	private String datainicial;
	private int idconta;
	private Double valor;
	
	/*mov*/
	private int idmov;
	private String datamov;
	private boolean pago;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdcategoria() {
		return idcategoria;
	}
	public void setIdcategoria(int idcategoria) {
		this.idcategoria = idcategoria;
	}
	public int getIdusuario() {
		return idusuario;
	}
	public void setIdusuario(int idusuario) {
		this.idusuario = idusuario;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public int getIddefreceita() {
		return iddefreceita;
	}
	public void setIddefreceita(int iddefreceita) {
		this.iddefreceita = iddefreceita;
	}
	public int getDiautil() {
		return diautil;
	}
	public void setDiautil(int diautil) {
		this.diautil = diautil;
	}
	public int getRepetir() {
		return repetir;
	}
	public void setRepetir(int repetir) {
		this.repetir = repetir;
	}
	public String getDatainicial() {
		return datainicial;
	}
	public void setDatainicial(String datainicial) {
		this.datainicial = datainicial;
	}
	public int getIdconta() {
		return idconta;
	}
	public void setIdconta(int idconta) {
		this.idconta = idconta;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public int getIdmov() {
		return idmov;
	}
	public void setIdmov(int idmov) {
		this.idmov = idmov;
	}
	public String getDatamov() {
		return datamov;
	}
	public void setDatamov(String datamov) {
		this.datamov = datamov;
	}
	public boolean isPago() {
		return pago;
	}
	public void setPago(boolean pago) {
		this.pago = pago;
	}	
}
