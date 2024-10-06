package progetto_java_e_voting_plat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class UtenteDAOImplementation extends DAOImplementationBasics implements UtenteDAO {
	
	private static UtenteDAOImplementation singleton_instance;
	
	public static UtenteDAOImplementation get_singleton_instance() {
		if(singleton_instance == null)
			singleton_instance = new UtenteDAOImplementation();
		return singleton_instance;
	}
	
	
	@Override
	public UtenteDTO login_utente(String username, String password) throws ClassNotFoundException, SQLException {
		
		Connection conn = connessione_db();
		
	 	String query = "SELECT * FROM `utenti` WHERE username = ? AND password LIKE SHA2(?, 512)";       
        PreparedStatement statement= conn.prepareStatement(query);
        
        statement.setString(1, username);
        statement.setString(2, password);
        
        ResultSet resultSet = statement.executeQuery();
        
        //guarda se ci sono risultati
        if(resultSet.next()) {
        	if(resultSet.getBoolean("is_admin")) {
        		UtenteDTO output = new GestoreDelSistemaDTO(resultSet.getString("username"),resultSet.getString("email"), resultSet.getString("nome"), 
        								resultSet.getString("cognome"),resultSet.getBoolean("is_maschio"), resultSet.getDate("data_nascita"), 
        								resultSet.getString("comune_nascita"), resultSet.getString("provincia_nascita"), resultSet.getString("codice_fiscale"));
        		chiudi_connessione(conn);
        		return output;
        	}
        	else {
        		UtenteDTO output = new ElettoreDTO(resultSet.getString("username"),resultSet.getString("email"), resultSet.getString("nome"), 
        								resultSet.getString("cognome"), resultSet.getBoolean("is_maschio"), resultSet.getDate("data_nascita"), 
        								resultSet.getString("comune_nascita"), resultSet.getString("provincia_nascita"), resultSet.getString("codice_fiscale"));
        		chiudi_connessione(conn);
        		return output;
        	}
        }else {
        	chiudi_connessione(conn);
        	return null;
        }
		
	}
	
	
	
	public boolean modifica_password(UtenteDTO utente, String password, String nuova_password) throws SQLException, ClassNotFoundException {
		Connection conn = connessione_db();
		
		String query ="UPDATE utenti SET password = SHA2(?, 512) WHERE username = '"+utente.username+"' AND password = SHA2(?, 512)";       
        PreparedStatement statement= conn.prepareStatement(query);
        
        statement.setString(1, nuova_password);
        statement.setString(2, password);
        
        int result = statement.executeUpdate();
		chiudi_connessione(conn);
		
        return result>0;
       
	}
	
	public boolean reset_password(String username, String nuova_password) throws SQLException, ClassNotFoundException {
		Connection conn = connessione_db();
		
		String query ="UPDATE utenti SET password = SHA2(?, 512) WHERE username = ?";       
        PreparedStatement statement= conn.prepareStatement(query);
        
        statement.setString(1, nuova_password);
        statement.setString(2, username);
        
        int result = statement.executeUpdate();
		chiudi_connessione(conn);
		
		
        return result>0;
       
	}
	
	public String get_email(String username) throws SQLException, ClassNotFoundException {
		Connection conn = connessione_db();
		
		String query ="SELECT email FROM `utenti` WHERE `username` = ?";       
        PreparedStatement statement= conn.prepareStatement(query);
        
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        
        if(resultSet.next()) {
        
    		String output = resultSet.getString("email");
    		chiudi_connessione(conn);
    		return output;        	
        	
        }
        
		chiudi_connessione(conn);
        return null;
       
	}

	@Override
	public boolean check_email_gia_usata(String email) throws SQLException, ClassNotFoundException {
		Connection conn = connessione_db();
		
	 	String query = "SELECT username FROM utenti WHERE email = ?";       
        PreparedStatement statement= conn.prepareStatement(query);
        
        statement.setString(1, email);
        
        ResultSet resultSet = statement.executeQuery();
        
        boolean result = resultSet.next();
		chiudi_connessione(conn);
		return result;
	}
	
	@Override
	public boolean check_username_gia_usato(String username) throws SQLException, ClassNotFoundException {
		Connection conn = connessione_db();
		
	 	String query = "SELECT username FROM utenti WHERE username = ?";       
        PreparedStatement statement= conn.prepareStatement(query);
        
        statement.setString(1, username);
        
        ResultSet resultSet = statement.executeQuery();
        
        //guarda se ci sono risultati
        boolean result = resultSet.next();
		chiudi_connessione(conn);
		return result;
	}
	
	@Override
	public boolean check_codice_fiscale_gia_usato(String codice_fiscale) throws SQLException, ClassNotFoundException {
		Connection conn = connessione_db();
		
	 	String query = "SELECT username FROM utenti WHERE codice_fiscale = ?";       
        PreparedStatement statement= conn.prepareStatement(query);
        
        statement.setString(1, codice_fiscale);
        
        ResultSet resultSet = statement.executeQuery();
        
        //guarda se ci sono risultati
        boolean result = resultSet.next();
		chiudi_connessione(conn);
		return result;
	}
	

}
