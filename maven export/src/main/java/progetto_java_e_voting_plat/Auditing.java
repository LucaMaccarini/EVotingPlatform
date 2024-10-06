package progetto_java_e_voting_plat;


import java.sql.SQLException;
import java.sql.Timestamp;


public class Auditing implements AuditingListener {
	
	private static Auditing singleton_instance;
	private AuditingDAO auditing_DAO;
	
	
	public static Auditing get_singleton_instance() {
		if(singleton_instance == null) {
			singleton_instance = new Auditing();
		}
		return singleton_instance;
	}
	
	public Auditing() {
		auditing_DAO = AuditingDAOImplementation.get_singleton_instance();
	}



	@Override
	public void evento_generato_da_DAO(UtenteDTO soggetto, Timestamp data_e_ora, String messaggio_evento) throws ClassNotFoundException, SQLException, LogException {
		auditing_DAO.report_log(soggetto.username, data_e_ora, messaggio_evento);
		
	}


	@Override
	public void evento_generato_da_DAO(String soggetto, Timestamp data_e_ora, String messaggio_evento) throws ClassNotFoundException, SQLException, LogException {
		auditing_DAO.report_log(soggetto, data_e_ora, messaggio_evento);
	}

}
