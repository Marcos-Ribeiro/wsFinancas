package br.com.tcc.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Usuario {

	private int id;
	private String nome;
	private String login;
	private String senha;
	
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getPin() {
		return senha;
	}
	public void setPin(String pin) {
		this.senha = pin;
	}
}
