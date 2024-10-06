package progetto_java_e_voting_plat;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;



class ElettoreDAOImlementationTest {
	//nota le sessioni di voto al quale il test si riferisce termineranno il 18/03/2032 alle 11:11
	
	UtenteDAO utente_DAO;
	ElettoreDAO elettore_DAO;
	SessioneDiVotoDAO sessione_DAO;
	public ElettoreDAOImlementationTest() {
		utente_DAO = UtenteDAOImplementation.get_singleton_instance();
		elettore_DAO = ElettoreDAOImplementation.get_singleton_instance();
		sessione_DAO = SessioneDiVotoDAOImplementation.get_singleton_instance();
	}

	@Test
	void vota_referendumTest_elettore_vota_si_e_successivamente_rivota() throws ClassNotFoundException, SQLException, LogException {
		ElettoreDTO elettore = (ElettoreDTO)utente_DAO.login_utente("alessio_verga", "LaMiaPassword1#");
		ReferendumDTO referendum = new ReferendumDTO(1, "test referendum 1", "quesito 1", "luca_maccarini", modalita_calcolo_vincitore_referendum.referendum_senza_quorum, Timestamp.valueOf("2032-03-18 11:11:00.000"));
		assertTrue(elettore_DAO.vota_referendum(elettore, referendum, true));
		assertFalse(elettore_DAO.vota_referendum(elettore, referendum, true));
		
	}
	
	@Test
	void vota_referendumTest_elettore_vota_no() throws ClassNotFoundException, SQLException, LogException {
		ElettoreDTO elettore = (ElettoreDTO)utente_DAO.login_utente("alessia_bertoli", "LaMiaPassword1#");
		ReferendumDTO referendum = new ReferendumDTO(1, "test referendum 1", "quesito 1", "luca_maccarini", modalita_calcolo_vincitore_referendum.referendum_senza_quorum, Timestamp.valueOf("2032-03-18 11:11:00.000"));
		assertTrue(elettore_DAO.vota_referendum(elettore, referendum, false));
		
	}
	
	@Test
	void voto_ordinale_sessione_di_votazioneTest_elettore_vota_e_successivamente_rivota() throws ClassNotFoundException, SQLException, LogException {
		ElettoreDTO elettore = (ElettoreDTO)utente_DAO.login_utente("alessia_bertoli", "LaMiaPassword1#");
		SessioneDiVotazioneDTO sessione = new SessioneDiVotazioneDTO(1, "test votazione ordinale", "descrizione voto ordinale", "luca_maccarini", modalita_voto.voto_ordinale,modalita_calcolo_vincitore_sessione_di_votazione.maggioranza, Timestamp.valueOf("2032-03-18 11:11:00.000"));
		List<PartitoDTO> partiti = new ArrayList<PartitoDTO>();
		partiti.add(new PartitoDTO("test partito 1", "descrizione 1"));
		partiti.add(new PartitoDTO("test partito 2", "descrizione 2"));
		partiti.add(new PartitoDTO("test partito 3", "descrizione 3"));
		assertTrue(elettore_DAO.voto_ordinale_sessione_di_votazione(elettore, sessione, partiti));
		assertFalse(elettore_DAO.voto_ordinale_sessione_di_votazione(elettore, sessione, partiti));
		
	}
	
	@Test
	void voto_ordinale_sessione_di_votazioneTest_un_altro_elettore_vota() throws ClassNotFoundException, SQLException, LogException {
		ElettoreDTO elettore = (ElettoreDTO)utente_DAO.login_utente("alessio_verga", "LaMiaPassword1#");
		SessioneDiVotazioneDTO sessione = new SessioneDiVotazioneDTO(1, "test votazione ordinale", "descrizione voto ordinale", "luca_maccarini", modalita_voto.voto_ordinale,modalita_calcolo_vincitore_sessione_di_votazione.maggioranza, Timestamp.valueOf("2032-03-18 11:11:00.000"));
		List<PartitoDTO> partiti = new ArrayList<PartitoDTO>();
		partiti.add(new PartitoDTO("test partito 1", "descrizione 1"));
		partiti.add(new PartitoDTO("test partito 2", "descrizione 2"));
		partiti.add(new PartitoDTO("test partito 3", "descrizione 3"));
		assertTrue(elettore_DAO.voto_ordinale_sessione_di_votazione(elettore, sessione, partiti));
		
	}
	
