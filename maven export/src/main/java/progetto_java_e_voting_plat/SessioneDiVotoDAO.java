package progetto_java_e_voting_plat;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;


public interface SessioneDiVotoDAO {
	public List<SessioneDiVotoDTO> get_sessioni_di_voto_attive_con_diritto_di_voto(ElettoreDTO elettore) throws SQLException, ClassNotFoundException;
	public List<SessioneDiVotoDTO> get_sessioni_di_voto_con_vincitore_non_calcolato() throws SQLException, ClassNotFoundException;
	public List<SessioneDiVotoDTO> get_sessioni_di_voto_terminate_con_vincitore_calcolato() throws SQLException, ClassNotFoundException;
	public void scarica_e_aggiungi_partiti_candidati(SessioneDiVotazioneDTO sessione) throws SQLException, ClassNotFoundException;
	public void calcola_vincitore(GestoreDelSistemaDTO gestore, SessioneDiVotoDTO sessione) throws SQLException, ClassNotFoundException, LogException;
	public void registra_nuova_sessione_di_votazione(String nome, String descrizione, GestoreDelSistemaDTO proprietario, modalita_voto mod_voto, modalita_calcolo_vincitore_sessione_di_votazione mod_vincitore, Timestamp data_termine, List<ElettoreDTO> aventi_il_diritto, List<PartitoDTO> candidati) throws SQLException, ClassNotFoundException, LogException;
	public void registra_nuovo_referendum(String nome, String quesito, GestoreDelSistemaDTO proprietario, modalita_calcolo_vincitore_referendum mod_vincitore, Timestamp data_termine, List<ElettoreDTO> aventi_il_diritto) throws SQLException, ClassNotFoundException, LogException;
	public boolean check_vincitore_gia_calcolato(SessioneDiVotoDTO sessione) throws SQLException, ClassNotFoundException;
}
