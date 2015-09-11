package br.com.tcc.resources;

import java.sql.Connection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import br.com.tcc.conection.ConnectionFactory;
import br.com.tcc.dao.UsuarioDAO;
import br.com.tcc.model.Usuario;

@Path("/usuarios")
public class UsuarioResources {

	/*@Path("{id}")
	@GET
	@Produces("application/json")*/
	public Usuario getObject(/*@PathParam("id") */Integer id) {
		Connection conexao = new ConnectionFactory().getConnection();
		UsuarioDAO dao = new UsuarioDAO(conexao);
		return dao.getObject(id);
	}

	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Usuario save(Usuario usuario) {
		Connection conexao = new ConnectionFactory().getConnection();
		UsuarioDAO dao = new UsuarioDAO(conexao);
		dao.save(usuario);
		return usuario;
	}
	
	@GET
	@Produces("application/json" )
	public List<Usuario> getAll() {
		Connection conexao = new ConnectionFactory().getConnection();
		UsuarioDAO dao = new UsuarioDAO(conexao);		
		return dao.getAll();
	}

	@Path("{id}")
	@POST
	@Consumes("application/json")
	public void update(Usuario usuario) {
		Connection conexao = new ConnectionFactory().getConnection();
		UsuarioDAO dao = new UsuarioDAO(conexao);
		dao.update(usuario);
	}

	@Path("{id}")
	@DELETE
	@Produces("application/json")
	public void delete(Usuario usuario) {
		Connection conexao = new ConnectionFactory().getConnection();
		UsuarioDAO dao = new UsuarioDAO(conexao);
		dao.delete(usuario);
	}


}