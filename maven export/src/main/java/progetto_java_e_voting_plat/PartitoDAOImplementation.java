package progetto_java_e_voting_plat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PartitoDAOImplementation extends DAOImplementationBasics implements PartitoDAO {

	private static PartitoDAOImplementation singleton_instance;
	private AuditingListener auditing;
	
	public static PartitoDAOImplementation get_singleton_instance() {
		if(singleton_instance == null)
			singleton_instance = new PartitoDAOImplementation();
		return singleton_instance;
	}
	
	public PartitoDAOImplementation() {
		auditing = Auditing.get_singleton_instance();
	}
	
	@Override
	public void scarica_e_aggiungi_persone_del_partito(PartitoDTO partito) throws ClassNotFoundException, SQLException{
		Connection conn = connessione_db();
		
	 	String query = 	"SELECT persone.id, persone.nome, persone.cognome FROM `persone` " + 
	 					"inner JOIN partiti on partiti.nome = persone.partito " + 
	 					"where partiti.nome = '"+ partito.nome +"'";
	 	
	 	Statement statement = conn.createStatement();
	 	ResultSet resultSet = statement.executeQuery(query);
       
	 	List<PersonaDTO> persone = new ArrayList<PersonaDTO>();
        if(resultSet.next()) {
        	do {
        		persone.add(new PersonaDTO(resultSet.getInt("id"), resultSet.getString("nome"), resultSet.getString("cognome")));
        	} while (resultSet.next());
        	chiudi_connessione(conn);
        	
        }
        partito.set_persone(persone);
	}

	@Override
	public List<PartitoDTO> get_partiti() throws SQLException, ClassNotFoundException {
		Connection conn = connessione_db();
		
	 	String query = 	"SELECT nome, descrizione FROM `partiti` " + 
	 					"WHERE nome !='Nessuno' and nome != 'non_calcolato' "+
	 					"order by nome";
	 	
	 	Statement statement = conn.createStatement();
	 	ResultSet resultSet = statement.executeQuery(query);
       
	 	List<PartitoDTO> partiti = new ArrayList<PartitoDTO>();
        if(resultSet.next()) {
        	do {
        		partiti.add(new PartitoDTO(resultSet.getString("nome"), resultSet.getString("descrizione")));
        	} while (resultSet.next());
        	chiudi_connessione(conn);
        }
        return partiti;
	}
	
	
	@Override
	public boolean aggiungi_partito(GestoreDelSistemaDTO gestore, String nome, String descrizione) throws SQLException, ClassNotFoundException, LogException {
		if(nome.equals("non_calcolato") || nome.equals("no_vincitore"))
			return false;
		
		Connection conn = connessione_db();		
	 	String query = 	"SELECT nome FROM partiti WHERE nome = ?";
	 	
	 	PreparedStatement statement= conn.prepareStatement(query);
        statement.setString(1, nome);
        
        ResultSet resultSet = statement.executeQuery();
        
        if(resultSet.next()) {
        	chiudi_connessione(conn);
        	return false;        	
        }
		
		
        conn = connessione_db();
	 	query = "INSERT INTO `partiti` (`nome`, `descrizione`) VALUES (?, ?);";
	 	
	 	statement= conn.prepareStatement(query);
        
        statement.setString(1, nome);
        statement.setString(2, descrizione);
        
        int result = statement.executeUpdate();
        chiudi_connessione(conn);
        
        if(result>0) {
        	Timestamp now = new Timestamp(System.currentTimeMillis());
	 		auditing.evento_generato_da_DAO(gestore, now, "ha creato un nuovo partito: "+ nome + " | " + descrizione);
        }
	 	
        
        return result>0;
	}

	@Override
	public boolean aggiungi_persona_ad_un_partito(GestoreDelSistemaDTO gestore, String nome, String cognome, PartitoDTO partito) throws SQLException, ClassNotFoundException, LogException {
		Connection conn = connessione_db();	
		
	 	String query = 	"INSERT INTO `persone` (`id`, `nome`, `cognome`, `partito`) VALUES (NULL, ?, ?, '"+partito.nome+"');";
	 	
		PreparedStatement statement= conn.prepareStatement(query);
        
        statement.setString(1, nome);
        statement.setString(2, cognome);
        
        int result = statement.executeUpdate();
        chiudi_connessione(conn);
        
        if(result>0) {
        	Timestamp now = new Timestamp(System.currentTimeMillis());
	 		auditing.evento_generato_da_DAO(gestore, now, "ha aggiunto la persona " + nome + ", "+ cognome + " al partito " + partito);
        }
	 	
        
		return result>0;
	}
	
	public PartitoDTO get_partito(String nome) throws SQLException, ClassNotFoundException {
		Connection conn = connessione_db();
		
	 	String query = 	"SELECT nome, descrizione FROM `partiti` " + 
	 					"WHERE nome ='"+nome+"'";
	 	
	 	Statement statement = conn.createStatement();
	 	ResultSet resultSet = statement.executeQuery(query);
	 	PartitoDTO partito = null;
        if(resultSet.next()) {
        	do {
        		partito = new PartitoDTO(resultSet.getString("nome"), resultSet.getString("descrizione"));
        		
        	} while (resultSet.next());
        	chiudi_connessione(conn);
        }
        return partito;
	}
	
	@Override
	public List<PartitoDTO> get_classifica_partiti_candidati(SessioneDiVotazioneDTO sessione) throws ClassNotFoundException, SQLException {
		List<PartitoDTO> result = new ArrayList<PartitoDTO>();
		
		Connection conn = connessione_db();
	 	String query = "SELECT partito FROM `voti_sessione_di_voto` WHERE id_sessione_voto = " + sessione.id + " ORDER BY n_voti, partito";
	 	Statement statement = conn.createStatement();
	 	ResultSet resultSet = statement.executeQuery(query);
	 	
	 	if(resultSet.next()) {
        	do {
        		PartitoDTO partito = get_partito(resultSet.getString("partito"));
        		if(partito !=null)
        			result.add(partito);
        	} while (resultSet.next());
        	chiudi_connessione(conn);
        }
	 	
		
		return result;
	}

	

}
