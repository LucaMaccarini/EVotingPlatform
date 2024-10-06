package progetto_java_e_voting_plat;

import java.sql.Timestamp;

public class ReferendumDTO extends SessioneDiVotoDTO {
	
	public final String quesito;
	public final modalita_calcolo_vincitore_referendum modalita_vincitore;
	private String vincitore;
	
	public ReferendumDTO(int id, String nome, String quesito, String proprietario, modalita_calcolo_vincitore_referendum modalita_vincitore, Timestamp data_e_ora_termine) {
		super(id, nome, proprietario, data_e_ora_termine);
		this.quesito=quesito;
		this.modalita_vincitore=modalita_vincitore;		
	}
	
	public ReferendumDTO(int id, String nome, String quesito, String proprietario, modalita_calcolo_vincitore_referendum modalita_vincitore, Timestamp data_e_ora_termine, String vincitore) {
		super(id, nome, proprietario, data_e_ora_termine);
		this.quesito=quesito;
		this.modalita_vincitore=modalita_vincitore;	
		this.vincitore = vincitore;
	}
	
	public void set_vincitore(String vincitore) {
		this.vincitore=vincitore;
	}
	
	public String get_vincitore() {
		return vincitore;
	}

	@Override
	public boolean ha_un_vincitore() {
		return vincitore!=null;
	}

}
