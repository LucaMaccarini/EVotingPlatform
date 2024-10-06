package progetto_java_e_voting_plat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class SessioneDiVotoDAOImplementation  extends DAOImplementationBasics implements SessioneDiVotoDAO {

	private static SessioneDiVotoDAOImplementation singleton_instance;
	private AuditingListener auditing;
	
	
	public static SessioneDiVotoDAOImplementation get_singleton_instance() {
		if(singleton_instance == null)
			singleton_instance = new SessioneDiVotoDAOImplementation();
		return singleton_instance;
	}
	
	public SessioneDiVotoDAOImplementation() {
		auditing = Auditing.get_singleton_instance();
	}
	
	
	@Override
	public List<SessioneDiVotoDTO> get_sessioni_di_voto_attive_con_diritto_di_voto(ElettoreDTO elettore) throws SQLException, ClassNotFoundException {
		
		Connection conn = connessione_db();
		
	 	String query = 	"SELECT sessioni_di_votazione.id, sessioni_di_votazione.nome, sessioni_di_votazione.descrizione as descrizione_o_quesito, sessioni_di_votazione.proprietario, sessioni_di_votazione.modalita_voto, sessioni_di_votazione.modalita_vincitore, sessioni_di_votazione.data_termine FROM `diritti_sessioni_di_voto` " +
	 			"inner join sessioni_di_votazione on diritti_sessioni_di_voto.id_sessione_di_voto = sessioni_di_votazione.id " + 
	 			"WHERE diritti_sessioni_di_voto.utente = '"+ elettore.username + "' and data_termine > CURRENT_TIMESTAMP " + 
	 			"UNION ALL " + 
	 			"SELECT sessioni_referendum.id, sessioni_referendum.nome, sessioni_referendum.quesito as descrizione_o_quesito, sessioni_referendum.proprietario, 'referendum', sessioni_referendum.modalita_vincitore, sessioni_referendum.data_termine FROM `diritti_referendum` " +
	 			"inner join sessioni_referendum on diritti_referendum.id_referendum = sessioni_referendum.id " + 
	 			"WHERE diritti_referendum.utente = '"+ elettore.username +"' and data_termine > CURRENT_TIMESTAMP " + 
	 			"ORDER BY data_termine";
	 	
	 	Statement statement = conn.createStatement();
	 	ResultSet resultSet = statement.executeQuery(query);
       
	 	List<SessioneDiVotoDTO> sessioni = new ArrayList<SessioneDiVotoDTO>();
        if(resultSet.next()) {
        	do {
        		String mod_voto_string = resultSet.getString("modalita_voto");
        		if (mod_voto_string.equals("referendum")) {
        			modalita_calcolo_vincitore_referendum mod_vincitore = modalita_calcolo_vincitore_referendum.valueOf(resultSet.getString("modalita_vincitore"));
        			SessioneDiVotoDTO elemento = new ReferendumDTO(resultSet.getInt("id"), resultSet.getString("nome"), resultSet.getString("descrizione_o_quesito"), resultSet.getString("proprietario"), mod_vincitore, resultSet.getTimestamp("data_termine"));
        			sessioni.add(elemento);
        		}else {
        			modalita_voto mod_voto = modalita_voto.valueOf(mod_voto_string);
        			modalita_calcolo_vincitore_sessione_di_votazione mod_vincitore = modalita_calcolo_vincitore_sessione_di_votazione.valueOf(resultSet.getString("modalita_vincitore"));
        			SessioneDiVotoDTO elemento = new SessioneDiVotazioneDTO (resultSet.getInt("id"), resultSet.getString("nome"), resultSet.getString("descrizione_o_quesito"), resultSet.getString("proprietario"), mod_voto, mod_vincitore, resultSet.getTimestamp("data_termine"));
        			sessioni.add(elemento);
        		}
        	} while (resultSet.next());
        	chiudi_connessione(conn);
        	
        }
        return sessioni;
	}

	@Override
	public void scarica_e_aggiungi_partiti_candidati(SessioneDiVotazioneDTO sessione) throws SQLException, ClassNotFoundException {
		
		Connection conn = connessione_db();
		
	 	String query = 	"select partiti.nome, partiti.descrizione from candidati_sessione_di_voto " + 
	 					"inner JOIN partiti ON partiti.nome = candidati_sessione_di_voto.nome_partito " + 
	 					"where candidati_sessione_di_voto.id_sessione_voto = " + sessione.id;
	 	
	 	Statement statement = conn.createStatement();
	 	ResultSet resultSet = statement.executeQuery(query);
       
	 	List<PartitoDTO> partiti = new ArrayList<PartitoDTO>();
        if(resultSet.next()) {
        	do {
        		partiti.add(new PartitoDTO(resultSet.getString("nome"), resultSet.getString("descrizione")));
        	} while (resultSet.next());
        	chiudi_connessione(conn);
        	
        }
        sessione.set_partiti_candidati(partiti);
	}

	@Override
	public List<SessioneDiVotoDTO> get_sessioni_di_voto_terminate_con_vincitore_calcolato() throws SQLException, ClassNotFoundException {
		Connection conn = connessione_db();
		
	 	String query = 	"SELECT id, nome, descrizione as descrizione_o_quesito, proprietario, modalita_voto, modalita_vincitore, data_termine, vincitore FROM `sessioni_di_votazione` WHERE vincitore != 'non_calcolato' " + 
	 					"UNION ALL " + 
	 					"SELECT id, nome, quesito as descrizione_o_quesito, proprietario, 'referendum' as modalita_voto, modalita_vincitore, data_termine, vincitore FROM sessioni_referendum WHERE vincitore != 'non_calcolato' ORDER BY data_termine DESC";
	 	
	 	Statement statement = conn.createStatement();
	 	ResultSet resultSet = statement.executeQuery(query);
       
	 	List<SessioneDiVotoDTO> sessioni = new ArrayList<SessioneDiVotoDTO>();
        if(resultSet.next()) {
        	do {
        		String mod_voto_string = resultSet.getString("modalita_voto");
        		if (mod_voto_string.equals("referendum")) {
        			modalita_calcolo_vincitore_referendum mod_vincitore = modalita_calcolo_vincitore_referendum.valueOf(resultSet.getString("modalita_vincitore"));
        			SessioneDiVotoDTO elemento = new ReferendumDTO(resultSet.getInt("id"), resultSet.getString("nome"), resultSet.getString("descrizione_o_quesito"), resultSet.getString("proprietario"), mod_vincitore, resultSet.getTimestamp("data_termine"), resultSet.getString("vincitore"));
        			sessioni.add(elemento);
        		}else {
        			modalita_voto mod_voto = modalita_voto.valueOf(mod_voto_string);
        			modalita_calcolo_vincitore_sessione_di_votazione mod_vincitore = modalita_calcolo_vincitore_sessione_di_votazione.valueOf(resultSet.getString("modalita_vincitore"));
        			
        			String nome_partito = resultSet.getString("vincitore");
        			PartitoDAO partito_DAO = PartitoDAOImplementation.get_singleton_instance();
        			
        			SessioneDiVotoDTO elemento = new SessioneDiVotazioneDTO (resultSet.getInt("id"), resultSet.getString("nome"), resultSet.getString("descrizione_o_quesito"), resultSet.getString("proprietario"), mod_voto, mod_vincitore, resultSet.getTimestamp("data_termine"),  partito_DAO.get_partito(nome_partito));
        			sessioni.add(elemento);
        		}
        	} while (resultSet.next());
        	chiudi_connessione(conn);
        	
        }
        return sessioni;
	}
	
	@Override
	public List<SessioneDiVotoDTO> get_sessioni_di_voto_con_vincitore_non_calcolato() throws SQLException, ClassNotFoundException {
		Connection conn = connessione_db();
		
	 	String query = 	"SELECT id, nome, descrizione as descrizione_o_quesito, proprietario, modalita_voto, modalita_vincitore, data_termine, vincitore FROM `sessioni_di_votazione` WHERE vincitore = 'non_calcolato' " + 
	 					"UNION ALL " + 
	 					"SELECT id, nome, quesito as descrizione_o_quesito, proprietario, 'referendum' as modalita_voto, modalita_vincitore, data_termine, vincitore FROM sessioni_referendum WHERE vincitore = 'non_calcolato' ORDER BY data_termine DESC";
	 	
	 	Statement statement = conn.createStatement();
	 	ResultSet resultSet = statement.executeQuery(query);
       
	 	List<SessioneDiVotoDTO> sessioni = new ArrayList<SessioneDiVotoDTO>();
        if(resultSet.next()) {
        	do {
        		String mod_voto_string = resultSet.getString("modalita_voto");
        		if (mod_voto_string.equals("referendum")) {
        			modalita_calcolo_vincitore_referendum mod_vincitore = modalita_calcolo_vincitore_referendum.valueOf(resultSet.getString("modalita_vincitore"));
        			SessioneDiVotoDTO elemento = new ReferendumDTO(resultSet.getInt("id"), resultSet.getString("nome"), resultSet.getString("descrizione_o_quesito"), resultSet.getString("proprietario"), mod_vincitore, resultSet.getTimestamp("data_termine"));
        			sessioni.add(elemento);
        		}else {
        			modalita_voto mod_voto = modalita_voto.valueOf(mod_voto_string);
        			modalita_calcolo_vincitore_sessione_di_votazione mod_vincitore = modalita_calcolo_vincitore_sessione_di_votazione.valueOf(resultSet.getString("modalita_vincitore"));
        			SessioneDiVotoDTO elemento = new SessioneDiVotazioneDTO (resultSet.getInt("id"), resultSet.getString("nome"), resultSet.getString("descrizione_o_quesito"), resultSet.getString("proprietario"), mod_voto, mod_vincitore, resultSet.getTimestamp("data_termine"));
        			sessioni.add(elemento);
        		}
        	} while (resultSet.next());
        	chiudi_connessione(conn);
        	
        }
        return sessioni;
	}

	
	private void calcola_vincitore_referendum(ReferendumDTO referendum) throws ClassNotFoundException, SQLException {
		Connection conn = connessione_db();
	 	String query = 	"SELECT n_si, n_no from sessioni_referendum where id =" + referendum.id;
	 	Statement statement = conn.createStatement();
	 	ResultSet resultSet = statement.executeQuery(query);
	 	resultSet.next();
	 	int si = resultSet.getInt("n_si");
	 	int no = resultSet.getInt("n_no");
	 	chiudi_connessione(conn);
	 	
	 	String vincitore = "Nessuno";
	 	
	 	if(si>no) 
	 		vincitore = "Si";
	 	else if(no>si)
	 		vincitore = "No";
	 	
	 	registrazione_vincitore_referendum(referendum, vincitore);
		
	}
	
	private void calcola_vincitore_referendum(ReferendumDTO referendum, int si, int no) throws ClassNotFoundException, SQLException {	 	
	 	String vincitore = "Nessuno";
	 	if(si>no) 
	 		vincitore = "Si";
	 	else if(no>si)
	 		vincitore = "No";
	 	
	 	registrazione_vincitore_referendum(referendum, vincitore);
		
	}
	
	private void registrazione_vincitore_referendum(ReferendumDTO referendum, String vincitore) throws ClassNotFoundException, SQLException {
		referendum.set_vincitore(vincitore);
		Connection conn = connessione_db();
	 	String query = "UPDATE `sessioni_referendum` SET `vincitore` = '"+vincitore+"' WHERE `sessioni_referendum`.`id` = " + referendum.id; 
	 	Statement statement = conn.createStatement();
	 	statement.executeUpdate(query);
	 	
	 	if(referendum.modalita_vincitore == modalita_calcolo_vincitore_referendum.referendum_con_quorum)
	 		cancella_astenuti_referendum_con_quorum(referendum);
	 	
	 	
	 	cancella_diritti_referendum(referendum);
	 	cancella_voti_registrati_dagli_utenti_per_referendum(referendum);
	}
	
	private void registra_vincitore_sessione_di_votazione(SessioneDiVotazioneDTO votazione, PartitoDTO vincitore) throws ClassNotFoundException, SQLException {
		votazione.set_vincitore(vincitore);
		Connection conn = connessione_db();
	 	String query = "UPDATE `sessioni_di_votazione` SET `vincitore` = '"+vincitore.nome+"' WHERE id = " + votazione.id; 
	 	Statement statement = conn.createStatement();
	 	statement.executeUpdate(query);
	 	
	 	cancella_candidati(votazione);
	 	cancella_diritti_sessione_di_votazione(votazione);
	 	cancella_voti_registrati_dagli_utenti_per_sessione_di_votazione(votazione);
	 	
	 	if(votazione.modalita_vincitore == modalita_calcolo_vincitore_sessione_di_votazione.maggioranza_assoluta)
	 		cancella_astenuti_votazione_maggioranza_assoluta(votazione);
	 	
	 	if(votazione.modalita_voto != modalita_voto.voto_ordinale)
	 		cancella_voti_sessione_di_votazione(votazione);
	 	
	 	if(votazione.modalita_voto == modalita_voto.voto_categorico_con_preferenze)
	 		cancella_preferenze_non_vincitore(votazione, vincitore);
	 	
	}
	
	
	@Override
	public void calcola_vincitore(GestoreDelSistemaDTO gestore, SessioneDiVotoDTO sessione) throws SQLException, ClassNotFoundException, LogException {

		
		if(sessione instanceof ReferendumDTO) {
			ReferendumDTO referendum = (ReferendumDTO) sessione;
			
			switch(referendum.modalita_vincitore) {
				case referendum_senza_quorum:
					calcola_vincitore_referendum(referendum);
					break;
				case referendum_con_quorum:
					Connection conn = connessione_db();
				 	String query = 	"SELECT COUNT(*) as result  FROM diritti_referendum WHERE id_referendum = " + referendum.id;
				 	Statement statement = conn.createStatement();
				 	ResultSet resultSet = statement.executeQuery(query);
				 	resultSet.next();
				 	int persone_con_diritto = resultSet.getInt("result");
				 	
				 	query = "SELECT n_si, n_no from sessioni_referendum where id =" + referendum.id;
				 	statement = conn.createStatement();
				 	resultSet = statement.executeQuery(query);
				 	resultSet.next();
				 	int si = resultSet.getInt("n_si");
				 	int no = resultSet.getInt("n_no");
				 	
				 	//query = "SELECT COUNT(*) as votanti from utente_votato_referendum where id_referendum =" + referendum.id;
				 	//resultSet = statement.executeQuery(query);
				 	//resultSet.next();
				 	//int votanti = resultSet.getInt("votanti");
				 	int votanti = si + no;
				 	
				 	query = "SELECT n_astenuti from astenuti_referendum_con_quorum where id_referendum =" + referendum.id;
				 	resultSet = statement.executeQuery(query);
				 	if(resultSet.next())
				 		votanti += resultSet.getInt("n_astenuti");
				 	
				 	chiudi_connessione(conn);
				 	
				 	
				 	if(votanti > persone_con_diritto/2) {
				 		calcola_vincitore_referendum(referendum, si, no);
				 	}else {
				 		registrazione_vincitore_referendum(referendum, "Nessuno");			 		
				 	}	
			}
			
			Timestamp now = new Timestamp(System.currentTimeMillis());
			auditing.evento_generato_da_DAO(gestore, now, "ha avviato il calcolo del vincitore per il referendum: " + referendum);
			
		}else if(sessione instanceof SessioneDiVotazioneDTO) {
			SessioneDiVotazioneDTO votazione = (SessioneDiVotazioneDTO)sessione;
			if(votazione.modalita_voto == modalita_voto.voto_ordinale) {
				calcola_voto_ordinale(votazione);
			}else {
				if(votazione.modalita_vincitore == modalita_calcolo_vincitore_sessione_di_votazione.maggioranza)
					calcola_voto_categorico_maggioranza(votazione);
				else if(votazione.modalita_vincitore == modalita_calcolo_vincitore_sessione_di_votazione.maggioranza_assoluta) 
					calcola_voto_categorico_maggioranza_assoluta(votazione);
			}
			
			Timestamp now = new Timestamp(System.currentTimeMillis());
			auditing.evento_generato_da_DAO(gestore, now, "ha avviato il calcolo del vincitore per la sessione: " + (SessioneDiVotazioneDTO)sessione);
		 	
				
		}
				
		
	}
	
	private void calcola_voto_categorico_maggioranza_assoluta(SessioneDiVotazioneDTO votazione) throws ClassNotFoundException, SQLException {
		Connection conn = connessione_db();
	 	String query = 	"SELECT MAX(n_voti) as voto_max, SUM(n_voti) as n_persone FROM `voti_sessione_di_voto` WHERE id_sessione_voto = " + votazione.id;
	 	Statement statement = conn.createStatement();
	 	ResultSet resultSet = statement.executeQuery(query);
	 	resultSet.next();
	 	Integer voto_max = resultSet.getInt("voto_max");	 	
	 	Integer n_persone = resultSet.getInt("n_persone");
	 	
	 	query = "SELECT n_astenuti as n_astenuti FROM `astenuti_votazione_maggioranza_assoluta` WHERE `id_sessione_di_votazione` = " + votazione.id;
	 	resultSet = statement.executeQuery(query);
	 	int n_persone_totali = 0;
	 	
	 	if(resultSet.next())
	 		n_persone_totali += resultSet.getInt("n_astenuti");
	 	
	 	if(n_persone != null)
	 		n_persone_totali+=n_persone;
	 	
	 	
	 	PartitoDAO partito_DAO = PartitoDAOImplementation.get_singleton_instance();
	 	if(voto_max!=null && voto_max > n_persone_totali/2) {
	 		calcola_voto_categorico_maggioranza(votazione);
	 	}else {
	 	PartitoDTO vincitore = partito_DAO.get_partito("Nessuno");
 		registra_vincitore_sessione_di_votazione(votazione, vincitore);
	 	}
	}
	
	private void calcola_voto_categorico_maggioranza(SessioneDiVotazioneDTO votazione) throws ClassNotFoundException, SQLException {
		Connection conn = connessione_db();
	 	String query = 	"SELECT MAX(n_voti) as voto_max FROM `voti_sessione_di_voto` WHERE id_sessione_voto = " + votazione.id;
	 	Statement statement = conn.createStatement();
	 	ResultSet resultSet = statement.executeQuery(query);
	 	resultSet.next();
	 	Integer voto_max = resultSet.getInt("voto_max");
	 	PartitoDAO partito_DAO = PartitoDAOImplementation.get_singleton_instance();
	 	if(voto_max!=null) {
	 		
		 	query = "SELECT partito FROM `voti_sessione_di_voto` WHERE n_voti = "+voto_max+" AND id_sessione_voto = " + votazione.id;
			statement = conn.createStatement();
		 	resultSet = statement.executeQuery(query);
		 	resultSet.next();
		 	//int righe = resultSet.getRow();
		 	//System.out.println(righe);
		 	String nome_partito = resultSet.getString("partito");
		 	if(!resultSet.next()) {
		 		//System.out.println(nome_partito);
			 	chiudi_connessione(conn);
		 		PartitoDTO vincitore = partito_DAO.get_partito(nome_partito);
		 		registra_vincitore_sessione_di_votazione(votazione, vincitore);
		 		return;		 		
		 	}
		 	
	 	}
	 	chiudi_connessione(conn);
	 	PartitoDTO vincitore = partito_DAO.get_partito("Nessuno");
 		registra_vincitore_sessione_di_votazione(votazione, vincitore);
	}

	private void calcola_voto_ordinale(SessioneDiVotazioneDTO votazione) throws ClassNotFoundException, SQLException {
		Connection conn = connessione_db();
	 	String query = 	"SELECT MIN(n_voti) as voto_min FROM `voti_sessione_di_voto` WHERE id_sessione_voto = " + votazione.id;
	 	Statement statement = conn.createStatement();
	 	ResultSet resultSet = statement.executeQuery(query);
	 	resultSet.next();
	 	Integer voto_min = resultSet.getInt("voto_min");
	 	PartitoDAO partito_DAO = PartitoDAOImplementation.get_singleton_instance();
	 	if(voto_min!=null) {
	 		
		 	query = "SELECT partito FROM `voti_sessione_di_voto` WHERE n_voti = "+voto_min+" AND id_sessione_voto = " + votazione.id;
			statement = conn.createStatement();
		 	resultSet = statement.executeQuery(query);
		 	if(resultSet.next()) {
		 	
			 	String nome_partito = resultSet.getString("partito");
			 	
			 	if(!resultSet.next()) {
			 		
			 		//System.out.println(nome_partito);
				 	chiudi_connessione(conn);
			 		PartitoDTO vincitore = partito_DAO.get_partito(nome_partito);
			 		registra_vincitore_sessione_di_votazione(votazione, vincitore);
			 		return;		 		
			 	}
		 	}
		 	
	 	}
	 	PartitoDTO vincitore = partito_DAO.get_partito("Nessuno");
 		registra_vincitore_sessione_di_votazione(votazione, vincitore);
	}
	
	private void cancella_candidati(SessioneDiVotoDTO sessione) throws ClassNotFoundException, SQLException {
		Connection conn = connessione_db();
	 	String query = "DELETE FROM candidati_sessione_di_voto WHERE id_sessione_voto = " + sessione.id; 
	 	Statement statement = conn.createStatement();
	 	statement.executeUpdate(query);
	}
	
	private void cancella_diritti_referendum(ReferendumDTO referendum) throws ClassNotFoundException, SQLException {
		Connection conn = connessione_db();
	 	String query = "DELETE FROM diritti_referendum WHERE id_referendum = " + referendum.id; 
	 	Statement statement = conn.createStatement();
	 	statement.executeUpdate(query);
	}
	
	private void cancella_diritti_sessione_di_votazione(SessioneDiVotazioneDTO sessione) throws ClassNotFoundException, SQLException {
		Connection conn = connessione_db();
	 	String query = "DELETE FROM diritti_sessioni_di_voto WHERE id_sessione_di_voto = " + sessione.id; 
	 	Statement statement = conn.createStatement();
	 	statement.executeUpdate(query);
	}
	
	private void cancella_voti_registrati_dagli_utenti_per_referendum(ReferendumDTO referendum) throws ClassNotFoundException, SQLException {
		Connection conn = connessione_db();
	 	String query = "DELETE FROM utente_votato_referendum WHERE id_referendum = " + referendum.id; 
	 	Statement statement = conn.createStatement();
	 	statement.executeUpdate(query);
	}
	
	private void cancella_voti_registrati_dagli_utenti_per_sessione_di_votazione(SessioneDiVotazioneDTO sessione) throws ClassNotFoundException, SQLException {
		Connection conn = connessione_db();
	 	String query = "DELETE FROM utente_votato_sessione_di_votazione WHERE id_sessione_di_voto = " + sessione.id; 
	 	Statement statement = conn.createStatement();
	 	statement.executeUpdate(query);
	}
	
	private void cancella_voti_sessione_di_votazione(SessioneDiVotazioneDTO sessione) throws ClassNotFoundException, SQLException {

		Connection conn = connessione_db();
	 	String query = "DELETE FROM voti_sessione_di_voto WHERE id_sessione_voto = " + sessione.id; 
	 	Statement statement = conn.createStatement();
	 	statement.executeUpdate(query);
	}
	
	private void cancella_preferenze_non_vincitore(SessioneDiVotazioneDTO sessione, PartitoDTO partito) throws ClassNotFoundException, SQLException {

		cancella_voti_sessione_di_votazione(sessione);
		Connection conn = connessione_db();
	 	String query = "DELETE FROM voti_preferenze_sessione_di_voto "+
	 					"WHERE id_sessione_di_votazione = " + sessione.id + 
	 					" AND id_persona NOT IN (Select id from persone WHERE partito = '"+ partito.nome +"')";
	 	Statement statement = conn.createStatement();
	 	statement.executeUpdate(query);
	}
	
	public void registra_nuova_sessione_di_votazione(String nome, String descrizione, GestoreDelSistemaDTO proprietario, modalita_voto mod_voto, modalita_calcolo_vincitore_sessione_di_votazione mod_vincitore, Timestamp data_termine, List<ElettoreDTO> aventi_il_diritto, List<PartitoDTO> candidati) throws SQLException, ClassNotFoundException, LogException {

		String stringa_mod_voto="";
		String stringa_mod_vincitore="";
		
		switch(mod_voto) {
			case voto_ordinale: stringa_mod_voto="voto_ordinale";
				break;
			case voto_categorico: stringa_mod_voto="voto_categorico";
				break;
			case voto_categorico_con_preferenze: stringa_mod_voto="voto_categorico_con_preferenze";
				break;
		}
		
		switch(mod_vincitore) {
			case maggioranza: stringa_mod_vincitore="maggioranza";
				break;
			case maggioranza_assoluta: stringa_mod_vincitore="maggioranza_assoluta";
				break;
		}
		
		
		
		Connection conn = connessione_db();
		String query = "SELECT max(id) as id FROM `sessioni_di_votazione`";
		Statement normalstatement = conn.createStatement();
	 	ResultSet resultSet = normalstatement.executeQuery(query);
	 	resultSet.next();
	 	int id = 1;
	 	try {
	 		id = resultSet.getInt("id")+1;
	 	}catch(Exception e) {
	 		
	 	}
		
		
		query = "INSERT INTO sessioni_di_votazione (`id`, `nome`, `descrizione`, `proprietario`, `modalita_voto`, `modalita_vincitore`, `data_termine`, `vincitore`) VALUES ("+id+", ?, ?, ?, ?, ?, ?, 'non_calcolato');\n";
		for(int i=0; i<aventi_il_diritto.size(); i++) {
			query += "INSERT INTO `diritti_sessioni_di_voto` (`id_sessione_di_voto`, `utente`) VALUES ("+id+", '"+aventi_il_diritto.get(i).username+"');\n";
		}
		
		for(int i=0; i<candidati.size(); i++) {
			query += "INSERT INTO `candidati_sessione_di_voto` (`id_sessione_voto`, `nome_partito`) VALUES ("+id+", '"+candidati.get(i).nome+"');\n";
		}
		
        PreparedStatement statement= conn.prepareStatement(query);
        
        statement.setString(1, nome);
        statement.setString(2, descrizione);
        statement.setString(3, proprietario.username);
        statement.setString(4, stringa_mod_voto);
        statement.setString(5, stringa_mod_vincitore);
        statement.setTimestamp(6, data_termine);
        statement.executeUpdate();  
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        auditing.evento_generato_da_DAO(proprietario, now, "ha registrato una nuova votazione (id: " + id +") " + nome + " | " + descrizione);
	}
	
	public void registra_nuovo_referendum(String nome, String quesito, GestoreDelSistemaDTO proprietario, modalita_calcolo_vincitore_referendum mod_vincitore, Timestamp data_termine, List<ElettoreDTO> aventi_il_diritto) throws SQLException, ClassNotFoundException, LogException {
		String stringa_mod_vincitore="";
			
		switch(mod_vincitore) {
			case referendum_senza_quorum: stringa_mod_vincitore="referendum_senza_quorum";
				break;
			case referendum_con_quorum: stringa_mod_vincitore="referendum_con_quorum";
				break;
		}
		
		Connection conn = connessione_db();
		String query = "SELECT max(id) as id FROM `sessioni_referendum`";
		Statement normalstatement = conn.createStatement();
	 	ResultSet resultSet = normalstatement.executeQuery(query);
	 	resultSet.next();
	 	int id = 1;
	 	try {
	 		id = resultSet.getInt("id")+1;
	 	}catch(Exception e) {
	 		
	 	}
	 	
		query = "INSERT INTO sessioni_referendum (`id`, `nome`, `quesito`, `proprietario`, `modalita_vincitore`, `data_termine`, `vincitore`) VALUES ("+id+", ?, ?, ?, ?, ?, 'non_calcolato');\n";
		for(int i=0; i<aventi_il_diritto.size(); i++) {
			query += "INSERT INTO `diritti_referendum` (`id_referendum`, `utente`) VALUES ("+id+", '"+aventi_il_diritto.get(i).username+"');\n";
		}
        PreparedStatement statement= conn.prepareStatement(query);
        
        statement.setString(1, nome);
        statement.setString(2, quesito);
        statement.setString(3, proprietario.username);
        statement.setString(4, stringa_mod_vincitore);
        statement.setTimestamp(5, data_termine);
        statement.executeUpdate(); 
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        auditing.evento_generato_da_DAO(proprietario, now, "ha registrato un nuovo referendum (id: " + id + ") " + nome + " | " + quesito);
	}

	@Override
	public boolean check_vincitore_gia_calcolato(SessioneDiVotoDTO sessione) throws SQLException, ClassNotFoundException {
		Connection conn = connessione_db();
		
	 	String query = "";
	 	if(sessione instanceof SessioneDiVotazioneDTO)
	 		query = "SELECT vincitore FROM sessioni_di_votazione WHERE id = "+sessione.id+" AND vincitore <> 'non_calcolato'";
	 	else if (sessione instanceof ReferendumDTO)
	 		query = "SELECT vincitore FROM sessioni_referendum WHERE id = "+sessione.id+" AND vincitore <> 'non_calcolato'";
	 	
        Statement statement= conn.createStatement();
        
        ResultSet resultSet = statement.executeQuery(query);
        
        boolean result = resultSet.next();
		chiudi_connessione(conn);
		return result;
	}
	
	private void cancella_astenuti_votazione_maggioranza_assoluta(SessioneDiVotazioneDTO sessione) throws ClassNotFoundException, SQLException {

		Connection conn = connessione_db();
	 	String query = "DELETE FROM `astenuti_votazione_maggioranza_assoluta` WHERE `astenuti_votazione_maggioranza_assoluta`.`id_sessione_di_votazione` = " + sessione.id; 
	 	Statement statement = conn.createStatement();
	 	statement.executeUpdate(query);
	}
	
	private void cancella_astenuti_referendum_con_quorum(ReferendumDTO referendum) throws ClassNotFoundException, SQLException {
		

		Connection conn = connessione_db();
	 	String query = "DELETE FROM `astenuti_referendum_con_quorum` WHERE `astenuti_referendum_con_quorum`.`id_referendum` = " + referendum.id; 
	 	Statement statement = conn.createStatement();
	 	statement.executeUpdate(query);
	}

}
