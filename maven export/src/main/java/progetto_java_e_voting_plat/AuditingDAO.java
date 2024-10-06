package progetto_java_e_voting_plat;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface AuditingDAO {
	public void report_log(String soggetto, Timestamp data_e_ora, String messaggio) throws SQLException, ClassNotFoundException, LogException;
	public List<LogDTO> get_logs() throws SQLException, ClassNotFoundException;
	public void delete_all_logs() throws SQLException, ClassNotFoundException;
}
