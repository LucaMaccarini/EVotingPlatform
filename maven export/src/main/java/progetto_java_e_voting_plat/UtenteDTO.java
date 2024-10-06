package progetto_java_e_voting_plat;

import java.sql.Date;

public abstract class UtenteDTO {
	public final String username;
	public final String email;
	public final String nome;
	public final String cognome;
	public final boolean is_maschio;
	public final Date data_nascita;
	public final String comune_nascita;
	public final String provincia_nascita;
	public final String codice_fiscale;
	public final boolean is_admin;
	
	
	public UtenteDTO (String username, String email, String nome, String cognome, boolean is_maschio, Date data_nascita, String comune_nascita, String provincia_nascita, String codice_fiscale, boolean is_admin) {
		this.username = username;
		this.email=email;
		this.nome = nome;
		this.cognome = cognome;
		this.is_maschio = is_maschio;
		this.data_nascita = data_nascita;
		this.comune_nascita = comune_nascita;	
		this.provincia_nascita = provincia_nascita;	
		this.codice_fiscale = codice_fiscale;
		this.is_admin = is_admin;		
	}
	
	@Override
	public String toString() {
		return username + " | " + nome + ", " +cognome;
	}

	
}

