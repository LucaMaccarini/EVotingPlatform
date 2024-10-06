package progetto_java_e_voting_plat;

import java.sql.Date;

public class ElettoreDTO extends UtenteDTO {

	public ElettoreDTO(String username, String email, String nome, String cognome, boolean is_maschio, Date data_nascita, String comune_nascita,
			String provincia_nascita, String codice_fiscale) {
		super(username, email, nome, cognome, is_maschio, data_nascita, comune_nascita, provincia_nascita, codice_fiscale, false);
		
		
		
	}
	
}
