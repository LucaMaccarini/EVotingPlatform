package progetto_java_e_voting_plat;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

public class OTPDialogController implements Initializable {

	private boolean result;
	private String correct_number;
	private String email;
	private Window owner;
	private boolean hide_email;

	 @FXML
	 private PasswordField tx_otp;
	
	 @FXML
	 private Label lb_email;
	 
	public OTPDialogController(String email, String correct_number, Window owner, boolean hide_email) {
		result=false;
		this.correct_number=correct_number;
		this.email=email;
		this.owner=owner;
		this.hide_email = hide_email;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(hide_email) {
			lb_email.setText(email.replaceAll("(^[^@]{3}|(?!^)\\G)[^@]", "$1*"));
		}
		else {
			lb_email.setText(email);
		}
	}
	
	@FXML
	private void conferma(ActionEvent event) throws IOException {
    	result = tx_otp.getText().equals(correct_number);
    	Stage this_stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
    	this_stage.hide();
	}
	
	
	public boolean get_result() {
		return result;
	}
	
	public void show_and_wait() throws IOException {
		MailSender mail_sender = MailSenderImlementation.get_singleton_instance();
		mail_sender.send_async_mail(email, "Codice O.T.P.", "il tuo codice otp è: " + correct_number);
		Stage stage_votazione = new Stage();
	    stage_votazione.setResizable(false);
	    stage_votazione.initModality(Modality.WINDOW_MODAL);
	    stage_votazione.initOwner(owner);
	    Parent root;
	    stage_votazione.setTitle("Codice O.T.P.");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("schermata_OTP.fxml"));
        OTPDialogController controller = this;
        loader.setController(controller);
		root = loader.load();
		stage_votazione.setScene(new Scene(root, 367, 143));
		stage_votazione.showAndWait();
	}
	
}
