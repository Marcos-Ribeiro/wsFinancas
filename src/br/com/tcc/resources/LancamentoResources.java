package br.com.tcc.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/lancamentos")
public class LancamentoResources {

	@GET
	@Produces("text/*")
	public String novo(){
		
		return "Salvo com sucesso!";
	}
	
}
