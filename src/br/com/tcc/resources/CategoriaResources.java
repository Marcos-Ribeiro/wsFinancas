package br.com.tcc.resources;

import java.sql.Connection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.tcc.conection.ConnectionFactory;
import br.com.tcc.dao.CategoriaDAO;
import br.com.tcc.model.Categoria;

@Path("/categorias")
public class CategoriaResources {
	
	@GET
	@Produces("application/json" )
	public List<Categoria> getAll() {
		Connection conexao = new ConnectionFactory().getConnection();
		CategoriaDAO dao = new CategoriaDAO(conexao);
		return dao.getAll();
	}
	
	
	@Path("{id}")
	@GET
	@Produces("application/json")
	public Categoria getAllUser(@PathParam("id") Integer id) {
		Connection conexao = new ConnectionFactory().getConnection();
		CategoriaDAO dao = new CategoriaDAO(conexao);
		return dao.getObject(id);
	}

	
	@POST
	@Consumes("application/json")
	public void save(Categoria categ) {
		Categoria novaCateg = new Categoria();
		try {
			JSONObject jobj = new JSONObject(categ);			
			novaCateg.setDescricao(jobj.getString("descricao").toString());
		
			Connection conexao = new ConnectionFactory().getConnection();
			CategoriaDAO dao = new CategoriaDAO(conexao);
			dao.save(novaCateg);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	@PUT
	@Consumes("application/json; charset=UTF-8")
	@Encoded
	public void update(Categoria categ) {
		Categoria novaCateg = new Categoria();
		try {
			JSONObject jobj = new JSONObject(categ);	
			novaCateg.setId(jobj.getInt("id"));
			novaCateg.setDescricao(jobj.getString("descricao").toString());
		
			Connection conexao = new ConnectionFactory().getConnection();
			CategoriaDAO dao = new CategoriaDAO(conexao);
			dao.update(novaCateg);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}	
	}

	
	@Path("{id}")
	@DELETE
	@Produces("application/json")
	@Consumes("application/json")
	public String delete(@PathParam("id") Integer id) {
		String str;
		try{
			Connection conexao = new ConnectionFactory().getConnection();
			CategoriaDAO dao = new CategoriaDAO(conexao);
			dao.delete(id);
			str = "{\"categoria\":{\"msg\":\"Categoria exluída com sucesso!\"}}";
			return str;
		}catch(Exception e){
			str = "{\"categoria\":{\"msg\":\"" + e.getMessage() +"\"}}";
			return str;
		}
	}

}
