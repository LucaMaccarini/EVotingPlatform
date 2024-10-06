package progetto_java_e_voting_plat;


import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class VotoSessioneDiVotazioneController implements Initializable {
	
	@FXML
    private Button bt_ordinale_giu;

    @FXML
    private Button bt_ordinale_su;

    @FXML
    private Button bt_preferenze_giu;

    @FXML
    private Button bt_preferenze_su;
    
    @FXML
    private Button bt_vota;

    @FXML
    private Label lb_data;
    
    @FXML
    private Label lb_info;

    @FXML
    private Label lb_preferenze;

    @FXML
    private Label lb_tipo;

    @FXML
    private Label lb_username_proprietario;

    @FXML
    private Label lb_voto;

    @FXML
    private ListView<PersonaDTO> listView_preferenze;

    @FXML
    private ListView<PartitoDTO> listView_partiti;
    
    @FXML
    private Label lb_descrizione;
    
    @FXML
    private Button bt_astenuto;
    
    @FXML
    private Label lb_mod_vincitore;
    
    private SessioneDiVotazioneDTO sessione_di_votazione;
    
    private UtenteDTO utente;
    
    private ElettoreDAO elettore_DAO = ElettoreDAOImplementation.get_singleton_instance();
    private SessioneDiVotoDAO sessione_di_voto_DAO = SessioneDiVotoDAOImplementation.get_singleton_instance();
    private PartitoDAO partito_DAO = PartitoDAOImplementation.get_singleton_instance();
    private PartitoDTO partito_selezionato;
    GestoreDelSistemaDAO gestore_del_sistema_DAO = GestoreDelSistemaDAOImplementation.get_singleton_instance();
    
    public VotoSessioneDiVotazioneController(UtenteDTO utente, SessioneDiVotazioneDTO sessione_di_votazione) throws ClassNotFoundException, SQLException {
    	this.utente = utente;
    	if(sessione_di_votazione.get_partiti_candidati() == null)
    		sessione_di_voto_DAO.scarica_e_aggiungi_partiti_candidati(sessione_di_votazione);
    	this.sessione_di_votazione=sessione_di_votazione; 
    	partito_selezionato = null;
    }
    
    
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	lb_username_proprietario.setText(sessione_di_votazione.proprietario);
    	lb_data.setText(sessione_di_votazione.get_formatted_data_string());
    	lb_descrizione.setText(sessione_di_votazione.descrizione);
    	
    	switch(sessione_di_votazione.modalita_vincitore) {
    		case maggioranza: lb_mod_vincitore.setText("maggioranza");break;
    		case maggioranza_assoluta: lb_mod_vincitore.setText("maggioranza assoluta");break;
    	}
    	
    	switch(sessione_di_votazione.modalita_voto) {
    		case voto_categorico: 
    			lb_tipo.setText("Voto categorico");
    			lb_info.setText("Seleziona il partito che vuoi votare e clicca il pulsante vota");
    			break;
    		case voto_ordinale:
    			lb_info.setText("Riordina i seguenti partiti e clicca il pulsante vota");
    			lb_tipo.setText("Voto ordinale");
    			bt_ordinale_giu.setVisible(true);
    			bt_ordinale_su.setVisible(true);
    			break;
    		case voto_categorico_con_preferenze: 
    			lb_info.setText("Seleziona il partito che vuoi votare, riordina le persone dal partito ed infine clicca il pulsante vota");
    			lb_tipo.setText("Voto categorico con preferenze");
    			lb_preferenze.setVisible(true);
    			listView_preferenze.setVisible(true);
    			lb_descrizione.setPrefWidth(736);
    			
    			listView_partiti.setOnMouseClicked(new EventHandler<MouseEvent>() {
    			    @Override
    			    public void handle(MouseEvent click) {
    			    	PartitoDTO currentItemSelected = listView_partiti.getSelectionModel().getSelectedItem();
    			    	if(partito_selezionato != currentItemSelected) {
    			    		if(currentItemSelected.get_persone() == null) {
    							try {
    								partito_DAO.scarica_e_aggiungi_persone_del_partito(currentItemSelected);
    							} catch (ClassNotFoundException | SQLException e) {
    							
    								e.printStackTrace();
    							}
        			    	}
    			    		
	    			    	ObservableList<PersonaDTO> persone = FXCollections.observableArrayList(currentItemSelected.get_persone());
	    			    	listView_preferenze.getItems().clear();
	    			    	listView_preferenze.setItems(persone);
	    			    	partito_selezionato = currentItemSelected;
    			    	}

    			    }
    			});
    			
    			break;
    	}
    	
    	if(utente instanceof ElettoreDTO) {
	    	Timestamp now = new Timestamp(System.currentTimeMillis());
	    	if(now.after(sessione_di_votazione.data_e_ora_termine)) {
	    		disabilita_pulsanti_voto("Sessione di voto terminata!");
	    	}
	    	
	    	try {
				if(elettore_DAO.check_elettore_ha_gia_votato(utente.username, sessione_di_votazione))
					disabilita_pulsanti_voto("Voto già espresso!");
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
    	}else if(utente instanceof GestoreDelSistemaDTO) {
    		if(sessione_di_votazione.modalita_vincitore != modalita_calcolo_vincitore_sessione_di_votazione.maggioranza_assoluta)
    			bt_astenuto.setDisable(true);
    	}
  
		ObservableList<PartitoDTO> partiti = FXCollections.observableArrayList(sessione_di_votazione.get_partiti_candidati());
		listView_partiti.getItems().clear();
		listView_partiti.setItems(partiti);
		
    	
	}
    
    private void disabilita_pulsanti_voto(String messaggio) {
    	bt_vota.setDisable(true);
        bt_astenuto.setDisable(true);
		lb_voto.setText(messaggio);
		lb_voto.setTextFill(Color.RED);
    }

    //sposta_in_su == true sposto in su altrimenti sposto in giù
    private <T> void sposta_elemento(ObservableList<T> items, int index, boolean sposta_in_su){
    	
    	if(sposta_in_su) {
    		if(index != 0) {
    			var appoggio = items.get(index);
        		items.set(index, items.get(index-1));
        		items.set(index-1, appoggio);
    		}
    	}
    	else {
    		if(index != items.size()-1) {
    			var appoggio = items.get(index);
        		items.set(index, items.get(index+1));
        		items.set(index+1, appoggio);
    		}
    	}

    }
  
    @FXML
    void click_pulsante_giu(ActionEvent event) {
    	if(listView_partiti.getSelectionModel().getSelectedItem()!=null) {
	    	int currentItemSelectedIndex = listView_partiti.getSelectionModel().getSelectedIndex();
	    	ObservableList<PartitoDTO> items = listView_partiti.getItems();
	    	if(currentItemSelectedIndex != items.size()-1) {
	    		sposta_elemento(items, currentItemSelectedIndex, false);
	    		listView_partiti.getSelectionModel().clearAndSelect(currentItemSelectedIndex+1);
	    	}
    	}
    }

    @FXML
    void click_pulsante_preferenze_giu(ActionEvent event) {
    	if(listView_preferenze.getSelectionModel().getSelectedItem()!=null) {
	    	int currentItemSelectedIndex = listView_preferenze.getSelectionModel().getSelectedIndex();
	    	ObservableList<PersonaDTO> items = listView_preferenze.getItems();
	    	if(currentItemSelectedIndex != items.size()-1) {
	    		sposta_elemento(items, currentItemSelectedIndex, false);
	    		listView_preferenze.getSelectionModel().clearAndSelect(currentItemSelectedIndex+1);
	    	}
    	}
    }

    @FXML
    void click_pulsante_preferenze_su(ActionEvent event) {
    	if(listView_preferenze.getSelectionModel().getSelectedItem()!=null) {
	    	int currentItemSelectedIndex = listView_preferenze.getSelectionModel().getSelectedIndex();
	    	ObservableList<PersonaDTO> items = listView_preferenze.getItems();
	    	if(currentItemSelectedIndex != 0) {
	    		sposta_elemento(items, currentItemSelectedIndex, true);
	    		listView_preferenze.getSelectionModel().clearAndSelect(currentItemSelectedIndex-1);
	    	}
    	}
    }

    @FXML
    void click_pulsante_su(ActionEvent event) {
    	if(listView_partiti.getSelectionModel().getSelectedItem()!=null) {
	    	int currentItemSelectedIndex = listView_partiti.getSelectionModel().getSelectedIndex();
	    	ObservableList<PartitoDTO> items = listView_partiti.getItems();
	    	if(currentItemSelectedIndex != 0) {
	    		sposta_elemento(items, currentItemSelectedIndex, true);
	    		listView_partiti.getSelectionModel().clearAndSelect(currentItemSelectedIndex-1);
	    	}
    	}
    	
    }

    @FXML
    void click_vota(ActionEvent event) throws ClassNotFoundException, SQLException, LogException {
    	
    	if(listView_partiti.getSelectionModel().getSelectedItem()==null && sessione_di_votazione.modalita_voto != modalita_voto.voto_ordinale) {
    		Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("Voto");
	    	alert.setHeaderText("Selezione non effettuata");
    		alert.setContentText("Per votare è necessario selezionare un partito");
    		alert.show();
    		return;
    	}
    	
    	
    	
    	if(utente instanceof ElettoreDTO) {
    		Timestamp now = new Timestamp(System.currentTimeMillis());
        	if(now.after(sessione_di_votazione.data_e_ora_termine)) {
        		Alert alert = new Alert(AlertType.ERROR);
    	    	alert.setTitle("Voto");
    	    	alert.setHeaderText("Sessione di voto terminata");
        		alert.setContentText("Non è possiobile registrare nuovi voti");
        		alert.show();
        		disabilita_pulsanti_voto("Sessione di voto terminata!");
        		return;
        	}
        	
    		ElettoreDTO elettore = (ElettoreDTO)utente;
	    	boolean result=false;
	    	switch(sessione_di_votazione.modalita_voto) {
	    		case voto_categorico:
	    			result = elettore_DAO.voto_categorico_sessione_di_votazione(elettore, sessione_di_votazione, listView_partiti.getSelectionModel().getSelectedItem());
	    			break;
	    		case voto_ordinale:
	    			List<PartitoDTO> ordine_partiti= listView_partiti.getItems();
	    			result = elettore_DAO.voto_ordinale_sessione_di_votazione(elettore, sessione_di_votazione, ordine_partiti);
	    			break;
	    		case voto_categorico_con_preferenze:
	    			List<PersonaDTO> ordine_persone= listView_preferenze.getItems();
	    			result = elettore_DAO.voto_categorico_con_preferenze_sessione_di_votazione(elettore, sessione_di_votazione, listView_partiti.getSelectionModel().getSelectedItem(), ordine_persone);
	    			break;
	    	
	    	}
	    	
	    	Alert alert = new Alert(AlertType.INFORMATION);
	    	alert.setTitle("Voto");
			if(result) {
				disabilita_pulsanti_voto("Voto già espresso!");
		    	alert.setHeaderText("Voto effettuato");
	    		alert.setContentText("Il tuo voto è stato registrato (in forma anonima)");    		
	    		alert.showAndWait();
			}else {
				alert.setAlertType(AlertType.ERROR);
		    	alert.setTitle("Voto");
		    	alert.setHeaderText("Voto non effettuato");
	    		alert.setContentText("Non è possibile registrare il voto");
	    		alert.showAndWait();
			}
			Stage this_stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
	    	this_stage.close();
    	}else if (utente instanceof GestoreDelSistemaDTO){
    		GestoreDelSistemaDTO gestore = (GestoreDelSistemaDTO)utente;
    		boolean result=false;
	    	switch(sessione_di_votazione.modalita_voto) {
	    		case voto_categorico:
	    			result = gestore_del_sistema_DAO.registra_voto_categorico_sessione_di_votazione(gestore, sessione_di_votazione, listView_partiti.getSelectionModel().getSelectedItem());
	    			break;
	    		case voto_ordinale:
	    			List<PartitoDTO> ordine_partiti= listView_partiti.getItems();
	    			result = gestore_del_sistema_DAO.registra_voto_ordinale_sessione_di_votazione(gestore, sessione_di_votazione, ordine_partiti);
	    			break;
	    		case voto_categorico_con_preferenze:
	    			List<PersonaDTO> ordine_persone= listView_preferenze.getItems();
	    			result = gestore_del_sistema_DAO.registra_voto_categorico_con_preferenze_sessione_di_votazione(gestore, sessione_di_votazione, listView_partiti.getSelectionModel().getSelectedItem(), ordine_persone);
	    			break;
	    	
	    	}
	    	
	    	Alert alert = new Alert(AlertType.INFORMATION);
	    	alert.setTitle("Registrazione voto");
			if(result) {
		    	alert.setHeaderText("Voto registrato");
	    		alert.setContentText("Il voto è stato registrato");    		
			}else {
				alert.setAlertType(AlertType.ERROR);		    	
		    	alert.setHeaderText("Voto non registrato");
	    		alert.setContentText("Non è possibile registrare il voto");
			}
			alert.show();
    	}
    }
    
    @FXML
    void click_astenuto(ActionEvent event) throws ClassNotFoundException, SQLException {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Registrazione voto");
    	alert.setContentText("Il voto è stato registrato");
    	if(utente instanceof ElettoreDTO ) {
    		elettore_DAO.segna_che_elettore_ha_votato(utente.username, sessione_di_votazione);
    		disabilita_pulsanti_voto("Voto già espresso!");
    	}else if (utente instanceof GestoreDelSistemaDTO && sessione_di_votazione.modalita_vincitore == modalita_calcolo_vincitore_sessione_di_votazione.maggioranza_assoluta) {
    		if(!gestore_del_sistema_DAO.registra_astenuto(sessione_di_votazione)) {
    			alert.setContentText("Non è possibile registrare il voto");
    			alert.setAlertType(AlertType.ERROR);
    		}
    	}
    	alert.show();
    }

}
