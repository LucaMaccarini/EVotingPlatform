package progetto_java_e_voting_plat;

import java.util.List;

public class PartitoDTO {
	public final String nome;
	public final String descrizione;
	private List<PersonaDTO> persone;
	
	public PartitoDTO(String nome, String descrizione) {
		this.nome=nome;
		this.descrizione = descrizione;
		persone = null;
	}
	
	
	public void set_persone(List<PersonaDTO> persone) {
		this.persone=persone;
	}
	
	public List<PersonaDTO> get_persone() {
		return persone;
	}
	
	@Override
	public String toString() {
		if(descrizione.length()>0)
			return nome + " | " + descrizione;
		else
			return nome;
	}
}
