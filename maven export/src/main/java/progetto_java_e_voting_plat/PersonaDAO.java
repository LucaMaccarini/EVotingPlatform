package progetto_java_e_voting_plat;

import java.sql.SQLException;
import java.util.List;

public interface PersonaDAO {
	List<PersonaDTO> get_classifica_persone_partito_vincitore(SessioneDiVotazioneDTO sessione) throws SQLException, ClassNotFoundException;
}
