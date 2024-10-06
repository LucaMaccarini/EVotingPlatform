package progetto_java_e_voting_plat;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ElettoreHomeController extends azioni_base_account_controller implements Initializable {

	private Stage login_stage;

    @FXML
    private Label lb_username;
    
    @FXML
    private Button bt_disconnetti;
    
    @FXML
    private ListView<SessioneDiVotoDTO> listView_sessioni_di_voto;
    
    @FXML
    private ListView<SessioneDiVotoDTO> listView_sessioni_di_voto_vincitori;
    
    @FXML
    private PasswordField tx_modifica_pass_attuale;

    @FXML
    private PasswordField tx_modifica_pass_nuova;

    @FXML
    private PasswordField tx_modifica_pass_ripeti;
    
    private SessioneDiVotoDAO sessione_di_voto_DAO;
    
    private ElettoreDTO elettore;
    
    public ElettoreHomeController(ElettoreDTO elettore, Stage login_stage) {
    	this.login_stage = login_stage;
    	this.elettore = elettore;
    	sessione_di_voto_DAO = SessioneDiVotoDAOImplementation.get_singleton_instance();
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lb_username.setText(elettore.username);
		listView_sessioni_di_voto.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent click) {

		        if (click.getClickCount() == 2) {
		           SessioneDiVotoDTO currentItemSelected = listView_sessioni_di_voto.getSelectionModel().getSelectedItem();
		           
		           Stage stage_votazione = new Stage();
		           stage_votazione.setResizable(false);
		           stage_votazione.initModality(Modality.WINDOW_MODAL);
		           stage_votazione.initOwner(((Node)click.getSource()).getScene().getWindow() );
		           Parent root;
		           if(currentItemSelected instanceof ReferendumDTO) {
			           stage_votazione.setTitle("Votazione Referendum: " + currentItemSelected.nome);
			           FXMLLoader loader = new FXMLLoader(getClass().getResource("schermata_referendum.fxml"));
			           ReferendumController controller = new ReferendumController(elettore, (ReferendumDTO) currentItemSelected);
			           loader.setController(controller);
						try {
							root = loader.load();
							stage_votazione.setScene(new Scene(root, 575, 513));
						} catch (IOException e) {
							e.printStackTrace();
						}
						 stage_votazione.show();
		           }
		           else if (currentItemSelected instanceof SessioneDiVotazioneDTO)
		           {
		        	   SessioneDiVotazioneDTO item = (SessioneDiVotazioneDTO) currentItemSelected;
		        	   stage_votazione.setTitle("Votazione: " + currentItemSelected.nome);
			           FXMLLoader loader = new FXMLLoader(getClass().getResource("schermata_voto_sessione_di_votazione.fxml"));
			           VotoSessioneDiVotazioneController controller;
			           try {
							controller = new VotoSessioneDiVotazioneController(elettore, (SessioneDiVotazioneDTO) currentItemSelected);
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
		    }
		});
		
		listView_sessioni_di_voto_vincitori.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent click) {
		        if (click.getClickCount() == 2) {
		        	SessioneDiVotoDTO currentItemSelected = listView_sessioni_di_voto_vincitori.getSelectionModel().getSelectedItem();
		        	try {
						apri_schermata_info_vincitore(currentItemSelected);
					} catch (Exception e) {
						
						e.printStackTrace();
					}
		        }
		    }
		});
	
	}

    
    

    @FXML
    private void disconnectAccount(ActionEvent event) throws IOException {
    	Stage this_stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
    	disconnectAccount(login_stage, this_stage); 
    }
    
    @FXML
    private void elimina_account(ActionEvent event) throws IOException, ClassNotFoundException, SQLException, LogException {
    	Stage this_stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
    	ElettoreDAO elettore_DAO = ElettoreDAOImplementation.get_singleton_instance();
    	String username = elettore.username;
    	elettore = null;
    	
    	if(elettore_DAO.delete_account(username)) {
	    	Alert alert = new Alert(AlertType.INFORMATION);
	    	alert.setTitle("Account");
	    	alert.setHeaderText("Account Eliminato");
			alert.setContentText("L'account è stato eliminato con successo");
			alert.showAndWait();
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("Account");
	    	alert.setHeaderText("Account non Eliminato");
			alert.setContentText("L'account è non è stato eliminato");
			alert.showAndWait();
    	}
    	disconnectAccount(login_stage, this_stage);  
    }
    
    @FXML
    private void fill_sessioni_di_voto_listview(ActionEvent event) throws ClassNotFoundException, SQLException {
    	ObservableList<SessioneDiVotoDTO> sessioni =   FXCollections.observableArrayList(sessione_di_voto_DAO.get_sessioni_di_voto_attive_con_diritto_di_voto(elettore));
    	listView_sessioni_di_voto.getItems().clear();
    	listView_sessioni_di_voto.setItems(sessioni);   
    }
  
    
    @FXML
    private void fill_sessioni_di_voto_vincitori_listview(ActionEvent event) throws ClassNotFoundException, SQLException { 
    	riempi_sessioni_di_voto_vincitori_listview(listView_sessioni_di_voto_vincitori);
    }
    
    @FXML
    private void modifica_password(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
    	modifica_password(elettore, tx_modifica_pass_attuale, tx_modifica_pass_nuova, tx_modifica_pass_ripeti);
    }
    

}