	@Test
	void voto_categorico_con_referenze_sessione_di_votazioneTest_elettore_vota_e_successivamente_rivota() throws ClassNotFoundException, SQLException, LogException {
		ElettoreDTO elettore = (ElettoreDTO)utente_DAO.login_utente("alessia_bertoli", "LaMiaPassword1#");
		SessioneDiVotazioneDTO sessione = new SessioneDiVotazioneDTO(2, "test categorica con preferenza", "descrizione voto categorico con preferenza", "luca_maccarini", modalita_voto.voto_categorico_con_preferenze,modalita_calcolo_vincitore_sessione_di_votazione.maggioranza, Timestamp.valueOf("2032-03-18 11:11:00.000"));
		PartitoDTO partito = new PartitoDTO("test partito 1", "descrizione 1");
		PartitoDAO partito_DAO = PartitoDAOImplementation.get_singleton_instance();
		partito_DAO.scarica_e_aggiungi_persone_del_partito(partito);
		assertTrue(elettore_DAO.voto_categorico_con_preferenze_sessione_di_votazione(elettore, sessione, partito, partito.get_persone()));
		assertFalse(elettore_DAO.voto_categorico_con_preferenze_sessione_di_votazione(elettore, sessione, partito, partito.get_persone()));
	}
	
	@Test
	void voto_categorico_con_preferenze_sessione_di_votazioneTest_un_altro_elettore_vota() throws ClassNotFoundException, SQLException, LogException {
		ElettoreDTO elettore = (ElettoreDTO)utente_DAO.login_utente("alessio_verga", "LaMiaPassword1#");
		SessioneDiVotazioneDTO sessione = new SessioneDiVotazioneDTO(2, "test categorica con preferenza", "descrizione voto categorico con preferenza", "luca_maccarini", modalita_voto.voto_categorico_con_preferenze,modalita_calcolo_vincitore_sessione_di_votazione.maggioranza, Timestamp.valueOf("2032-03-18 11:11:00.000"));
		PartitoDTO partito = new PartitoDTO("test partito 1", "descrizione 1");
		PartitoDAO partito_DAO = PartitoDAOImplementation.get_singleton_instance();
		partito_DAO.scarica_e_aggiungi_persone_del_partito(partito);
		assertTrue(elettore_DAO.voto_categorico_con_preferenze_sessione_di_votazione(elettore, sessione, partito, partito.get_persone()));	
		
	}
	
	@AfterAll
	static void pulisci_voti() throws SQLException, ClassNotFoundException {
		Connection conn= null;
		Class.forName("com.mysql.jdbc.Driver");
		try {
			conn =  DriverManager.getConnection("jdbc:mysql://localhost:3306/e_voting_platform?characterEncoding=utf8&allowMultiQueries=true&user=root&password=");
		}catch(Exception e){
			fail("non ho potuto resettare i voti");
		}
		
		Statement s = conn.createStatement();
		String query=	"DELETE FROM `voti_preferenze_sessione_di_voto` WHERE `voti_preferenze_sessione_di_voto`.`id_sessione_di_votazione` = 2; "+
						"DELETE FROM `voti_sessione_di_voto` WHERE `voti_sessione_di_voto`.`id_sessione_voto` = 1 OR `voti_sessione_di_voto`.`id_sessione_voto` = 2; " +
						"DELETE FROM `utente_votato_sessione_di_votazione` WHERE `utente_votato_sessione_di_votazione`.`id_sessione_di_voto` = 1 OR `utente_votato_sessione_di_votazione`.`id_sessione_di_voto` = 2; " + 
						"DELETE FROM `utente_votato_referendum` WHERE `utente_votato_referendum`.`id_referendum` = 1;" + 
						"UPDATE `sessioni_referendum` SET `n_si` = '0', `n_no` = '0' WHERE `sessioni_referendum`.`id` = 1;";
		
		s.execute(query);
		
		
	}
	

}
