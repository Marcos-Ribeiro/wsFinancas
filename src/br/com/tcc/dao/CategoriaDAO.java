package br.com.tcc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.tcc.model.Categoria;

public class CategoriaDAO{
	private Connection conexao;
	
	public CategoriaDAO(Connection conexao) {
		this.conexao = conexao;
	}

	public List<Categoria> getAll() {
		List<Categoria> listacateg = new ArrayList<Categoria>();
		String sql =   "SELECT * FROM categoria ORDER BY descricao; ";
		
		try{		
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
							
			while( rs.next()){
				Categoria categ = new Categoria();
				
				categ.setId(rs.getInt(1));
				categ.setDescricao(rs.getString(2));
				
				listacateg.add(categ);
			}
			
			conexao.close();
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Aten��o n�o foi poss�vel listar as categorias de contas! \n "+
					" Mensagem T�cnica: " + e.toString());
		}	
		
		return listacateg;
	}

	public void save(Categoria categ) {
		String sql = "INSERT INTO categoria "
				+ " (descricao)"
		  + " VALUES (?);";  

			try {
				PreparedStatement stmt = conexao.prepareStatement(sql);
				
				stmt.setString(1, categ.getDescricao());
				stmt.executeUpdate();

				stmt.close();
				conexao.close();
				
			} catch (SQLException e) {
				throw new RuntimeException( "Aten��o n�o foi poss�vel salvar a categoria de conta! \n "+
						" Mensagem T�cnica: " + e.toString());	
			}			
		
	}

	public void update(Categoria categ) {
		String sql = "UPDATE categoria SET "
				+ "descricao = ? "				
				+ "WHERE idcategoria = ?;";

		try {
			
			PreparedStatement stmt = conexao.prepareStatement(sql);
			
			stmt.setString(1, categ.getDescricao());
			stmt.setInt(2, categ.getId());
			
			stmt.execute();
			conexao.close();
			stmt.close();
			
		} catch (Exception e) {
			throw new RuntimeException( "Aten��o n�o foi poss�vel alterar a categoria de conta! \n "+
					" Mensagem T�cnica: " + e.toString());
		}
		
	}

	public void delete(Integer id) {
		String sql = "DELETE FROM categoria WHERE idcategoria = ?";
		
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.execute();
			conexao.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException( "Aten��o. N�o foi poss�vel deletar a categoria! \n"
					+ "Mensagem T�cnica: " + e.toString().replaceAll("\"", ""));
		}
		
	}
	
	public Categoria getObject(Integer id) {
		
		Categoria categ = new Categoria();
		String sql =   "SELECT * FROM categoria WHERE idcategoria = ?; ";
		
		try{		
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			while( rs.next()){
				categ.setId(rs.getInt(1));
				categ.setDescricao(rs.getString(2));
			}
			conexao.close();
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Aten��o n�o foi poss�vel localizar essa categoria! \n "+
					" Mensagem T�cnica: " + e.toString());
		}	
		return categ;
	}



}
