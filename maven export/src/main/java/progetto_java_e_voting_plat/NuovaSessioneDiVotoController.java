package progetto_java_e_voting_plat;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class NuovaSessioneDiVotoController implements Initializable {
	@FXML
    private ComboBox<modalita_calcolo_vincitore_referendum> cb_modalita_calcolo_vincitore_referendum;
    
    @FXML
    private ComboBox<modalita_calcolo_vincitore_sessione_di_votazione> cb_modalita_calcolo_vincitore_votazione;
    
    @FXML
    private ComboBox<modalita_voto> cb_modalita_di_voto;

    @FXML
    private DatePicker dt_data_termine;

    @FXML
    private TextField tx_nome;

    @FXML
    private TextField tx_ora_termine;
    
    @FXML
    private TextField tx_minuti_termine;
    
    @FXML
    private Label lb_descrizione_o_quesito;

    @FXML
    private TextArea tx_quesito_o_descrizione;
    
    @FXML
    private ListView<ElettoreDTO> listview_elettori;
    
    @FXML
    private ListView<PartitoDTO> listview_partiti;
    
    private ObservableList<ElettoreDTO> elettori;
    private ObservableList<PartitoDTO> partiti;
    private boolean is_votazione;
    private GestoreDelSistemaDTO gestore;
    private SessioneDiVotoDAO sessione_di_voto_DAO;
    
    public NuovaSessioneDiVotoController(GestoreDelSistemaDTO gestore, boolean is_votazione) throws ClassNotFoundException, SQLException {
    	this.is_votazione=is_votazione;
    	this.gestore = gestore;
    	ElettoreDAO elettore_DAO = ElettoreDAOImplementation.get_singleton_instance();
    	PartitoDAO partito_DAO = PartitoDAOImplementation.get_singleton_instance();
    	sessione_di_voto_DAO = SessioneDiVotoDAOImplementation.get_singleton_instance();
    	
    	elettori = FXCollections.observableArrayList(elettore_DAO.get_elettori());
    	if(is_votazione)
    		partiti = FXCollections.observableArrayList(partito_DAO.get_partiti());
    	
    	
    }
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		listview_elettori.setItems(elettori);
		if(is_votazione)
			listview_partiti.setItems(partiti);
		listview_elettori.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		if(is_votazione){
			cb_modalita_calcolo_vincitore_votazione.getItems().addAll(modalita_calcolo_vincitore_sessione_di_votazione.maggioranza, modalita_calcolo_vincitore_sessione_di_votazione.maggioranza_assoluta);
			cb_modalita_di_voto.getItems().addAll(modalita_voto.voto_categorico, modalita_voto.voto_categorico_con_preferenze, modalita_voto.voto_ordinale);
			listview_partiti.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		}
		else{
			cb_modalita_calcolo_vincitore_votazione.setVisible(false);
			cb_modalita_di_voto.setVisible(false);
			lb_descrizione_o_quesito.setText("Quesito");
			cb_modalita_calcolo_vincitore_referendum.setVisible(true);
			cb_modalita_calcolo_vincitore_referendum.getItems().addAll(modalita_calcolo_vincitore_referendum.referendum_senza_quorum, modalita_calcolo_vincitore_referendum.referendum_con_quorum);
			
		}
		
		
	}
    
    @FXML
    void registra_nuova_sessione_di_voto(ActionEvent event) throws ClassNotFoundException, SQLException {
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle("nuova sessione di voto");
   	
    	String nome = tx_nome.getText();
    	if(nome.length()==0 || nome.length()>30) {
    		alert.setHeaderText("Campo Nome errato");
    		alert.setContentText("Il campo nome non può essere vuoto o più lungo di 30 caratteri");
    		alert.show();
    		return;
    	}
    	
    	String descrizione_o_quesito = tx_quesito_o_descrizione.getText();
    	
    	
    	LocalDate data = dt_data_termine.getValue();
    	
    	if(data==null) {
    		alert.setHeaderText("Campo Data errato");
    		alert.setContentText("Il campo data non è stato inserito");
    		alert.show();
    		return;
    	}
    	
    	
    	String stringa_ore = tx_ora_termine.getText();
    	String stringa_minuti = tx_minuti_termine.getText();
    	if(stringa_ore.length()==0 || stringa_minuti.length()==0) {
    		alert.setHeaderText("Campo Orario errato");
    		alert.setContentText("Il campo delle ore o minuti non può essere vuoto");
    		alert.show();
    		return;
    	}
    	
    	LocalDateTime data_termine=null;
    	try {
	    	int ore = Integer.parseInt(stringa_ore);
	    	int min = Integer.parseInt(stringa_minuti);
	    	data_termine = data.atTime(ore, min);
    	}catch(Exception e) {
    		alert.setHeaderText("Campo Orario incompatibile");
    		alert.setContentText("il campo orario è stato inserito in un formato errato");
    		alert.show();
    		return;
    	}
    	
    	
    	Timestamp time_stamp_data_termine = Timestamp.valueOf(data_termine);
    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	
    	if(time_stamp_data_termine.before(now)) {
    		alert.setHeaderText("Data e ora già passati");
    		alert.setContentText("inserire una data e un orario successivi alla data ed ora attuali");
    		alert.show();
    		return;
    	}
    	
    	
    	
    	
    	List<ElettoreDTO> elettori_con_diritto =  listview_elettori.getSelectionModel().getSelectedItems();
    	if(elettori_con_diritto.size()==0) {
    		alert.setHeaderText("Elettori aventi il diritto di voto non selezionati");
    		alert.setContentText("selezionare almeno un eletore avente il diritto di voto");
    		alert.show();
    		return;    		
    	}
    	
    	
    	if(is_votazione) {
    		if(descrizione_o_quesito.length()>300) {
        		alert.setHeaderText("Campo Descrizione errato");
        		alert.setContentText("Il campo descrizione non può essere lungo di 300 caratteri");
        		alert.show();
        		return;
        	}
    		
    		modalita_calcolo_vincitore_sessione_di_votazione mod_vincitore = cb_modalita_calcolo_vincitore_votazione.getValue();
    		if(mod_vincitore == null) {
    			alert.setHeaderText("Modalità di calcolo del vincitore non inserita");
        		alert.setContentText("selezionare una Modalità di calcolo del vincitore");
        		alert.show();
        		return;
    		}
    		
    		modalita_voto mod_voto = cb_modalita_di_voto.getValue();
    		if(mod_voto == null) {
    			alert.setHeaderText("Modalità di voto non inserita");
        		alert.setContentText("selezionare una modalità di voto");
        		alert.show();
        		return;
    		}
    		
    		
    		List<PartitoDTO> partiti_candidati =  listview_partiti.getSelectionModel().getSelectedItems();
        	if(partiti_candidati.size()==0) {
        		alert.setHeaderText("Candidati non selezionati");
        		alert.setContentText("selezionare almeno un candidato");
        		alert.show();
        		return;    		
        	}
    		
    		
        	try {
	        	sessione_di_voto_DAO.registra_nuova_sessione_di_votazione(nome, descrizione_o_quesito, gestore, mod_voto, mod_vincitore, time_stamp_data_termine, elettori_con_diritto, partiti_candidati);
	        	alert.setAlertType(AlertType.INFORMATION);
	        	alert.setHeaderText("votazione registrata");
	        	clear_campi();
        	}catch(Exception e){
    			alert.setAlertType(AlertType.ERROR);
    			alert.setHeaderText("Votazione non registrata");
    		}
    	}else {
    		
        	if(descrizione_o_quesito.length()==0 || descrizione_o_quesito.length()>300) {
        		alert.setHeaderText("Campo quesito errato");
        		alert.setContentText("Il campo quesito non può essere vuoto o più lungo di 300 caratteri");
        		alert.show();
        		return;
        	}
        	
    		modalita_calcolo_vincitore_referendum mod_vincitore = cb_modalita_calcolo_vincitore_referendum.getValue();
    		if(mod_vincitore == null) {
    			alert.setHeaderText("Modalità di calcolo del vincitore non inserita");
        		alert.setContentText("selezionare una Modalità di calcolo del vincitore");
        		alert.show();
        		return;
    		}    
    		
    		
    		
    		try {
    			sessione_di_voto_DAO.registra_nuovo_referendum(nome, descrizione_o_quesito, gestore, mod_vincitore, time_stamp_data_termine, elettori_con_diritto);
    			alert.setAlertType(AlertType.INFORMATION);
    			alert.setHeaderText("referendum registrato");
    			clear_campi();
    		}catch(Exception e){
    			alert.setAlertType(AlertType.ERROR);
    			alert.setHeaderText("Referendum non registrato");
    		}
    	}
    	
		alert.show();
    	
    }
    
    private void clear_campi() {
    	tx_nome.clear();
    	tx_quesito_o_descrizione.clear();
    	cb_modalita_calcolo_vincitore_votazione.setValue(null);
    	cb_modalita_di_voto.setValue(null);
    	dt_data_termine.setValue(null);
    	tx_ora_termine.clear();
    	tx_minuti_termine.clear();
    	listview_elettori.getSelectionModel().clearSelection();
    	listview_partiti.getSelectionModel().clearSelection();
    	cb_modalita_calcolo_vincitore_referendum.setValue(null);
    }

	
}
