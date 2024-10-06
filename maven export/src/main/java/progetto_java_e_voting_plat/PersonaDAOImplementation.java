package progetto_java_e_voting_plat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAOImplementation extends DAOImplementationBasics implements PersonaDAO {
	
	private static PersonaDAOImplementation singleton_instance;
	
	public static PersonaDAOImplementation get_singleton_instance() {
		if(singleton_instance == null)
			singleton_instance = new PersonaDAOImplementation();
		return singleton_instance;
	}
	
	@Override
	public List<PersonaDTO> get_classifica_persone_partito_vincitore(SessioneDiVotazioneDTO sessione) throws SQLException, ClassNotFoundException {
		
		
		List<PersonaDTO> result = new ArrayList<PersonaDTO>();
		Connection conn = connessione_db();
	 	String query = 	"SELECT persone.id, nome, cognome FROM voti_preferenze_sessione_di_voto " + 
			 			"inner join persone on voti_preferenze_sessione_di_voto.id_persona = persone.id " + 
			 			"WHERE id_sessione_di_votazione = "+sessione.id+"  AND partito = '"+sessione.get_vincitore().nome+"' " + 
			 			"ORDER BY n_voti, nome, cognome";
	 	Statement statement = conn.createStatement();
	 	ResultSet resultSet = statement.executeQuery(query);
	 	
	 	if(resultSet.next()) {
        	do {
        		PersonaDTO persona = new PersonaDTO(resultSet.getInt("id"), resultSet.getString("nome"), resultSet.getString("cognome"));
        		result.add(persona);
        	} while (resultSet.next());
        	chiudi_connessione(conn);
        }
	 	
		return result;
	}

}
