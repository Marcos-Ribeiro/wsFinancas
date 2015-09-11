package br.com.tcc.resources;

import java.sql.Connection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.tcc.conection.ConnectionFactory;
import br.com.tcc.dao.DespesaDAO;
import br.com.tcc.model.Despesa;

@Path("/despesas")
public class DespesaResources{
	
	@GET
	@Produces("application/json" )
	public List<Despesa> getAll() {
		Connection conexao = new ConnectionFactory().getConnection();
		DespesaDAO dao = new DespesaDAO(conexao);
		return dao.getAll();
	}
	
	
	@Path("{idusuario}")
	@GET
	@Produces("application/json")
	public List<Despesa> getAllUser(@PathParam("idusuario") Integer user) {
		Connection conexao = new ConnectionFactory().getConnection();
		DespesaDAO dao = new DespesaDAO(conexao);
		return dao.getAllUser(user);
	}

	
	@PUT
	@Consumes("application/json")
	public void save(Despesa despesa) {
		Connection conexao = new ConnectionFactory().getConnection();
		DespesaDAO dao = new DespesaDAO(conexao);
		dao.save(despesa);
	}


	@Path("{id}")
	@POST
	@Consumes("application/json")
	public void update(Despesa despesa) {
		Connection conexao = new ConnectionFactory().getConnection();
		DespesaDAO dao = new DespesaDAO(conexao);
		dao.update(despesa);	
	}

	
	@Path("{idmov}/{iddesp}")
	@DELETE
	@Produces("application/json")
	@Consumes("application/json")
	public String delete(@PathParam("idmov") Integer idmovimentacao, @PathParam("iddesp") Integer iddesp) {
		
		String str = "{\"despesa\":{\"msg\":\"Nenhuma ação!\"}}" ;
		
		try{
			Connection conexaomov = new ConnectionFactory().getConnection();
			DespesaDAO daomov = new DespesaDAO(conexaomov);			
			daomov.deleteMov(idmovimentacao);
			
			Connection conexaodesp = new ConnectionFactory().getConnection();
			DespesaDAO daodesp = new DespesaDAO(conexaodesp);
			daodesp.deleteDesp(iddesp);
		
			str = "{\"despesa\":{\"msg\":\"Despesa excluída com sucesso!\"}}";
			return str;
			
		}catch(Exception e){
			
			str = "{\"despesa\":{\"msg\":\"" + e.getMessage() +"\"}}";
			return str;
		}
	}
	
}
