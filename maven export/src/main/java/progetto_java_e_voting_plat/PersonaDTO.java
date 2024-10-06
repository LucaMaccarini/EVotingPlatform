package progetto_java_e_voting_plat;

public class PersonaDTO {
	public final int id;
	public final String nome;
	public final String cognome;
	
	public PersonaDTO(int id, String nome, String cognome) {
		this.id=id;
		this.nome=nome;
		this.cognome=cognome;
	}
	
	@Override
	public String toString() {
		return nome + ", " + cognome;
	}
}
