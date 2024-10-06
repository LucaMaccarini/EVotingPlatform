package progetto_java_e_voting_plat;
import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

	private UtenteDAO utente_DAO = UtenteDAOImplementation.get_singleton_instance();
    @FXML
    private Button bt_accedi;

    @FXML
    private Label lb_login;

    @FXML
    private PasswordField tx_password;

    @FXML
    private TextField tx_username;
    
    private Password password_functions = AccountPassword.get_singleton_instance();
    private Password otp_functions = PasswordOTP.get_singletone_instance();
    
    @FXML
    void bt_accedi_click(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
    	String username = tx_username.getText();
    	String password = tx_password.getText();

    	
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle("Error");
    	alert.setHeaderText("Username error");

    	if(username.length() < 6 || username.length() > 30 ) {
    		alert.setContentText("L'username non puo' essere di lunghezza inferiore a 6 o maggiore di 30");
    		alert.show();
    		return;
    	}
    	
    	alert.setHeaderText("Password error");    	    	
    	if(!password_functions.checkPassword(password)) {
    		alert.setContentText("La password non puo' essere di lunghezza inferiore a 6 o maggiore di 20.\nLa password deve avere almeno una lettera maiuscola, almeno un numero e un almeno un carattere speciale");
    		alert.show();
    		return;
    	}
    	
    	UtenteDTO utente = utente_DAO.login_utente(tx_username.getText(), tx_password.getText());
    	
    	
    	if(utente == null) {
    		alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Login");
        	alert.setHeaderText("Credenziali errate");
        	alert.setContentText("Username o password errati");
    		alert.show();
    	}
    	else {
    		Stage this_stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
    		Stage home_stage = new Stage();
    		home_stage.setTitle("Home");
    		home_stage.setResizable(false);
    		if(utente instanceof ElettoreDTO) {
    			FXMLLoader loader = new FXMLLoader(getClass().getResource("schermata_principale_elettore.fxml"));
    			ElettoreHomeController controller = new ElettoreHomeController((ElettoreDTO) utente, this_stage);
				loader.setController(controller);
	    		Parent root = loader.load();
	    		Scene home_scene = new Scene(root, 600, 608);
	    		home_stage.setScene(home_scene);
    		}else {
    			FXMLLoader loader = new FXMLLoader(getClass().getResource("schermata_principale_gestore_del_sistema.fxml"));
    			GestoreDelSistemaHomeController controller = new GestoreDelSistemaHomeController((GestoreDelSistemaDTO) utente, this_stage);
				loader.setController(controller);
	    		Parent root = loader.load();
	    		Scene home_scene = new Scene(root, 600, 608);
	    		home_stage.setScene(home_scene);
				
    		}
    		home_stage.show();
    		this_stage.hide();
    		
    		tx_username.clear();
    		tx_password.clear();
    		
    	}
    	
    }
    

    @FXML
    void Reset_password(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
    	String username = tx_username.getText();
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle("Password reset");
    	alert.setHeaderText("Username error");
    	
    	if(username.length() == 0 ) {
    		alert.setContentText("Per resettare la password è necessario inserire l'username dell'account");
    		alert.show();
    		return;
    	}

    	if(username.length() < 6 || username.length() > 30 ) {
    		alert.setContentText("Il campo username non puo' essere di lunghezza inferiore a 6 o maggiore di 30");
    		alert.show();
    		return;
    	}
    	
    	String email = utente_DAO.get_email(username);
    	if(email==null) {
    		alert.setContentText("Il campo username non corrisponde a nessun account");
    		alert.show();
    		return;
    	}
    	
    	alert.setAlertType(AlertType.ERROR);
    	
    	String otp=otp_functions.generateRandomPassword();		
		OTPDialogController otp_dialog = new OTPDialogController(email, otp, ((Node)event.getSource()).getScene().getWindow(), true);
		otp_dialog.show_and_wait();
		boolean result = otp_dialog.get_result();
		
		if(result) {
	    	String nuova_password = password_functions.generateRandomPassword();
	    	if(utente_DAO.reset_password(username, nuova_password)) {
	    	    MailSender mail_sender = MailSenderImlementation.get_singleton_instance();
	    	    mail_sender.send_async_mail(email, "Password resettata", "Questa è la tua nuova password generata automaticamente: "+ nuova_password);
	    		alert.setAlertType(AlertType.INFORMATION);
	        	alert.setHeaderText("Reset della password effettuato");
	    		alert.setContentText("La password è stata resettata, ti verrà inviata via e-mail la nuova password generata automaticamente");
	    	}else {
	    		alert.setContentText("Non è stato possibile resettare la password");
	    	}
	    	
		}else {
			alert.setContentText("Codice O.T.P. errato, la password non verrà resettata");
		}
    	
		alert.show();
    }
    

}
