package progetto_java_e_voting_plat;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface GestoreDelSistemaDAO {
	public boolean registra_nuovo_elettore(GestoreDelSistemaDTO gestore, String username, String email, String nome, String cognome, boolean is_maschio, LocalDate data_nascita, String comune_nascita, String provincia_nascita, String codice_fiscale, String password) throws ClassNotFoundException, SQLException, LogException;
	public boolean registra_voto_referendum(GestoreDelSistemaDTO gestore, ReferendumDTO referendum, boolean scelta) throws ClassNotFoundException, SQLException, LogException;
	public boolean registra_voto_categorico_sessione_di_votazione(GestoreDelSistemaDTO gestore, SessioneDiVotazioneDTO sessione, PartitoDTO partito) throws ClassNotFoundException, SQLException, LogException;
	public boolean registra_voto_ordinale_sessione_di_votazione(GestoreDelSistemaDTO gestore, SessioneDiVotazioneDTO sessione, List <PartitoDTO> partito) throws ClassNotFoundException, SQLException, LogException;
	public boolean registra_voto_categorico_con_preferenze_sessione_di_votazione(GestoreDelSistemaDTO gestore, SessioneDiVotazioneDTO sessione, PartitoDTO partito, List<PersonaDTO>ordine_persone) throws ClassNotFoundException, SQLException, LogException;
	public boolean registra_astenuto(SessioneDiVotoDTO sessione) throws ClassNotFoundException, SQLException;
}
