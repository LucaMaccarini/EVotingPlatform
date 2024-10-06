package progetto_java_e_voting_plat;

import java.sql.SQLException;
import java.util.List;

public interface ElettoreDAO {
	public boolean check_elettore_ha_gia_votato(String username, SessioneDiVotoDTO sessione) throws SQLException, ClassNotFoundException;
	public boolean vota_referendum(ElettoreDTO elettore, ReferendumDTO referendum, boolean scelta) throws ClassNotFoundException, SQLException, LogException;
	public boolean voto_categorico_sessione_di_votazione(ElettoreDTO elettore, SessioneDiVotazioneDTO sessione, PartitoDTO partito) throws ClassNotFoundException, SQLException, LogException;
	public boolean voto_ordinale_sessione_di_votazione(ElettoreDTO elettore, SessioneDiVotazioneDTO sessione, List <PartitoDTO> partito) throws ClassNotFoundException, SQLException, LogException;
	public boolean voto_categorico_con_preferenze_sessione_di_votazione(ElettoreDTO elettore, SessioneDiVotazioneDTO sessione, PartitoDTO partito, List<PersonaDTO>ordine_persone) throws ClassNotFoundException, SQLException, LogException;
	public void segna_che_elettore_ha_votato(String username, SessioneDiVotoDTO sessione) throws ClassNotFoundException, SQLException;
	public boolean check_elettore_ha_diritto_di_voto(String username, SessioneDiVotoDTO sessione) throws ClassNotFoundException, SQLException;
	public List<ElettoreDTO> get_elettori() throws ClassNotFoundException, SQLException;
	public boolean delete_account(String username) throws SQLException, ClassNotFoundException, LogException;
}
