package progetto_java_e_voting_plat;

import java.sql.Timestamp;
import java.util.List;

public class SessioneDiVotazioneDTO extends SessioneDiVotoDTO {
	public final String descrizione;
	public final modalita_voto modalita_voto;
	public final modalita_calcolo_vincitore_sessione_di_votazione modalita_vincitore;
	private List<PartitoDTO> candidati;
	private PartitoDTO vincitore;
	
	public SessioneDiVotazioneDTO(int id, String nome, String descrizione, String proprietario, modalita_voto modalita_voto, modalita_calcolo_vincitore_sessione_di_votazione modalita_vincitore, Timestamp data_e_ora_termine) {
		super(id, nome, proprietario, data_e_ora_termine);
		this.descrizione=descrizione;
		this.modalita_voto = modalita_voto;
		this.modalita_vincitore=modalita_vincitore;
		candidati=null;
		this.vincitore=null;
	}
	
	public SessioneDiVotazioneDTO(int id, String nome, String descrizione, String proprietario, modalita_voto modalita_voto, modalita_calcolo_vincitore_sessione_di_votazione modalita_vincitore, Timestamp data_e_ora_termine, PartitoDTO vincitore) {
		super(id, nome, proprietario, data_e_ora_termine);
		this.descrizione=descrizione;
		this.modalita_voto = modalita_voto;
		this.modalita_vincitore=modalita_vincitore;
		candidati=null;
		this.vincitore=vincitore;
	}
	
	public void set_partiti_candidati(List<PartitoDTO> candidati) {
		this.candidati=candidati;
	}
	
	public List<PartitoDTO> get_partiti_candidati() {
		return candidati;
	}
	
	public void set_vincitore(PartitoDTO vincitore) {
		this.vincitore=vincitore;
	}
	
	public PartitoDTO get_vincitore() {
		return vincitore;
	}

	@Override
	public boolean ha_un_vincitore() {
		return vincitore!=null;
	}
	
	
	

}
