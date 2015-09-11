package br.com.tcc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.tcc.model.Conta;

public class ContaDAO{

	private Connection conexao;
	
	public ContaDAO(Connection conexao) {
		this.conexao = conexao;
	}

	public List<Conta> getAll() {
		
		List<Conta> listaconta = new ArrayList<Conta>();
		String sql =   "SELECT * FROM conta ORDER BY descricao; ";
		
		try{		
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
							
			while( rs.next()){
				Conta conta = new Conta();				

				conta.setId(rs.getInt(1));
				conta.setIdusuario(rs.getInt(2));
				conta.setDescricao(rs.getString(3));
				
				listaconta.add(conta);
			}
			
			conexao.close();
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível listar as contas! \n "+
					" Mensagem Técnica: " + e.toString());
		}	
		
		return listaconta;
	}


	public Conta save(Conta conta) {
		String sql = "INSERT INTO conta( "
				  + " idusuario, "
				  + " descricao)"
		  + " values( " 
		  		+ " ?,"
		  		+ " ?)";  

			try {
				PreparedStatement stmt = conexao.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				
				stmt.setInt(1, conta.getIdusuario());
				stmt.setString(2, conta.getDescricao());
				
				int linhasInseridas = stmt.executeUpdate();
								
				//Executando o insert e obtendo o id gerado caso seja autoincremental
				if (linhasInseridas > 0){
					ResultSet generatedKeys = stmt.getGeneratedKeys();
					
					if (generatedKeys.next()) {
						conta.setId(generatedKeys.getInt(9));
					}
				}
				conexao.close();
				stmt.close();	
				
			} catch (SQLException e) {
				throw new RuntimeException( "Atenção não foi possível salvar a conta! \n "+
						" Mensagem Técnica: " + e.toString());	
			}			
		
		return conta;
	}


	public void update(Conta conta) {
		String sql = "UPDATE conta SET "
				+ "idusuario = ?,"	
				+ "descricao = ?"			
				+ "WHERE idconta = ?";

		try {
			
			PreparedStatement stmt = conexao.prepareStatement(sql);
			
			stmt.setInt(1, conta.getIdusuario());
			stmt.setString(2, conta.getDescricao());
			stmt.setInt(3, conta.getId());
			
			stmt.execute();
			conexao.close();
			stmt.close();
		} catch (Exception e) {
			throw new RuntimeException( "Atenção não foi possível alterar a conta! \n "+
					" Mensagem Técnica: " + e.toString());
		}
		
	}


	public void delete(Conta conta) {
		String sql = "DELETE FROM conta WHERE idconta = ?";
		
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, conta.getId());
			stmt.execute();
			conexao.close();
			stmt.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível deletar a conta! \n "+
					" Mensagem Técnica: " + e.toString());
		}
		
	}


	public Conta getObject(Integer id) {
		
		Conta conta = new Conta();
		String sql =   "SELECT * FROM conta WHERE idconta = ?; ";
		
		try{		
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			while( rs.next()){
				conta.setId(rs.getInt(1));
				conta.setIdusuario(rs.getInt(2));
				conta.setDescricao(rs.getString(3));
								
			}		
			conexao.close();
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível localizar essa conta! \n "+
					" Mensagem Técnica: " + e.toString());
		}	
		return conta;
	}


	public List<Conta> getAllUser(Integer user) {
		List<Conta> listaConta = new ArrayList<Conta>();
		String sql =   "SELECT * FROM conta WHERE idusuario = ? ORDER BY descricao; ";
		
		try{		
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			stmt.setInt(1, user);
			ResultSet rs = stmt.executeQuery();
			
			while( rs.next()){
				Conta conta = new Conta();
				
				conta.setId(rs.getInt(1));
				conta.setIdusuario(rs.getInt(2));
				conta.setDescricao(rs.getString(3));
				
				listaConta.add(conta);
			}		
			conexao.close();
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível localizar essa conta! \n "+
					" Mensagem Técnica: " + e.toString());
		}	
		return listaConta;
	}

}
