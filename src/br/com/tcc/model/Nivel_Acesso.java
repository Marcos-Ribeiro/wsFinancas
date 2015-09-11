package br.com.tcc.model;

import javax.xml.bind.annotation.XmlRootElement;

/**  
 * idnivel_acesso serial NOT NULL,
   descricao character varying(50)
*/
@XmlRootElement
public class Nivel_Acesso {

	private int id;
	private String descricao;
	
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
	
	
}
