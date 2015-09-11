package br.com.tcc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.tcc.model.Receita;

public class ReceitaDAO{

	private Connection conexao;
	SimpleDateFormat dformat = new SimpleDateFormat("dd/MM/yyyy");
	
	public ReceitaDAO(Connection conexao) {
		this.conexao = conexao;
	}


	public List<Receita> getAll() {
		List<Receita> listaReceita = new ArrayList<Receita>();
		String sql =   "SELECT * FROM receita;";
		
		try{		
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
							
			while( rs.next()){
				Receita receita = new Receita();

				receita.setId(rs.getInt(1));
				receita.setIdconta(rs.getInt(2));
				receita.setIddefreceita(rs.getInt(3));
				receita.setIdcategoria(rs.getInt(4));
				receita.setIdusuario(rs.getInt(5));
				receita.setDescricao(rs.getString(6));
				receita.setValor(rs.getDouble(7));
				receita.setDiautil(rs.getInt(8));
				receita.setRepetir(rs.getInt(9));
				receita.setDatainicial(rs.getString(10));
				
				listaReceita.add(receita);
			}
			
			conexao.close();
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível listar as receitas! \n "+
					" Mensagem Técnica: " + e.toString());
		}	
		
		return listaReceita;
	}


	public Receita save(Receita receita) {
		String sql = "INSERT INTO Receita( "
				  + " idconta,"
				  + " iddefreceita, "
				  + " idcategoria, "
				  + " idusuario, "
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

				stmt.setInt(1, receita.getIdconta());
				stmt.setInt(2, receita.getIddefreceita());
				stmt.setInt(3, receita.getIdcategoria());
				stmt.setInt(4, receita.getIdusuario());
				stmt.setString(5, receita.getDescricao());
				stmt.setDouble(6, receita.getValor());
				stmt.setInt(7, receita.getDiautil());
				stmt.setInt(8, receita.getRepetir());
				stmt.setString(9, receita.getDatainicial());
				
				int linhasInseridas = stmt.executeUpdate();
								
				//Executando o insert e obtendo o id gerado caso seja autoincremental
				if (linhasInseridas > 0){
					ResultSet generatedKeys = stmt.getGeneratedKeys();
					
					if (generatedKeys.next()) {
						receita.setId(generatedKeys.getInt(9));
					}
				}
				conexao.close();
				stmt.close();
			} catch (SQLException e) {
				throw new RuntimeException( "Atenção não foi possível salvar a receita! \n "+
						" Mensagem Técnica: " + e.toString());	
			}			
		
		return receita;
	}


	public void update(Receita receita) {
		String sql = "UPDATE receita SET "
				  + " idconta = ?,"
				  + " iddefreceita = ?,"
				  + " idcategoria = ?,"
				  + " idusuario = ?,"
				  + " descricao = ?,"
				  + " valor = ?,"
				  + " diautil = ?,"
				  + " repetir = ?,"
				  + " datainicial = ?)"				
				+ "WHERE idreceita = ?";

		try {
			
			PreparedStatement stmt = conexao.prepareStatement(sql);

			stmt.setInt(1, receita.getIdconta());
			stmt.setInt(2, receita.getIddefreceita());
			stmt.setInt(3, receita.getIdcategoria());
			stmt.setInt(4, receita.getIdusuario());
			stmt.setString(5, receita.getDescricao());
			stmt.setDouble(6, receita.getValor());
			stmt.setInt(7, receita.getDiautil());
			stmt.setInt(8, receita.getRepetir());
			stmt.setString(9, receita.getDatainicial());
			stmt.setInt(10, receita.getId());
			
			stmt.execute();
			conexao.close();
			stmt.close();
			
		} catch (Exception e) {
			throw new RuntimeException( "Atenção não foi possível alterar a receita! \n "+
					" Mensagem Técnica: " + e.toString());
		}
		
	}


	public void delete(Receita receita) {
		String sql = "DELETE FROM receita WHERE idreceita = ?";
		
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, receita.getId());
			stmt.execute();
			conexao.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível deletar a receita! \n "+
					" Mensagem Técnica: " + e.toString());
		}		
	}


	public Receita getObject(Integer id) {
		
		Receita receita = new Receita();
		String sql =   "SELECT * FROM receita WHERE idreceita = ?; ";
		
		try{		
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			while( rs.next()){	
				receita.setId(rs.getInt(1));
				receita.setIdconta(rs.getInt(2));
				receita.setIddefreceita(rs.getInt(3));
				receita.setIdcategoria(rs.getInt(4));
				receita.setIdusuario(rs.getInt(5));
				receita.setDescricao(rs.getString(6));
				receita.setValor(rs.getDouble(7));
				receita.setDiautil(rs.getInt(8));
				receita.setRepetir(rs.getInt(9));
				receita.setDatainicial(rs.getString(10));
				
			}
			conexao.close();
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível localizar essa receita! \n "+
					" Mensagem Técnica: " + e.toString());
		}	
		return receita;
	}


	public List<Receita> getAllUser(Integer user) {
		List<Receita> listaRec = new ArrayList<Receita>();
		String sql =   "SELECT * FROM receita r INNER JOIN movimentacao_receita mv ON r.idreceita = mv.idreceita WHERE r.idusuario = ? ORDER BY mv.datamov;";
		
		try{
			
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			stmt.setInt(1, user);
			ResultSet rs = stmt.executeQuery();
			
			while( rs.next()){					
				Receita rec = new Receita();
				
				rec.setId(rs.getInt(1));
				rec.setIddefreceita(rs.getInt(2));
				rec.setIdconta(rs.getInt(3));
				rec.setIdcategoria(rs.getInt(4));
				rec.setIdusuario(rs.getInt(5));
				rec.setDescricao(rs.getString(6));
				rec.setValor(rs.getDouble(7));
				rec.setDiautil(rs.getInt(8));
				rec.setRepetir(rs.getInt(9));
				rec.setDatainicial(dformat.format(rs.getDate(10)));
					
				rec.setIdmov(rs.getInt(11));
				rec.setDatamov(dformat.format(rs.getDate(14)));
				rec.setPago(rs.getBoolean(16));
				
				listaRec.add(rec);
			}
			
			conexao.close();
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			throw new RuntimeException( "Atenção não foi possível localizar as receitas! \n "+
					" Mensagem Técnica: " + e.toString());
		}	
		return listaRec;
	}
}
