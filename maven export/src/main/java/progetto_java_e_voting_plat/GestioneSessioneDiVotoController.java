package progetto_java_e_voting_plat;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GestioneSessioneDiVotoController implements Initializable {

	
    @FXML
    private Label lb_data;

    @FXML
    private Label lb_descrizione;

    @FXML
    private Label lb_mod_vincitore;

    @FXML
    private Label lb_tipo;

    @FXML
    private Label lb_username_proprietario;

    @FXML
    private TextField tx_username_elettore_controlla;

    @FXML
    private TextField tx_username_elettore_registra;
    
    @FXML
    private Button bt_calcola_vincitore;

    
    @FXML
    private Button bt_registra;
    
    @FXML
    private Button bt_controlla;
  
    private SessioneDiVotoDTO sessione; 
    private ElettoreDAO elettore_DAO;
    private GestoreDelSistemaDTO gestore_del_sistema_DTO;
    private SessioneDiVotoDAO sessione_di_voto_DAO;
    
    public GestioneSessioneDiVotoController(GestoreDelSistemaDTO gestore_del_sistema_DTO, SessioneDiVotoDTO sessione) {
    	elettore_DAO = ElettoreDAOImplementation.get_singleton_instance();
    	sessione_di_voto_DAO = SessioneDiVotoDAOImplementation.get_singleton_instance();
    	this.gestore_del_sistema_DTO=gestore_del_sistema_DTO;
    	this.sessione=sessione; 
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lb_data.setText(sessione.get_formatted_data_string());
		lb_username_proprietario.setText(sessione.proprietario);
		
		if(sessione instanceof ReferendumDTO)	{
			ReferendumDTO la_sessione = (ReferendumDTO)sessione;
			lb_tipo.setText("Referendum");
			lb_descrizione.setText(la_sessione.quesito);
			
			switch(la_sessione.modalita_vincitore) {
	    		case referendum_senza_quorum: 
	    			lb_mod_vincitore.setText("senza quorum");
	    			break;
	    		case referendum_con_quorum:
	    			lb_mod_vincitore.setText("con quorum");
	    			break;
			}
			
		}else if(sessione instanceof SessioneDiVotazioneDTO) {
			SessioneDiVotazioneDTO la_sessione = (SessioneDiVotazioneDTO)sessione;
			lb_descrizione.setText(la_sessione.descrizione);
			switch(la_sessione.modalita_voto) {
	    		case voto_categorico: 
	    			lb_tipo.setText("Voto categorico");
	    			break;
	    		case voto_ordinale:
	    			lb_tipo.setText("Voto ordinale");
	    			break;
	    		case voto_categorico_con_preferenze: 
	    			lb_tipo.setText("Voto categorico con preferenze");
	    			break;
			}
			
			switch(la_sessione.modalita_vincitore) {
	    		case maggioranza: 
	    			lb_mod_vincitore.setText("Maggioranza");
	    			break;
	    		case maggioranza_assoluta:
	    			lb_mod_vincitore.setText("Maggioranza assoluta");
	    			break;
			}
		}
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if(now.after(sessione.data_e_ora_termine)) {
			bt_registra.setDisable(true);
			bt_controlla.setDisable(true);
		}else{
			bt_calcola_vincitore.setDisable(true);
		}
			
		
    	
	}
	
	@FXML
    void registra_voto_elettore(ActionEvent event) {
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if(now.after(sessione.data_e_ora_termine)) {
			bt_registra.setDisable(true);
			bt_controlla.setDisable(true);
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Registrazione voto");
			alert.setContentText("Non è possibile registrare altri voti in quanto la sessione di votazione è terminata");
			alert.show();
			return;
		}
		
		String username = tx_username_elettore_registra.getText();
		Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Registrazione voto");
		try {
			elettore_DAO.segna_che_elettore_ha_votato(username, sessione);
		} catch (Exception e) {
			alert.setAlertType(AlertType.ERROR);
			alert.setHeaderText("Voto non registrato");
			alert.setContentText(e.getMessage());
			alert.show();
			return;
		}
		alert.setHeaderText("Voto registrato");
		alert.setContentText("Il voto dell'elettore " + username + " è stato registrato");
		alert.show();
    }
	
	@FXML
    void controlla_elettore(ActionEvent event) throws ClassNotFoundException, SQLException {
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if(now.after(sessione.data_e_ora_termine)) {
			bt_registra.setDisable(true);
			bt_controlla.setDisable(true);
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Controllo elettore");
			alert.setContentText("Non è possibile controllare l'elettore perchè la sessione di voto è terminata");
			alert.show();
			return;
		}
		
		String username = tx_username_elettore_controlla.getText();
		boolean diritti = elettore_DAO.check_elettore_ha_diritto_di_voto(username, sessione);
		boolean ha_votato= elettore_DAO.check_elettore_ha_gia_votato(username, sessione);
		Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Controllo elettore");
    	
		if(diritti && !ha_votato) {
			alert.setHeaderText("Controllo elettore superato");
			alert.setContentText("l'elettore " + username + " ha diritto di voto e non ha ancora espresso un voto per questa sessione di voto");
			alert.show();
			return;
		}
		
		alert.setAlertType(AlertType.ERROR);
		alert.setHeaderText("Controllo elettore non superato");
		if(!diritti) {
			alert.setContentText("l'elettore " + username + " non ha diritto di voto per questa sessione di voto");
			alert.show();
			return;
		}
		
		if(ha_votato) {
			alert.setContentText("l'elettore " + username + " ha diritto di voto per questa sessione di voto ma ha già espresso un voto");
			alert.show();
		}
				
    }
	
	@FXML
    void inserisci_voti_in_presenza(ActionEvent event) throws ClassNotFoundException, SQLException {
		Alert alert = new Alert(AlertType.ERROR);
		if(sessione_di_voto_DAO.check_vincitore_gia_calcolato(sessione)) {
	    	alert.setTitle("Calcolo vincitore");
	    	alert.setHeaderText("un altro gestore del sistema ha già avviato il calcolo del vincitore");
			alert.setContentText("");
			alert.showAndWait();
			Stage this_stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
			this_stage.hide();
			return;
		}
		
        Stage stage_votazione = new Stage();
        stage_votazione.setResizable(false);
        stage_votazione.initModality(Modality.WINDOW_MODAL);
        stage_votazione.initOwner(((Node)event.getSource()).getScene().getWindow() );
        Parent root;
        if(sessione instanceof ReferendumDTO) {
	           stage_votazione.setTitle("Votazione Referendum: " + sessione.nome);
	           FXMLLoader loader = new FXMLLoader(getClass().getResource("schermata_referendum.fxml"));
	           ReferendumController controller = new ReferendumController(gestore_del_sistema_DTO, (ReferendumDTO) sessione);
	           loader.setController(controller);
				try {
					root = loader.load();
					stage_votazione.setScene(new Scene(root, 575, 513));
				} catch (IOException e) {
					e.printStackTrace();
				}
				 stage_votazione.show();
        }
        else if (sessione instanceof SessioneDiVotazioneDTO)
        {
     	   SessioneDiVotazioneDTO item = (SessioneDiVotazioneDTO) sessione;
     	   stage_votazione.setTitle("Votazione: " + sessione.nome);
	           FXMLLoader loader = new FXMLLoader(getClass().getResource("schermata_voto_sessione_di_votazione.fxml"));
	           VotoSessioneDiVotazioneController controller;
	           try {
					controller = new VotoSessioneDiVotazioneController(gestore_del_sistema_DTO, item);
					loader.setController(controller);
					root = loader.load();
					switch(item.modalita_voto) {
						case voto_ordinale: stage_votazione.setScene(new Scene(root, 424, 707)); break;
						case voto_categorico: stage_votazione.setScene(new Scene(root, 395, 707)); break;
						case voto_categorico_con_preferenze: stage_votazione.setScene(new Scene(root, 802, 707)); break;
					}
					
					stage_votazione.show();							 
				} catch (Exception e) {
					e.printStackTrace();
				}
				
        }
    }
	
	@FXML
    void calcola_il_vincitore(ActionEvent event) throws ClassNotFoundException, SQLException, IOException, LogException {
		Alert alert = new Alert(AlertType.ERROR);
		if(sessione_di_voto_DAO.check_vincitore_gia_calcolato(sessione)) {
	    	alert.setTitle("Calcolo vincitore");
	    	alert.setHeaderText("un altro gestore del sistema ha già avviato il calcolo del vincitore");
			alert.setContentText("");
			
		}else {
		
		sessione_di_voto_DAO.calcola_vincitore(gestore_del_sistema_DTO, sessione);
		alert.setAlertType(AlertType.INFORMATION);
    	alert.setTitle("Calcolo vincitore");
    	alert.setHeaderText("Il vincitore è stato calcolato");
		alert.setContentText("");
		}
		
		alert.showAndWait();
		Stage this_stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
		this_stage.hide();
	}
}
