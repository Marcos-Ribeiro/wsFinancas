package br.com.tcc.model;

public class Saldo {

	/**
	idsaldo serial PRIMARY KEY,
	idconta integer NOT NULL,	
	mes integer NOT NULL,
	ano integer NOT NULL,
	saldo numeric(10,2) NOT NULL,
	 */
	
	private int id;
	private int idconta;
	private int mes;
	private int ano;
	private Double saldo;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdconta() {
		return idconta;
	}
	public void setIdconta(int idconta) {
		this.idconta = idconta;
	}
	public int getMes() {
		return mes;
	}
	public void setMes(int mes) {
		this.mes = mes;
	}
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	
	

}
