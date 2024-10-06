package progetto_java_e_voting_plat;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public class GestoreDelSistemaDAOImplementation extends DAOImplementationBasics implements GestoreDelSistemaDAO {

	private static GestoreDelSistemaDAOImplementation singleton_instance;
	private AuditingListener auditing;
	
	public static GestoreDelSistemaDAOImplementation get_singleton_instance() {
		if(singleton_instance == null)
			singleton_instance = new GestoreDelSistemaDAOImplementation();
		return singleton_instance;
	}
	
	public GestoreDelSistemaDAOImplementation() {
		auditing = Auditing.get_singleton_instance();
	}
	
	
	@Override
	public boolean registra_nuovo_elettore(GestoreDelSistemaDTO gestore, String username, String email, String nome, String cognome, boolean is_maschio, LocalDate data_nascita, String comune_nascita, String provincia_nascita, String codice_fiscale, String password) throws ClassNotFoundException, SQLException, LogException {
		Connection conn = connessione_db();
		
	 	String query = "INSERT INTO `utenti` (`username`, `email`, `nome`, `cognome`, `is_maschio`, `data_nascita`, `comune_nascita`, `provincia_nascita`, `codice_fiscale`, `password`, `is_admin`) VALUES (?, ?, ?, ?, ?, ?, ?, UPPER(?), UPPER(?), SHA2(?, 512), '0')";       
        PreparedStatement statement= conn.prepareStatement(query);
        
        statement.setString(1, username);
        statement.setString(2, email);
        statement.setString(3, nome);
        statement.setString(4, cognome);
        statement.setBoolean(5, is_maschio);
        statement.setDate(6, Date.valueOf(data_nascita));
        statement.setString(7, comune_nascita);
        statement.setString(8, provincia_nascita);
        statement.setString(9, codice_fiscale);
        statement.setString(10, password);
        
        statement.executeUpdate();
        chiudi_connessione(conn);
        
    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	auditing.evento_generato_da_DAO(gestore, now, "ha registrato un nuovo elettore con username " + username);
        
        return true;
		
	}

	@Override
	public boolean registra_voto_referendum(GestoreDelSistemaDTO gestore, ReferendumDTO referendum, boolean scelta)	throws ClassNotFoundException, SQLException, LogException {
			
		Connection conn = connessione_db();
		
	 	String query = "UPDATE `sessioni_referendum` SET ";
	 	if(scelta)
	 		query += " n_si = n_si + 1 WHERE sessioni_referendum.id = " + referendum.id;
	 	else
	 		query += " n_no = n_no + 1 WHERE sessioni_referendum.id = " + referendum.id;
	 	
	 	
	 	Statement statement = conn.createStatement();
        
	 	int result = statement.executeUpdate(query);
	 	chiudi_connessione(conn);
	 	 if(result > 0) {
	 		Timestamp now = new Timestamp(System.currentTimeMillis());
	        auditing.evento_generato_da_DAO(gestore, now, "ha registrato un voto nel referendum (id: "+referendum.id+") " + referendum);
	 	 }
	 	return result>0;
	}

	@Override
	public boolean registra_voto_categorico_sessione_di_votazione(GestoreDelSistemaDTO gestore, SessioneDiVotazioneDTO sessione, PartitoDTO partito) throws ClassNotFoundException, SQLException, LogException {

		
		Connection conn = connessione_db();
		String query="";
		if(check_presente_voto(sessione, partito))
			query = "UPDATE `voti_sessione_di_voto` SET n_voti = n_voti+1 WHERE `voti_sessione_di_voto`.`id_sessione_voto` =" +sessione.id+" AND `voti_sessione_di_voto`.`partito` ='" + partito.nome +"'";
		else			 	
			query = "INSERT INTO `voti_sessione_di_voto` (`id_sessione_voto`, `partito`, `n_voti`) VALUES ("+sessione.id+", '"+partito.nome+"', 1)";
	 	Statement statement = conn.createStatement();
        
	 	int result = statement.executeUpdate(query);
	 	chiudi_connessione(conn);
	 	
	 	if(result > 0) {
	 		Timestamp now = new Timestamp(System.currentTimeMillis());
	        auditing.evento_generato_da_DAO(gestore, now, " ha registrato un voto nella sessione di voto (id: "+sessione.id+") " + sessione);
	 	}
	 	
	 	return result>0;	
	}
	
	private boolean check_presente_voto(SessioneDiVotazioneDTO sessione, PartitoDTO partito) throws ClassNotFoundException, SQLException {
		
		Connection conn = connessione_db();
		
	 	String query = "SELECT id_sessione_voto FROM `voti_sessione_di_voto` WHERE `id_sessione_voto` = " + sessione.id + " AND `partito` LIKE '"+ partito.nome +"'";
	 				 	
	 	Statement statement = conn.createStatement();
        
	 	ResultSet result = statement.executeQuery(query);
	 	boolean output = result.next();
	 	chiudi_connessione(conn);
	 	return output;	
	}
	
	private boolean check_presente_voto_preferenza(SessioneDiVotazioneDTO sessione, PersonaDTO persona) throws ClassNotFoundException, SQLException {
		Connection conn = connessione_db();
		
	 	String query = "SELECT id_sessione_di_votazione FROM `voti_preferenze_sessione_di_voto` WHERE `id_sessione_di_votazione` = " + sessione.id + " AND `id_persona` = "+ persona.id +"";
	 				 	
	 	Statement statement = conn.createStatement();
        
	 	ResultSet result = statement.executeQuery(query);
	 	boolean output = result.next();
	 	chiudi_connessione(conn);
	 	return output;	
	}

	@Override
	public boolean registra_voto_ordinale_sessione_di_votazione(GestoreDelSistemaDTO gestore, SessioneDiVotazioneDTO sessione, List<PartitoDTO> partito) throws ClassNotFoundException, SQLException, LogException {
				
				
				Connection conn = connessione_db();
				String query = "";
				for(int i=0; i<partito.size(); i++) {
					if(check_presente_voto(sessione, partito.get(i)))
						query += "UPDATE `voti_sessione_di_voto` SET n_voti = n_voti+ "+ i +" WHERE `id_sessione_voto` =" +sessione.id+" AND `partito` ='" + partito.get(i).nome +"';\n";
					else			 	
						query += "INSERT INTO `voti_sessione_di_voto` (`id_sessione_voto`, `partito`, `n_voti`) VALUES ("+sessione.id+", '"+partito.get(i).nome+"', "+i+");\n";
				}
				
				
			 	Statement statement = conn.createStatement();
		        
			 	int result = statement.executeUpdate(query);
			 	chiudi_connessione(conn);
			 	
			 	if(result > 0) {
			 		Timestamp now = new Timestamp(System.currentTimeMillis());
			        auditing.evento_generato_da_DAO(gestore, now, " ha registrato un voto nella sessione di voto (id: "+sessione.id+") " + sessione);
			 	}
			 	
			 	
			 	return result>0;	
	}

	@Override
	public boolean registra_voto_categorico_con_preferenze_sessione_di_votazione(GestoreDelSistemaDTO gestore, SessioneDiVotazioneDTO sessione, PartitoDTO partito, List<PersonaDTO> ordine_persone) throws ClassNotFoundException, SQLException, LogException {
		
		
		if(registra_voto_categorico_sessione_di_votazione(gestore, sessione, partito)) {
		
			Connection conn = connessione_db();
			String query = "";
			
			for(int i=0; i<ordine_persone.size(); i++) {
				if(check_presente_voto_preferenza(sessione, ordine_persone.get(i)))
					query += "UPDATE `voti_preferenze_sessione_di_voto` SET n_voti = n_voti+ "+ i +" WHERE `id_sessione_di_votazione` =" +sessione.id+" AND `id_persona` = " + ordine_persone.get(i).id +";\n";
				else			 	
					query += "INSERT INTO `voti_preferenze_sessione_di_voto` (`id_sessione_di_votazione`, `id_persona`, `n_voti`) VALUES ("+sessione.id+", "+ordine_persone.get(i).id+", "+i+");\n";
			}
			
		 	Statement statement = conn.createStatement();
	        
		 	int result = statement.executeUpdate(query);
		 	chiudi_connessione(conn);
		 	
		 	return result>0;	
		}else {
			return false;
		}
	}
	
	public boolean registra_astenuto(SessioneDiVotoDTO sessione) throws ClassNotFoundException, SQLException {
		
		if(sessione instanceof SessioneDiVotazioneDTO) {
			Connection conn = connessione_db();
			
		 	String query = "SELECT id_sessione_di_votazione FROM astenuti_votazione_maggioranza_assoluta WHERE `id_sessione_di_votazione` = " + sessione.id;
		 				 	
		 	Statement statement = conn.createStatement();
	        
		 	ResultSet result = statement.executeQuery(query);
		 	
		 	if(result.next()) {
		 		query ="UPDATE `astenuti_votazione_maggioranza_assoluta` SET n_astenuti = n_astenuti + 1 WHERE `id_sessione_di_votazione` = " + sessione.id;
		 	}else {
		 		query ="INSERT INTO `astenuti_votazione_maggioranza_assoluta` (`id_sessione_di_votazione`, `n_astenuti`) VALUES ("+sessione.id+", 1)";
		 	}
		 
		 	int output= statement.executeUpdate(query);
		 	chiudi_connessione(conn);
		 	return output>0;
		}else {
			Connection conn = connessione_db();
			
		 	String query = "SELECT id_referendum FROM astenuti_referendum_con_quorum WHERE `id_referendum` = " + sessione.id;
		 				 	
		 	Statement statement = conn.createStatement();
	        
		 	ResultSet result = statement.executeQuery(query);
		 	
		 	if(result.next()) {
		 		query ="UPDATE `astenuti_referendum_con_quorum` SET n_astenuti = n_astenuti + 1 WHERE id_referendum = " + sessione.id;
		 	}else {
		 		query ="INSERT INTO `astenuti_referendum_con_quorum` (`id_referendum`, `n_astenuti`) VALUES ("+sessione.id+", 1)";
		 	}
		 	
		 	int output= statement.executeUpdate(query);
		 	chiudi_connessione(conn);
		 	return output>0;
		}
	 	
	}
	

}
