package br.com.tcc.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Conta {
	
	private int id;
	private String descricao;
	private int idusuario;
	
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
	public int getIdusuario() {
		return idusuario;
	}
	public void setIdusuario(int idusuario) {
		this.idusuario = idusuario;
	}	
}
