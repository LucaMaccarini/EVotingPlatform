package progetto_java_e_voting_plat;

import java.sql.SQLException;
import java.sql.Timestamp;

public interface AuditingListener {
	public void evento_generato_da_DAO(UtenteDTO soggetto, Timestamp data_e_ora, String messaggio_evento) throws ClassNotFoundException, SQLException, LogException;
	public void evento_generato_da_DAO(String soggetto, Timestamp data_e_ora, String messaggio_evento) throws ClassNotFoundException, SQLException, LogException;
}
