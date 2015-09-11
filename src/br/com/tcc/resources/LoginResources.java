package br.com.tcc.resources;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.tcc.conection.ConnectionFactory;
import br.com.tcc.dao.UsuarioDAO;
import br.com.tcc.model.Usuario;

@Path("login")
public class LoginResources {

	@Path("{nome}/{pin}")
	@GET
	@Produces("application/json")
	public List<Usuario> login(@PathParam("nome") String usuario,@PathParam("pin") String pin) {
		
		Connection conexao = new ConnectionFactory().getConnection();
		
		UsuarioDAO daoUsuario = new UsuarioDAO(conexao);	
		List<Usuario> userLogado = new ArrayList<Usuario>();
		
		userLogado.add(daoUsuario.getLogin(usuario, pin));
			
		return userLogado;
	}
}
