package br.com.tcc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.tcc.model.Definicao;

public class DefinicaoDAO{

	private Connection conexao;
	
	public DefinicaoDAO(Connection conexao) {
		this.conexao = conexao;
	}


	public List<Definicao> getAll() {
		List<Definicao> listadefinicao = new ArrayList<Definicao>();
		String sql =   "SELECT * FROM definicao; ";
		
		try{		
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
							
			while( rs.next()){
				Definicao definicao = new Definicao();
				
				definicao.setId(rs.getInt(1));
				definicao.setDescricao(rs.getString(2));
				
				listadefinicao.add(definicao);
			}
			
			conexao.close();
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Aten��o n�o foi poss�vel listar as defini��es de contas! \n "+
					" Mensagem T�cnica: " + e.toString());
		}	
		
		return listadefinicao;
	}


	public Definicao save(Definicao definicao) {
		String sql = "INSERT INTO definicao( "
				+ " iddefinicao,"
				+ " descricao)"
		  + " values( "
		  		+ "	?," 
		  		+ " ?)";  

			try {
				PreparedStatement stmt = conexao.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				
				stmt.setInt(1, definicao.getId());
				stmt.setString(2, definicao.getDescricao());
				
				int linhasInseridas = stmt.executeUpdate();
								
				//Executando o insert e obtendo o id gerado caso seja autoincremental
				if (linhasInseridas > 0){
					ResultSet generatedKeys = stmt.getGeneratedKeys();
					
					if (generatedKeys.next()) {
						definicao.setId(generatedKeys.getInt(9));
					}
				}
				conexao.close();
				stmt.close();
			} catch (SQLException e) {
				throw new RuntimeException( "Aten��o n�o foi poss�vel salvar a definicao de conta! \n "+
						" Mensagem T�cnica: " + e.toString());	
			}			
		
		return definicao;
	}


	public void update(Definicao definicao) {
		String sql = "UPDATE definicao SET "
				+ "iddefinicao = ?,"
				+ "descricao = ?,"				
				+ "WHERE iddefinicao = ?";

		try {
			
			PreparedStatement stmt = conexao.prepareStatement(sql);
			
			stmt.setInt(1, definicao.getId());
			stmt.setString(2, definicao.getDescricao());
			
			stmt.execute();
			conexao.close();
			stmt.close();
			
		} catch (Exception e) {
			throw new RuntimeException( "Aten��o n�o foi poss�vel alterar a definicao de conta! \n "+
					" Mensagem T�cnica: " + e.toString());
		}
		
	}


	public void delete(Definicao definicao) {
		String sql = "DELETE FROM definicao WHERE iddefinicao = ?";
		
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, definicao.getId());
			stmt.execute();
			conexao.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException( "Aten��o n�o foi poss�vel deletar a definicao de conta! \n "+
					" Mensagem T�cnica: " + e.toString());
		}
		
	}


	public Definicao getObject(Integer id) {
		
		Definicao definicao = new Definicao();
		String sql =   "SELECT * FROM definicao WHERE iddefinicao = ?; ";
		
		try{		
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			while( rs.next()){
				definicao.setId(rs.getInt(1));
				definicao.setDescricao(rs.getString(2));
			}
			conexao.close();
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Aten��o n�o foi poss�vel localizar essa defini��o! \n "+
					" Mensagem T�cnica: " + e.toString());
		}	
		return definicao;
	}

}
