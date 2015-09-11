package br.com.tcc.model;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Despesa {
	
	private int id;
	private int iddefdespesa;
	private int idconta;
	private int idcategoria;
	private int idusuario;
	private String descricao;
	private int diautil;
	private int repetir;
	private double valor;
	private String datainicial;

	/*mov*/
	private int idmov;
	private String datamov;
	private boolean pago;
	
	
	public String getDatamov() {
		return datamov;
	}
	public void setDatamov(String datamov) {
		this.datamov = datamov;
	}
	public int getIdmov() {
		return idmov;
	}
	public void setIdmov(int idmov) {
		this.idmov = idmov;
	}
	public String getData() {
		return datamov;
	}
	public void setData(String datamov) {
		this.datamov = datamov;
	}
	public boolean isPago() {
		return pago;
	}
	public void setPago(boolean pago) {
		this.pago = pago;
	}

	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public int getIddefdespesa() {
		return iddefdespesa;
	}
	public void setIddefdespesa(int iddefdespesa) {
		this.iddefdespesa = iddefdespesa;
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
	public int getIdconta() {
		return idconta;
	}
	public void setIdconta(int idconta) {
		this.idconta = idconta;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public String getDatainicial() {
		return datainicial;
	}
	public void setDatainicial(String datainicial) {
		this.datainicial = datainicial;
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
}
