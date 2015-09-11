package br.com.tcc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.tcc.model.Usuario;

public class UsuarioDAO{
	
	private Connection conexao;
	
	public UsuarioDAO(Connection conexao) {
		this.conexao = conexao;
	}


	public List<Usuario> getAll() {
		List<Usuario> listaUsuario = new ArrayList<Usuario>();
		String sql =   "SELECT * FROM usuario;";
		
		try{		
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
							
			while( rs.next()){
				Usuario usuario = new Usuario();

				usuario.setId(rs.getInt(1));
				usuario.setNome(rs.getString(2));
				usuario.setPin(rs.getString(3));
				
				listaUsuario.add(usuario);
			}
			
			conexao.close();
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível listar os usuarios! \n "+
					" Mensagem Técnica: " + e.toString());
		}	
		
		return listaUsuario;
	}


	public Usuario save(Usuario usuario) {
		String sql = "INSERT INTO Usuario( "
				  + " nome,"
				  + " pin)"
		  + " values( "
		  		+ "	?," 
		  		+ "	?)";  

			try {
				PreparedStatement stmt = conexao.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

				stmt.setString(1, usuario.getNome());
				stmt.setString(2, usuario.getPin());
				
				int linhasInseridas = stmt.executeUpdate();
								
				//Executando o insert e obtendo o id gerado caso seja autoincremental
				if (linhasInseridas > 0){
					ResultSet generatedKeys = stmt.getGeneratedKeys();
					
					if (generatedKeys.next()) {
						usuario.setId(generatedKeys.getInt(9));
					}
				}
				conexao.close();
				stmt.close();
			} catch (SQLException e) {
				throw new RuntimeException( "Atenção não foi possível salvar o usuario! \n "+
						" Mensagem Técnica: " + e.toString());	
			}			
		
		return usuario;
	}


	public void update(Usuario usuario) {
		String sql = "UPDATE usuario SET "
				  + " nome = ?,"
				  + " pin = ?"				
				+ "WHERE id = ?";

		try {
			
			PreparedStatement stmt = conexao.prepareStatement(sql);

			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getPin());
			
			stmt.setInt(3, usuario.getId());
			
			stmt.execute();
			conexao.close();
			stmt.close();
			
		} catch (Exception e) {
			throw new RuntimeException( "Atenção não foi possível alterar a usuario! \n "+
					" Mensagem Técnica: " + e.toString());
		}
		
	}


	public void delete(Usuario usuario) {
		String sql = "DELETE FROM usuario WHERE id = ?";
		
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, usuario.getId());
			stmt.execute();
			conexao.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível deletar a receita! \n "+
					" Mensagem Técnica: " + e.toString());
		}		
	}


	public Usuario getObject(Integer id) {
		
		Usuario usuario = new Usuario();
		String sql =   "SELECT * FROM usuario WHERE idusuario = ?; ";
		
		try{		
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			while( rs.next()){	
				usuario.setId(rs.getInt(1));				
			}
			conexao.close();
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível localizar essa usuario! \n "+
					" Mensagem Técnica: " + e.toString());
		}	
		return usuario;
	}

	public Usuario getLogin(String usuario, String pin){
		String sql = "SELECT * FROM usuario WHERE nome=? AND pin=?;";
		Usuario usuarioValido = new Usuario();
		try{
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, usuario);
			stmt.setString(2, pin);
			ResultSet rs = stmt.executeQuery();
			
			while( rs.next()){	
				usuarioValido.setId(rs.getInt(1));
				usuarioValido.setNome(rs.getString(2));
				usuarioValido.setPin(rs.getString(3));
			}
			conexao.close();
			stmt.close();
			rs.close();
			
		}catch(SQLException e){
			throw new RuntimeException( "Login e/ou senha inválidos! \n "+
					" Mensagem Técnica: " + e.toString().replaceAll("\"", ""));
		}
		return usuarioValido;
	}

}
