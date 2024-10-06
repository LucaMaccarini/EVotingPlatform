package progetto_java_e_voting_plat;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ReferendumController implements Initializable {
    
	@FXML
	private Button bt_no;

	@FXML
	private Button bt_si;
	    
	@FXML
    private Label lb_data;

	@FXML
    private Label lb_voto;
	
    @FXML
    private Label lb_quesito;
    
    @FXML
    private Label lb_mod_vincitore;

    @FXML
    private Label lb_username_proprietario;
    
    @FXML
    private Button bt_astenuto;
    
    private ReferendumDTO referendum;
    
    private UtenteDTO utente;
    
    private ElettoreDAO elettore_DAO = ElettoreDAOImplementation.get_singleton_instance();
    private GestoreDelSistemaDAO gestore_del_sistema_DAO = GestoreDelSistemaDAOImplementation.get_singleton_instance();
    
    public ReferendumController(UtenteDTO utente, ReferendumDTO referendum) {
    	this.utente = utente;
    	this.referendum=referendum;    	
    }
  
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		
    	lb_username_proprietario.setText(referendum.proprietario);
    	lb_data.setText(referendum.get_formatted_data_string());
    	lb_quesito.setText(referendum.quesito);
    	switch (referendum.modalita_vincitore) {
    		case referendum_con_quorum:
    			lb_mod_vincitore.setText("referendum con quorum");break;
    		case referendum_senza_quorum:
    			lb_mod_vincitore.setText("referendum senza quorum");break;
    		
    	}
    	
    	if(utente instanceof ElettoreDTO) {
	    	Timestamp now = new Timestamp(System.currentTimeMillis());
	    	if(now.after(referendum.data_e_ora_termine)) {
	    		disabilita_pulsanti_voto("Sessione di voto terminata!");
	    	}
    	}else if(utente instanceof GestoreDelSistemaDTO && referendum.modalita_vincitore == modalita_calcolo_vincitore_referendum.referendum_senza_quorum) {
    		bt_astenuto.setDisable(true);
    	}
	    		
    	try {
			if(utente instanceof ElettoreDTO && elettore_DAO.check_elettore_ha_gia_votato(utente.username, referendum)) {
				disabilita_pulsanti_voto("Voto già espresso!");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
	}
    
    private void disabilita_pulsanti_voto(String messaggio) {
    	bt_si.setDisable(true);
		bt_no.setDisable(true);
		bt_astenuto.setDisable(true);
		lb_voto.setText(messaggio);
		lb_voto.setTextFill(Color.RED);
    }

    @FXML
    void click_voto_no(ActionEvent event) throws ClassNotFoundException, SQLException, IOException, LogException {
    	if(utente instanceof ElettoreDTO) {
	    	Timestamp now = new Timestamp(System.currentTimeMillis());
	    	if(now.after(referendum.data_e_ora_termine)) {
	    		disabilita_pulsanti_voto("Sessione di voto terminata!");
	    		Alert alert = new Alert(AlertType.ERROR);
    	    	alert.setTitle("Voto");
    	    	alert.setHeaderText("Referendum terminato");
        		alert.setContentText("Non è possiobile registrare nuovi voti");
        		alert.show();
	    		return;
	    	}
    	}
    	vota(false);
    	if(utente instanceof ElettoreDTO) {
	    	Stage this_stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
	    	this_stage.close();
    	}
    }

    @FXML
    void click_voto_si(ActionEvent event) throws ClassNotFoundException, SQLException, IOException, LogException {
    	if(utente instanceof ElettoreDTO) {
	    	Timestamp now = new Timestamp(System.currentTimeMillis());
	    	if(now.after(referendum.data_e_ora_termine)) {
	    		disabilita_pulsanti_voto("Sessione di voto terminata!");
	    		Alert alert = new Alert(AlertType.ERROR);
    	    	alert.setTitle("Voto");
    	    	alert.setHeaderText("Referendum terminato");
        		alert.setContentText("Non è possiobile registrare nuovi voti");
        		alert.show();
	    		return;
	    	}
    	}
    	vota(true);
    	if(utente instanceof ElettoreDTO) {
	    	Stage this_stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
	    	this_stage.close();
    	}
    }
    
    //scelta==true il voto è si altrimenti no
    private void vota(boolean scelta) throws ClassNotFoundException, SQLException, IOException, LogException {
    	if(utente instanceof ElettoreDTO) {
    		Timestamp now = new Timestamp(System.currentTimeMillis());
        	if(now.after(referendum.data_e_ora_termine)) {
        		Alert alert = new Alert(AlertType.ERROR);
    	    	alert.setTitle("Voto");
    	    	alert.setHeaderText("Sessione di voto terminata");
        		alert.setContentText("non è possiobile registrare nuovi voti");
        		alert.showAndWait();
        		disabilita_pulsanti_voto("Sessione di voto terminata!");
        		return;
        	}
    	}
    	
    	if(utente instanceof ElettoreDTO) {
	    	ElettoreDTO elettore = (ElettoreDTO) utente;
	    	boolean result = elettore_DAO.vota_referendum(elettore, referendum, scelta);
	    	
	    	Alert alert = new Alert(AlertType.INFORMATION);
	    	alert.setTitle("Voto");
			if(result) {
				disabilita_pulsanti_voto("Voto già espresso!");
		    	alert.setHeaderText("Voto effettuato");
	    		alert.setContentText("Il tuo voto è stato registrato (in forma anonima)");    		
	    		alert.showAndWait();
			}else {
				alert.setAlertType(AlertType.ERROR);
		    	alert.setHeaderText("Voto non effettuato");
	    		alert.setContentText("Non è possibile registrare il voto");
	    		alert.showAndWait();
			}
    	}else if (utente instanceof GestoreDelSistemaDTO) {
    		boolean result = gestore_del_sistema_DAO.registra_voto_referendum((GestoreDelSistemaDTO)utente, referendum, scelta);
    		Alert alert = new Alert(AlertType.INFORMATION);
    		if(result) {
		    	alert.setHeaderText("Voto registrato");		
	    		
			}else {
				alert.setAlertType(AlertType.ERROR);
		    	alert.setHeaderText("Voto non registrato");
			}
    		alert.show();
    	}
    }
    
    @FXML
    void click_astenuto(ActionEvent event) throws ClassNotFoundException, SQLException {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Registrazione voto");
    	alert.setContentText("Il voto è stato registrato");
    	
    	if(utente instanceof ElettoreDTO) {
    		elettore_DAO.segna_che_elettore_ha_votato(utente.username, referendum);
    		disabilita_pulsanti_voto("Voto già espresso!");
    	}else if(utente instanceof GestoreDelSistemaDTO && referendum.modalita_vincitore == modalita_calcolo_vincitore_referendum.referendum_con_quorum) {
    		if(!gestore_del_sistema_DAO.registra_astenuto(referendum)) {
    			alert.setAlertType(AlertType.ERROR);
    			alert.setContentText("Non è possibile registrare il voto");
    		}
    	}
	    	
    	
    	alert.show();
    }

	
}
