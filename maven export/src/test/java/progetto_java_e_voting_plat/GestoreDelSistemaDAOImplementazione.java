package progetto_java_e_voting_plat;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

class GestoreDelSistemaDAOImplementazione {

	GestoreDelSistemaDAO gestore_DAO;
	UtenteDAO utente_DAO;
	
	public GestoreDelSistemaDAOImplementazione() {
		gestore_DAO = GestoreDelSistemaDAOImplementation.get_singleton_instance();
		utente_DAO = UtenteDAOImplementation.get_singleton_instance();
	}
	
	@Test
	void registra_nuovo_elettoreTest_e_registrazione_di_elettore_gia_esistente() throws ClassNotFoundException, SQLException, LogException {
		GestoreDelSistemaDTO gestore = (GestoreDelSistemaDTO)utente_DAO.login_utente("luca_maccarini", "LaMiaPassword1#");
		LocalDate data = LocalDate.of(2000, 11, 6);
		//password: LaMiaPassword1#
		String pass_hash = "7f844b4c6e0017ef0382e64aeab15c992e74dea7c2f71b6ab8bf62d082ca8e5d166c88eaddc2a42cf0c81370c0b6b5dab3a32e327ee2e1e163c10546bc2cab13";
		assertTrue(gestore_DAO.registra_nuovo_elettore(gestore, "test", "test@gmail.com", "test", "test", true, data, "test", "TS", "testxxxxxxxtest", pass_hash));
		
		boolean result=false;
		try {
			result = gestore_DAO.registra_nuovo_elettore(gestore, "test", "test@gmail.com", "test", "test", true, data, "test", "TS", "testxxxxxxxtest", pass_hash);
			assertFalse(result);
		}catch(Exception e) {
			assertFalse(result);
		}
	}
	
	@AfterAll
	public static void elimina_utente_di_test() throws SQLException, ClassNotFoundException {
		Connection conn= null;
		Class.forName("com.mysql.jdbc.Driver");
		try {
			conn =  DriverManager.getConnection("jdbc:mysql://localhost:3306/e_voting_platform?characterEncoding=utf8&allowMultiQueries=true&user=root&password=");
		}catch(Exception e){
			fail("non ho potuto eliminare l'account di test");
		}
		
		Statement s = conn.createStatement();
		String query=	"DELETE FROM `utenti` WHERE `utenti`.`username` = 'test';";
		s.execute(query);	
		
	}

}
