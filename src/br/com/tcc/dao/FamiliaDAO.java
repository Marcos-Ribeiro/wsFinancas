package br.com.tcc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.tcc.crud.Crud;
import br.com.tcc.model.Familia;

public class FamiliaDAO implements Crud<Familia> {

	private Connection conexao;
	
	public FamiliaDAO(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public List<Familia> getAll() {
		List<Familia> listaFamilia = new ArrayList<Familia>();
		String sql =   "SELECT * FROM familia;";
		
		try{		
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
							
			while( rs.next()){
				Familia familia = new Familia();

				familia.setId(rs.getInt(1));
				familia.setNome(rs.getString(2));
				familia.setEmail(rs.getString(3));
				familia.setLogin(rs.getString(4));
				familia.setSenha(rs.getString(5));
				
				listaFamilia.add(familia);
			}
			
			conexao.close();
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível listar os usuarios! \n "+
					" Mensagem Técnica: " + e.toString());
		}	
		
		return listaFamilia;
	}

	@Override
	public Familia save(Familia familia) {
		String sql = "INSERT INTO familia( "
				  + " nome,"
				  + " email, "
				  + " assuncao, "
				  + " senha)"
		  + " values( "
		  		+ "	?," 
		  		+ "	?," 
		  		+ " ?,"
		  		+ " ?)";  

			try {
				PreparedStatement stmt = conexao.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

				stmt.setString(1, familia.getNome());
				stmt.setString(2, familia.getEmail());
				stmt.setString(3, familia.getLogin());
				stmt.setString(4, familia.getSenha());
				
				int linhasInseridas = stmt.executeUpdate();
								
				//Executando o insert e obtendo o id gerado caso seja autoincremental
				if (linhasInseridas > 0){
					ResultSet generatedKeys = stmt.getGeneratedKeys();
					
					if (generatedKeys.next()) {
						familia.setId(generatedKeys.getInt(9));
					}
				}
				conexao.close();
				stmt.close();
			} catch (SQLException e) {
				throw new RuntimeException( "Atenção não foi possível salvar a familia! \n "+
						" Mensagem Técnica: " + e.toString());	
			}			
		
		return familia;
	}

	@Override
	public void update(Familia familia) {
		String sql = "UPDATE familia SET "
				  + " nome = ?,"
				  + " email = ?,"
				  + " login = ?,"
				  + " senha = ?)"				
				+ "WHERE idfamilia = ?";

		try {
			
			PreparedStatement stmt = conexao.prepareStatement(sql);

			stmt.setString(1, familia.getNome());
			stmt.setString(2, familia.getEmail());
			stmt.setString(3, familia.getLogin());
			stmt.setString(4, familia.getSenha());
			
			stmt.setInt(5, familia.getId());
			
			stmt.execute();
			conexao.close();
			stmt.close();
			
		} catch (Exception e) {
			throw new RuntimeException( "Atenção não foi possível alterar a usuario! \n "+
					" Mensagem Técnica: " + e.toString());
		}
		
	}

	@Override
	public void delete(Familia familia) {
		String sql = "DELETE FROM familia WHERE idfamilia = ?";
		
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, familia.getId());
			stmt.execute();
			conexao.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível deletar a familia! \n "+
					" Mensagem Técnica: " + e.toString());
		}		
	}
	
	@Override
	public Familia getObject(Integer id) {
		
		Familia familia = new Familia();
		String sql =   "SELECT * FROM familia WHERE id = ?; ";
		
		try{		
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			while( rs.next()){	
				familia.setId(rs.getInt(1));
				
			}
			conexao.close();
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível localizar essa familia! \n "+
					" Mensagem Técnica: " + e.toString());
		}	
		return familia;
	}

	@Override
	public List<Familia> getAllUser(Integer user) {
		// TODO Auto-generated method stub
		return null;
	}
}
