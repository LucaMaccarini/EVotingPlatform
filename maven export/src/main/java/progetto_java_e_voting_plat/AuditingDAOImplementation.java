package progetto_java_e_voting_plat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AuditingDAOImplementation extends DAOImplementationBasics implements AuditingDAO{

private static AuditingDAOImplementation singleton_instance;
	
	
	public static AuditingDAOImplementation get_singleton_instance() {
		if(singleton_instance == null) {
			singleton_instance = new AuditingDAOImplementation();
		}
		return singleton_instance;
	}
	
	
	
	public void report_log(String soggetto, Timestamp data_e_ora, String messaggio) throws SQLException, ClassNotFoundException, LogException {
		
		
		Connection conn = connessione_db();
		
	 	String query = "INSERT INTO `logs` (`id`, `data_e_ora`, `soggetto`, `messaggio`) VALUES (NULL, '"+data_e_ora+"', '"+soggetto+"', '"+messaggio+"');";       
        Statement statement = conn.createStatement();
        int result = statement.executeUpdate(query);
        chiudi_connessione(conn);
        
        if(result==0)
        	throw new LogException("errore nella registrazione del log");
		
	}
	
	
	public List<LogDTO> get_logs() throws SQLException, ClassNotFoundException {
		List<LogDTO> result = new ArrayList<LogDTO>();
		Connection conn = connessione_db();
	 	String query = 	"SELECT data_e_ora, soggetto, messaggio FROM logs ORDER BY data_e_ora DESC";
	 	Statement statement = conn.createStatement();
	 	ResultSet resultSet = statement.executeQuery(query);
	 	
	 	if(resultSet.next()) {
        	do {
        		LogDTO log = new LogDTO(resultSet.getString("soggetto"), resultSet.getString("messaggio"), resultSet.getTimestamp("data_e_ora"));
        		result.add(log);
        	} while (resultSet.next());
        	chiudi_connessione(conn);
        }
	 	
		return result;
	}
	
	public void delete_all_logs() throws SQLException, ClassNotFoundException {
		

		Connection conn = connessione_db();
		
	 	String query = "TRUNCATE e_voting_platform.logs";     
        Statement statement = conn.createStatement();
        statement.executeUpdate(query);
        chiudi_connessione(conn);
	}
	
}
