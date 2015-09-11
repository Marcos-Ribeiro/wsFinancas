package br.com.tcc.model;

import java.sql.Date;

public class Movimentacao_receita {
/**
 *   idreceita integer,
	  data date,
	  idconta integer,
	  valor numeric,
	  recebido boolean,
	  idmovimentacao serial NOT NULL,
 */
	private int id;
	private int idreceita;
	private Date data;
	private int idconta;
	private Double valor;
	private boolean recebido;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdreceita() {
		return idreceita;
	}
	public void setIdreceita(int idreceita) {
		this.idreceita = idreceita;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
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
	public boolean isRecebido() {
		return recebido;
	}
	public void setRecebido(boolean recebido) {
		this.recebido = recebido;
	}
}
