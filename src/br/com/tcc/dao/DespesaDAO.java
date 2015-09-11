package br.com.tcc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.tcc.model.Despesa;

public class DespesaDAO{

	private Connection conexao;
	SimpleDateFormat dformat = new SimpleDateFormat("dd/MM/yyyy");
	
	public DespesaDAO(Connection conexao) {
		this.conexao = conexao;
	}


	public List<Despesa> getAll() {
		
		List<Despesa> listaDespesas = new ArrayList<Despesa>();
		String sql =   "SELECT * FROM despesa ";
		
		try{		
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
							
			while( rs.next()){
				Despesa desp = new Despesa();
				
				desp.setId(rs.getInt(1));
				desp.setIddefdespesa(rs.getInt(2));
				desp.setIdconta(rs.getInt(3));
				desp.setIdcategoria(rs.getInt(4));
				desp.setIdusuario(rs.getInt(5));
				desp.setDescricao(rs.getString(6));
				desp.setValor(rs.getDouble(7));
				desp.setDiautil(rs.getInt(8));
				desp.setRepetir(rs.getInt(9));
				desp.setDatainicial(rs.getString(10));				
				
				listaDespesas.add(desp);
			}
			
			conexao.close();
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível listar todas as despesas! \n "+
					" Mensagem Técnica: " + e.toString());
		}	
		
		return listaDespesas;
	}


	public Despesa getObject(Integer id) {
		
		Despesa desp = new Despesa();
		String sql =   "SELECT * FROM despesa WHERE iddespesa = ?; ";
		
		try{
			
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			while( rs.next()){							
				desp.setId(rs.getInt(1));
				desp.setIddefdespesa(rs.getInt(2));
				desp.setIdconta(rs.getInt(3));
				desp.setIdcategoria(rs.getInt(4));
				desp.setIdusuario(rs.getInt(5));
				desp.setDescricao(rs.getString(6));
				desp.setValor(rs.getDouble(7));
				desp.setDiautil(rs.getInt(8));
				desp.setRepetir(rs.getInt(9));
				desp.setDatainicial(rs.getString(10));
					
			}
			
			conexao.close();
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível localizar essa despesa! \n "+
					" Mensagem Técnica: " + e.toString());
		}	
		return desp;
	}


	public Despesa save(Despesa despesa) {
		String sql = "INSERT INTO Despesa( "
				  + " iddefdespesa, "
				  + " idconta,"
				  + " idcategoria,"
				  + " idusuario,"
				  + " descricao, "
				  + " valor,"
				  + " diautil,"
				  + " repetir, "
				  + " datainicial)"
		  + " values( "
		  		+ "	?," 
		  		+ "	?," 
		  		+ " ?,"
		  		+ " ?,"
		  		+ " ?,"
		  		+ " ?,"
		  		+ "	?,"
		  		+ "	?,"
		  		+ " ?)";  

			try {
				PreparedStatement stmt = conexao.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				
				stmt.setInt(1, despesa.getIddefdespesa());
				stmt.setInt(2, despesa.getIdconta());
				stmt.setInt(3, despesa.getIdcategoria());
				stmt.setInt(4, despesa.getIdusuario());
				stmt.setString(5, despesa.getDescricao());
				stmt.setDouble(6, despesa.getValor());
				stmt.setInt(7, despesa.getDiautil());
				stmt.setInt(8, despesa.getRepetir());
				stmt.setString(9, despesa.getDatainicial());
				
				int linhasInseridas = stmt.executeUpdate();
								
				//Executando o insert e obtendo o id gerado caso seja autoincremental
				if (linhasInseridas > 0){
					ResultSet generatedKeys = stmt.getGeneratedKeys();
					
					if (generatedKeys.next()) {
						despesa.setId(generatedKeys.getInt(9));
					}
				}
					
				conexao.close();
				stmt.close();
			} catch (SQLException e) {
				throw new RuntimeException( "Atenção não foi possível salvar a despesa! \n "+
						" Mensagem Técnica: " + e.toString());	
			}			
		
		return despesa;
	}


	public void update(Despesa despesa) {
		String sql = "UPDATE despesa SET "
				  + " iddefdespesa = ?,"
				  + " idconta = ?,"
				  + " idcategoria = ?,"
				  + " idusuario = ?,"
				  + " descricao = ?,"
				  + " valor = ?,"
				  + " diautil = ?,"
				  + " repetir = ?,"
				  + " datainicial = ?)"				
				+ "WHERE iddespesa = ?";

		try {
			
			PreparedStatement stmt = conexao.prepareStatement(sql);

			stmt.setInt(1, despesa.getIddefdespesa());
			stmt.setInt(2, despesa.getIdconta());
			stmt.setInt(3, despesa.getIdcategoria());
			stmt.setInt(4, despesa.getIdusuario());
			stmt.setString(5, despesa.getDescricao());
			stmt.setDouble(6, despesa.getValor());
			stmt.setInt(7, despesa.getDiautil());
			stmt.setInt(8, despesa.getRepetir());
			stmt.setString(9, despesa.getDatainicial());
			stmt.setInt(10, despesa.getId());
			
			stmt.execute();
			conexao.close();
			stmt.close();
			
		} catch (Exception e) {
			throw new RuntimeException( "Atenção não foi possível alterar a despesa! \n "+
					" Mensagem Técnica: " + e.toString());
		}
	}


	public void deleteMov(Integer idmov) {
		
		try {
			String sql = "DELETE FROM movimentacao_despesa WHERE idmovimentacao = ?;";
			PreparedStatement stmt;
			
			stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, idmov);
			stmt.execute();
			
			stmt.close();
			conexao.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível excluir essa movimentação! \n "+
					" Mensagem Técnica: " + e.toString().replaceAll("\"", ""));
		}
		
	}
	

	public void deleteDesp(Integer iddesp) {
		try {
			String sql = "DELETE FROM despesa WHERE iddespesa = ?;";
			PreparedStatement stmt;
			
			stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, iddesp);
			stmt.execute();
			
			stmt.close();	
			conexao.close();
			
		} catch (SQLException e) {
			//OMITIDO
		}
	}
	
	
	public List<Despesa> getAllUser(Integer user) {
		List<Despesa> listaDesp = new ArrayList<Despesa>();
		
		String sql = "SELECT * FROM despesa d INNER JOIN movimentacao_despesa mv ON d.iddespesa = mv.iddespesa WHERE d.idusuario = ? ORDER BY mv.datamov;";
	
		try{
			
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			stmt.setInt(1, user);
			ResultSet rs = stmt.executeQuery();
			
			while( rs.next()){				
				Despesa desp = new Despesa();
				
				desp.setId(rs.getInt("iddespesa"));
				desp.setIddefdespesa(rs.getInt("iddefdespesa"));
				desp.setIdconta(rs.getInt("idconta"));
				desp.setIdcategoria(rs.getInt("idcategoria"));
				desp.setIdusuario(rs.getInt("idusuario"));
				desp.setDescricao(rs.getString("descricao"));
				desp.setValor(rs.getDouble("valor"));
				desp.setDiautil(rs.getInt("diautil"));
				desp.setRepetir(rs.getInt("repetir"));
				desp.setDatainicial(dformat.format(rs.getDate("datainicial")));
				
				desp.setIdmov(rs.getInt(11));
				desp.setDatamov(dformat.format(rs.getDate("datamov")));
				desp.setPago(rs.getBoolean("pago"));
				
				listaDesp.add(desp);
			}
			
			conexao.close();
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível localizar as despesas! \n "+
					" Mensagem Técnica: " + e.toString().replaceAll("\"",""));
		}	
		return listaDesp;
	}
}
