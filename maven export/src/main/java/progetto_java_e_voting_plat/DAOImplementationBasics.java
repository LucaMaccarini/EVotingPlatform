package progetto_java_e_voting_plat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public abstract class DAOImplementationBasics {
	protected Connection connessione_db() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		try {
			return DriverManager.getConnection("jdbc:mysql://localhost:3306/e_voting_platform?characterEncoding=utf8&allowMultiQueries=true&user=root&password=");
		}catch(Exception e){
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Connessione al database");
			a.setHeaderText("Errore Di connessione al database");
			a.setContentText("controlla che il database mysql sia acceso");
			a.show();
			return null;
		}
		
	}
	
	protected void chiudi_connessione(Connection conn) throws SQLException {
		conn.close();
	}
	
}
