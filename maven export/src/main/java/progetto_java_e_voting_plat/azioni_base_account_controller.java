package progetto_java_e_voting_plat;

import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public abstract class azioni_base_account_controller {
	
	private Password password_functions;
	protected MailSender mail_sender;
	
	public azioni_base_account_controller() {
		password_functions = AccountPassword.get_singleton_instance();
		mail_sender = MailSenderImlementation.get_singleton_instance();
	}
	
	 protected void disconnectAccount(Stage login_stage, Stage this_stage) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("login.fxml"));
		loader.load();
		login_stage.show();
		this_stage.hide();
	 }
	 
	 
	 protected void riempi_sessioni_di_voto_vincitori_listview(ListView<SessioneDiVotoDTO> listView_sessioni_di_voto_vincitori) throws ClassNotFoundException, SQLException {
		SessioneDiVotoDAO sessione_di_voto_DAO = SessioneDiVotoDAOImplementation.get_singleton_instance();
		ObservableList<SessioneDiVotoDTO> sessioni =   FXCollections.observableArrayList(sessione_di_voto_DAO.get_sessioni_di_voto_terminate_con_vincitore_calcolato());
		listView_sessioni_di_voto_vincitori.getItems().clear();
		listView_sessioni_di_voto_vincitori.setItems(sessioni);   
	 }
	 
	 protected void modifica_password(UtenteDTO utente, PasswordField tx_modifica_pass_attuale, PasswordField tx_modifica_pass_nuova, PasswordField tx_modifica_pass_ripeti) throws ClassNotFoundException, SQLException, IOException {
		Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Error");
	    if(!password_functions.checkPassword(tx_modifica_pass_attuale.getText())) {
	    	alert.setHeaderText("Password Attuale");    
	    	alert.setContentText("La password attuale non puo' essere di lunghezza inferiore a 6 o maggiore di 20.\nLa password deve avere almeno una lettera maiuscola, almeno un numero e un almeno un carattere speciale");
	    	alert.show();
	    	return;
	    }
	    	
	    if(!password_functions.checkPassword(tx_modifica_pass_nuova.getText())) {
    		alert.setHeaderText("Password Nuova");    
    		alert.setContentText("La password nuova non puo' essere di lunghezza inferiore a 6 o maggiore di 20.\nLa password deve avere almeno una lettera maiuscola, almeno un numero e un almeno un carattere speciale");
    		alert.show();
    		return;
    	}
    	
    	if(!tx_modifica_pass_ripeti.getText().contentEquals(tx_modifica_pass_nuova.getText())) {
    		alert.setHeaderText("Password nuova");    
    		alert.setContentText("La nuova password e la sua ripetizione non coincidono");
    		alert.show();
    		return;
    	}
	    	
    	if(tx_modifica_pass_attuale.getText().contentEquals(tx_modifica_pass_nuova.getText())) {
    		alert.setHeaderText("Password nuova");    
    		alert.setContentText("La nuova password coincide con la password attuale");
    		alert.show();
    		tx_modifica_pass_nuova.clear();
    		tx_modifica_pass_ripeti.clear();
    		return;
    	}
    	
    	UtenteDAO utente_dao = UtenteDAOImplementation.get_singleton_instance();
    	if(utente_dao.modifica_password(utente, tx_modifica_pass_attuale.getText(), tx_modifica_pass_nuova.getText())) {
    		alert.setAlertType(AlertType.INFORMATION);
    		alert.setTitle("Success");
    		alert.setHeaderText("Password");    
    		alert.setContentText("La password è stata modificata");
    		
    		mail_sender.send_async_mail(utente.email, "Password Modificata", "La tua password è stata modificata");
    		
    		tx_modifica_pass_attuale.clear();
    		tx_modifica_pass_nuova.clear();
    		tx_modifica_pass_ripeti.clear();
    	}else {
    		alert.setHeaderText("Password");    
    		alert.setContentText("La password attuale non è corretta");
    		tx_modifica_pass_attuale.clear();
    	}
	    alert.show();

	 }
	 
	 protected void apri_schermata_info_vincitore(SessioneDiVotoDTO sessione) throws ClassNotFoundException, SQLException {
         Stage stage_info_vincitore = new Stage();
         stage_info_vincitore.setResizable(false);
         Parent root;
         stage_info_vincitore.setTitle("Vincitore: " + sessione.nome);
         FXMLLoader loader = new FXMLLoader(getClass().getResource("schermata_info_vincitore.fxml"));
         InfoVincitoreController controller = new InfoVincitoreController(sessione);
         loader.setController(controller);
			try {
				root = loader.load();
				if(sessione instanceof SessioneDiVotazioneDTO ) {
					SessioneDiVotazioneDTO votazione = (SessioneDiVotazioneDTO)sessione;
					
					if( !votazione.get_vincitore().equals("Nessuno") && (votazione.modalita_voto == modalita_voto.voto_ordinale || votazione.modalita_voto == modalita_voto.voto_categorico_con_preferenze))
						stage_info_vincitore.setScene(new Scene(root, 806, 389));
					else
						stage_info_vincitore.setScene(new Scene(root, 449, 389));
				}else {
					stage_info_vincitore.setScene(new Scene(root, 449, 389));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			 stage_info_vincitore.show(); 
	 }
}
