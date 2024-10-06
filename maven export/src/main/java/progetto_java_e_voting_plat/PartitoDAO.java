package progetto_java_e_voting_plat;

import java.sql.SQLException;
import java.util.List;

public interface PartitoDAO {
	public void scarica_e_aggiungi_persone_del_partito(PartitoDTO partito) throws SQLException, ClassNotFoundException;
	public List<PartitoDTO> get_partiti() throws SQLException, ClassNotFoundException;
	public boolean aggiungi_partito(GestoreDelSistemaDTO gestore, String nome, String descrizione) throws SQLException, ClassNotFoundException, LogException;
	public boolean aggiungi_persona_ad_un_partito(GestoreDelSistemaDTO gestore, String nome, String cognome, PartitoDTO partito) throws SQLException, ClassNotFoundException, LogException;
	public PartitoDTO get_partito(String nome) throws SQLException, ClassNotFoundException;
	public List<PartitoDTO> get_classifica_partiti_candidati(SessioneDiVotazioneDTO sessione) throws ClassNotFoundException, SQLException;
}
