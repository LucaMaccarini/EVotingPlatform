package progetto_java_e_voting_plat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class LogDTO {
	public final String soggetto;
	public final String messaggio;
	public final Timestamp data_e_ora;
	
	public LogDTO(String soggetto, String messaggio, Timestamp data_e_ora) {
		this.soggetto=soggetto;
		this.messaggio=messaggio;
		this.data_e_ora=data_e_ora;
	}
	
	
	public String get_formatted_data_string() {
		LocalDateTime data_termine = data_e_ora.toLocalDateTime();
		int giorno = data_termine.getDayOfMonth();
		int anno = data_termine.getYear();
		int mese = data_termine.getMonthValue();
		int ore = data_termine.getHour();
		int minuti = data_termine.getMinute();
		return giorno + "/" + mese + "/" + anno + " alle ore " + ore + ":" + minuti;
	}
	
	@Override
	public String toString() {
		return "REGISTRATO DA: "+ soggetto + " - DATA REGISTRAZIONE: " + get_formatted_data_string() + " - MESSAGGIO DEL LOG: " + messaggio;
	}
}
