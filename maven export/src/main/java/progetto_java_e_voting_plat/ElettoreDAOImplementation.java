package progetto_java_e_voting_plat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ElettoreDAOImplementation extends DAOImplementationBasics implements ElettoreDAO {

	private static ElettoreDAOImplementation singleton_instance;
	private AuditingListener auditing;
	public String ciao;
	
	public static ElettoreDAOImplementation get_singleton_instance() {
		if(singleton_instance == null) {
			singleton_instance = new ElettoreDAOImplementation();
		}
		return singleton_instance;
	}
	
	public ElettoreDAOImplementation() {
		auditing = Auditing.get_singleton_instance();
	}
	
	
	@Override
	//scelta == true ==> si altriemnti no
	public boolean vota_referendum(ElettoreDTO elettore, ReferendumDTO referendum, boolean scelta) throws ClassNotFoundException, SQLException, LogException {
		
		
		if(check_elettore_ha_gia_votato(elettore.username, referendum))
			return false;
		
		Connection conn = connessione_db();
		
	 	String query = "UPDATE `sessioni_referendum` SET ";
	 	if(scelta)
	 		query += " n_si = n_si + 1 WHERE sessioni_referendum.id = " + referendum.id;
	 	else
	 		query += " n_no = n_no + 1 WHERE sessioni_referendum.id = " + referendum.id;
	 	
	 	
	 	Statement statement = conn.createStatement();
        
	 	statement.executeUpdate(query);
	 	chiudi_connessione(conn);
	 	
	 	
 		segna_che_elettore_ha_votato(elettore.username, referendum);
 		Timestamp now = new Timestamp(System.currentTimeMillis());
 		auditing.evento_generato_da_DAO(elettore, now, "ha votato al referendum: (id: " + referendum.id + ") " + referendum );
 	
	 	
	 	return true;
		
	}
	
	public boolean delete_account(String username) throws SQLException, ClassNotFoundException, LogException {
		Connection conn = connessione_db();
		
	 	String query = "DELETE FROM `utenti` WHERE username = '"+ username +"' AND is_admin=0";       
        Statement statement= conn.createStatement();
        int result = statement.executeUpdate(query);
        chiudi_connessione(conn);
        
        if(result>0) {
        	Timestamp now = new Timestamp(System.currentTimeMillis());
        	auditing.evento_generato_da_DAO(username, now, "ha eliminato il proprio account");
        }
        return result>0;
	}
	
	
	public boolean check_elettore_ha_gia_votato(String username, SessioneDiVotoDTO sessione) throws SQLException, ClassNotFoundException {
		Connection conn = connessione_db();
		
		if(sessione instanceof SessioneDiVotazioneDTO) {
			String query = "SELECT username FROM `utente_votato_sessione_di_votazione` WHERE `id_sessione_di_voto` = "+ sessione.id +" AND `username` = '"+ username +"'";
		 	Statement statement = conn.createStatement();
		 	ResultSet result = statement.executeQuery(query);
		 	boolean output = result.next();
		 	chiudi_connessione(conn);
		 	return output;
		}else {
			String query = "SELECT username FROM `utente_votato_referendum` WHERE `id_referendum` = "+ sessione.id +" AND `username` = '"+ username +"'";
		 	Statement statement = conn.createStatement();
		 	ResultSet result = statement.executeQuery(query);
		 	boolean output = result.next();
		 	chiudi_connessione(conn);
		 	return output;
		}
			
	}
	
	public void segna_che_elettore_ha_votato(String username, SessioneDiVotoDTO sessione) throws ClassNotFoundException, SQLException {
		
		Connection conn = connessione_db();
		
	 	String query = "SELECT is_admin FROM utenti WHERE username = ?";
        PreparedStatement statement= conn.prepareStatement(query);
        
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
	 	
	 	if(resultSet.next()) {
	 		boolean admin = resultSet.getBoolean("is_admin");
	 		if(admin)
	 			throw new SQLException("L'username corrisponde ad un gestore del sistema e non ad un elettore");
	 	}else {
	 		throw new SQLException("L'username non corrisponde ad un elettore");
	 	}
	 	
	 	if(!check_elettore_ha_diritto_di_voto(username, sessione))
	 		throw new SQLException("L'elettore non ha diritto di votare in questa sessione di voto");
	 	
	 	int result=0;	 	
		if(sessione instanceof ReferendumDTO) {
			query = "INSERT INTO `utente_votato_referendum` (`username`, `id_referendum`) VALUES ('"+ username +"', "+sessione.id+")";
			Statement statement2 = conn.createStatement();
			
			try {
			 	result = statement2.executeUpdate(query);
			}catch(Exception e) {
				chiudi_connessione(conn);
				throw new SQLException("L'elettore ha già espresso un voto in questa sessione di voto");
			}
			
			if(result == 0) {
				chiudi_connessione(conn);
				throw new SQLException("Non è stato possibile registrare che l'elettore ha votato");
			}
		}else {
			query = "INSERT INTO `utente_votato_sessione_di_votazione` (`username`, `id_sessione_di_voto`) VALUES ('"+ username +"', "+sessione.id+")";
			Statement statement2 = conn.createStatement();
			try {
			 	result = statement2.executeUpdate(query);
			}catch(Exception e) {
				chiudi_connessione(conn);
				throw new SQLException("L'elettore ha già espresso un voto in questa sessione di voto");
			}
			if(result == 0) {
				chiudi_connessione(conn);
				throw new SQLException("non è stato possibile registrare che l'elettore ha votato");
			}
		}
		chiudi_connessione(conn);
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
	
	public boolean voto_categorico_sessione_di_votazione(ElettoreDTO elettore, SessioneDiVotazioneDTO sessione, PartitoDTO partito) throws ClassNotFoundException, SQLException, LogException{
		
		
		if(check_elettore_ha_gia_votato(elettore.username, sessione))
			return false;
		
		Connection conn = connessione_db();
		String query="";
		if(check_presente_voto(sessione, partito))
			query = "UPDATE `voti_sessione_di_voto` SET n_voti = n_voti+1 WHERE `voti_sessione_di_voto`.`id_sessione_voto` =" +sessione.id+" AND `voti_sessione_di_voto`.`partito` ='" + partito.nome +"'";
		else			 	
			query = "INSERT INTO `voti_sessione_di_voto` (`id_sessione_voto`, `partito`, `n_voti`) VALUES ("+sessione.id+", '"+partito.nome+"', 1)";
	 	Statement statement = conn.createStatement();
        
	 	statement.executeUpdate(query);
	 	chiudi_connessione(conn);
	 	
	 
 		segna_che_elettore_ha_votato(elettore.username, sessione);
 		Timestamp now = new Timestamp(System.currentTimeMillis());
 		auditing.evento_generato_da_DAO(elettore, now, "ha votato alla votazione: (id: " + sessione.id + ") " + sessione );
	 
	 	return true;	

	}
	
	public boolean voto_ordinale_sessione_di_votazione(ElettoreDTO elettore, SessioneDiVotazioneDTO sessione, List <PartitoDTO> partito) throws ClassNotFoundException, SQLException, LogException{
		
		
		if(check_elettore_ha_gia_votato(elettore.username, sessione))
			return false;
		
		Connection conn = connessione_db();
		String query = "";
		for(int i=0; i<partito.size(); i++) {
			if(check_presente_voto(sessione, partito.get(i)))
				query += "UPDATE `voti_sessione_di_voto` SET n_voti = n_voti+ "+ i +" WHERE `id_sessione_voto` =" +sessione.id+" AND `partito` ='" + partito.get(i).nome +"';\n";
			else			 	
				query += "INSERT INTO `voti_sessione_di_voto` (`id_sessione_voto`, `partito`, `n_voti`) VALUES ("+sessione.id+", '"+partito.get(i).nome+"', "+i+");\n";
		}
		
	
	 	Statement statement = conn.createStatement();
        
	 	statement.executeUpdate(query);
	 	chiudi_connessione(conn);
	 	
	 	
 		segna_che_elettore_ha_votato(elettore.username, sessione);
 		Timestamp now = new Timestamp(System.currentTimeMillis());
 		auditing.evento_generato_da_DAO(elettore, now, "ha votato alla votazione: (id: " + sessione.id + ") " + sessione );
 	
 		return true;

	}
	
	
	public boolean voto_categorico_con_preferenze_sessione_di_votazione(ElettoreDTO elettore, SessioneDiVotazioneDTO sessione, PartitoDTO partito, List<PersonaDTO>ordine_persone) throws ClassNotFoundException, SQLException, LogException{
	
		
		if(voto_categorico_sessione_di_votazione(elettore, sessione, partito)) {
		
			Connection conn = connessione_db();
			String query = "";
			
			for(int i=0; i<ordine_persone.size(); i++) {
				if(check_presente_voto_preferenza(sessione, ordine_persone.get(i)))
					query += "UPDATE `voti_preferenze_sessione_di_voto` SET n_voti = n_voti+ "+ i +" WHERE `id_sessione_di_votazione` =" +sessione.id+" AND `id_persona` = " + ordine_persone.get(i).id +";\n";
				else			 	
					query += "INSERT INTO `voti_preferenze_sessione_di_voto` (`id_sessione_di_votazione`, `id_persona`, `n_voti`) VALUES ("+sessione.id+", "+ordine_persone.get(i).id+", "+i+");\n";
			}
			
		 	Statement statement = conn.createStatement();
	        
		 	statement.executeUpdate(query);
		 	chiudi_connessione(conn);
		 	
		 	return true;	
		}else {
			return false;
		}

	}

	@Override
	public boolean check_elettore_ha_diritto_di_voto(String username, SessioneDiVotoDTO sessione) throws ClassNotFoundException, SQLException {
		
		Connection conn = connessione_db();
		String query = "";
		if(sessione instanceof SessioneDiVotazioneDTO)
			query = "SELECT * FROM `diritti_sessioni_di_voto` WHERE `id_sessione_di_voto` = "+sessione.id+" AND `utente` = '"+username+"'";
		else if (sessione instanceof ReferendumDTO)
			query = "SELECT * FROM `diritti_referendum` WHERE `id_referendum` = "+sessione.id+" AND `utente` = '"+username+"'";
	 				 	
	 	Statement statement = conn.createStatement();
        
	 	ResultSet result = statement.executeQuery(query);
	 	boolean output = result.next();
	 	chiudi_connessione(conn);
	 	return output;	
	}

	@Override
	public List<ElettoreDTO> get_elettori() throws ClassNotFoundException, SQLException {
		Connection conn = connessione_db();
		
	 	String query = 	"SELECT username, email, nome, cognome, is_maschio, data_nascita, comune_nascita, provincia_nascita, codice_fiscale FROM utenti " + 
	 					"WHERE is_admin=0 "+
	 					"order by username";
	 	
	 	Statement statement = conn.createStatement();
	 	ResultSet resultSet = statement.executeQuery(query);
       
	 	List<ElettoreDTO> elettori = new ArrayList<ElettoreDTO>();
	 	
        if(resultSet.next()) {
        	do {
        		elettori.add(new ElettoreDTO(resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("nome"), resultSet.getString("cognome"), resultSet.getBoolean("is_maschio"), resultSet.getDate("data_nascita"), resultSet.getString("comune_nascita"), resultSet.getString("provincia_nascita"), resultSet.getString("codice_fiscale")));
        	} while (resultSet.next());
        	chiudi_connessione(conn);
        }
        return elettori;
	}

}
