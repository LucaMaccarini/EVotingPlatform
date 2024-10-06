package progetto_java_e_voting_plat;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class UtenteDAOImlementationTest {

	@Test
	void login_utenteTest_login_fail() throws ClassNotFoundException, SQLException {
		UtenteDAO utente_DAO = UtenteDAOImplementation.get_singleton_instance();
		UtenteDTO result = utente_DAO.login_utente("username_insesistente", "LaMiaPassword1#");
		assertNull(result);
	}
	
	@Test
	void login_utenteTest_login_success_elettore() throws ClassNotFoundException, SQLException {
		UtenteDAO utente_DAO = UtenteDAOImplementation.get_singleton_instance();
		UtenteDTO result = utente_DAO.login_utente("alessio_verga", "LaMiaPassword1#");
		assertNotNull(result);
	}
	
	@Test
	void login_utenteTest_login_success_gestore_del_sistema() throws ClassNotFoundException, SQLException {
		UtenteDAO utente_DAO = UtenteDAOImplementation.get_singleton_instance();
		UtenteDTO result = utente_DAO.login_utente("luca_maccarini", "LaMiaPassword1#");
		assertNotNull(result);
	}

}
