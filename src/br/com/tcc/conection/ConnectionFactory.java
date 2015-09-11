package br.com.tcc.conection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	public Connection getConnection(){
		Connection con = null;
        String url = "jdbc:postgresql://localhost:5432/economy";   
        String usuario = "postgres";  
        String senha = "postgres";
        
        try {        
        	Class.forName("org.postgresql.Driver");
        	con = DriverManager.getConnection(url,usuario,senha);
        	
        }catch (ClassNotFoundException e) {
			System.err.println("Erro de conexão com banco de dados: ClassNotFound - " + e.getMessage() );
		}catch (SQLException e) {
			System.err.println("Erro de conexão com banco de dados: SQLException - " + e.getMessage() );		
		}
      
		return con;
	}
	

}
