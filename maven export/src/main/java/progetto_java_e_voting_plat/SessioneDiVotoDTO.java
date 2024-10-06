package progetto_java_e_voting_plat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class SessioneDiVotoDTO {
	public final int id;
	public final String nome;
	public final String proprietario;
	public final Timestamp data_e_ora_termine;
	
	public SessioneDiVotoDTO(int id, String nome, String proprietario, Timestamp data_e_ora_termine) {
		this.id = id;
		this.nome = nome;
		this.proprietario = proprietario;
		this.data_e_ora_termine=data_e_ora_termine;
	}
	
	
	public String get_formatted_data_string() {
		LocalDateTime data_termine = data_e_ora_termine.toLocalDateTime();
		int giorno = data_termine.getDayOfMonth();
		int anno = data_termine.getYear();
		int mese = data_termine.getMonthValue();
		int ore = data_termine.getHour();
		int minuti = data_termine.getMinute();
		return giorno + "/" + mese + "/" + anno + " alle ore " + ore + ":" + minuti;
	}
	
	public abstract boolean ha_un_vincitore();
	
	@Override
	public String toString() {
		
		//int secondi = data_termine.getSecond();
		return nome + " | Creata da: " + proprietario + " | Data termine: " + get_formatted_data_string();
	}
	
}
