package progetto_java_e_voting_plat;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class PersoneDelPartitoController implements Initializable {
	@FXML
	private ListView<PersonaDTO> listView_parsone;
	
	@FXML
    private Label tx_nome_partito;

	private PartitoDTO partito;
	
	public PersoneDelPartitoController(PartitoDTO partito) throws ClassNotFoundException, SQLException {
		this.partito=partito;
		PartitoDAO partito_DAO = PartitoDAOImplementation.get_singleton_instance();
		partito_DAO.scarica_e_aggiungi_persone_del_partito(partito);
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		tx_nome_partito.setText(partito.nome);
		listView_parsone.setItems(FXCollections.observableArrayList(partito.get_persone()));
	}
}
