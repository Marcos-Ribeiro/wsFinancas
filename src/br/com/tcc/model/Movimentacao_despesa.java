package br.com.tcc.model;

import java.sql.Date;

public class Movimentacao_despesa {
/**
 *   iddespesa integer,
	  data date,
	  idconta integer,
	  valor numeric,
	  pago boolean,
	  idmovimentacao
 */
	
	private int id;
	private int iddespesa;
	private Date data;
	private int idconta;
	private Double valor;
	private boolean pago;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIddespesa() {
		return iddespesa;
	}
	public void setIddespesa(int iddespesa) {
		this.iddespesa = iddespesa;
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
	public boolean isPago() {
		return pago;
	}
	public void setPago(boolean pago) {
		this.pago = pago;
	}
	
}
