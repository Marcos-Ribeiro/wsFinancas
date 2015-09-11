package br.com.tcc.resources;

import java.sql.Connection;
import java.util.ArrayList;
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
import br.com.tcc.dao.ReceitaDAO;
import br.com.tcc.model.Receita;

@Path("/receitas")
public class ReceitaResources{

	@GET
	@Produces("application/json" )
	public List<Receita> getAll() {
		Connection conexao = new ConnectionFactory().getConnection();
		ReceitaDAO dao = new ReceitaDAO(conexao);
		return dao.getAll();
	}
	
	@Path("{idusuario}")
	@GET
	@Produces("application/json")
	public List<Receita> getAllUser(@PathParam("idusuario") Integer user) {
		List<Receita> list = new ArrayList<Receita>();
		Connection conexao = new ConnectionFactory().getConnection();
		ReceitaDAO dao = new ReceitaDAO(conexao);
		list = dao.getAllUser(user);
		return list;
	}


	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Receita save(Receita receita) {
		Connection conexao = new ConnectionFactory().getConnection();
		ReceitaDAO dao = new ReceitaDAO(conexao);
		dao.save(receita);
		return receita;
	}

	@Path("{id}")
	@POST
	@Consumes("application/json")
	public void update(Receita receita) {
		Connection conexao = new ConnectionFactory().getConnection();
		ReceitaDAO dao = new ReceitaDAO(conexao);
		dao.update(receita);	
	}

	@Path("{id}")
	@DELETE
	@Consumes("application/json")
	public void delete(Receita receita) {
		Connection conexao = new ConnectionFactory().getConnection();
		ReceitaDAO dao = new ReceitaDAO(conexao);
		dao.delete(receita);
	}

}
