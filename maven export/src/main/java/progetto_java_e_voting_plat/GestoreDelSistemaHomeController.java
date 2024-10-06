package progetto_java_e_voting_plat;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GestoreDelSistemaHomeController extends azioni_base_account_controller implements Initializable {

private Stage login_stage;
	
	@FXML
    private Button bt_aggiorna;
	

    @FXML
    private Label lb_username;
    
    @FXML
    private Button bt_disconnetti;
    
    @FXML
    private ListView<SessioneDiVotoDTO> listView_sessioni_di_voto;
    
    @FXML
    private ListView<SessioneDiVotoDTO> listView_sessioni_di_voto_vincitori;
    
    @FXML
    private ListView<LogDTO> listView_logs;
    
    @FXML
    private ListView<PartitoDTO> listView_partiti;
    
    @FXML
    private PasswordField tx_modifica_pass_attuale;

    @FXML
    private PasswordField tx_modifica_pass_nuova;
    
    @FXML
    private PasswordField tx_modifica_pass_ripeti;

    @FXML
    private TextField tx_nuovo_partito_nome;
    
    @FXML
    private TextField tx_nuovo_partito_descrizione;
    
    @FXML
    private TextField tx_nuovo_partito_nome_persona;
    
    @FXML
    private TextField tx_nuovo_partito_cognome_persona;
    
    @FXML
    private TextField tx_nuova_persona_nome;
    
    @FXML
    private TextField tx_nuova_persona_cognome;
    
    @FXML
    private TextField tx_nuovo_elettore_codice_fiscale;

    @FXML
    private TextField tx_nuovo_elettore_cognome;

    @FXML
    private TextField tx_nuovo_elettore_comune_nascita;

    @FXML
    private TextField tx_nuovo_elettore_email;

    @FXML
    private TextField tx_nuovo_elettore_nome;

    @FXML
    private TextField tx_nuovo_elettore_provincia;

    @FXML
    private TextField tx_nuovo_elettore_username;
    
    @FXML
    private DatePicker dt_nuovo_elettore_data_nascita;
    
    @FXML
    private TextField tx_nuovo_elettore_sesso;

    
    private SessioneDiVotoDAO sessione_di_voto_DAO;
    private PartitoDAO partito_DAO;
    private GestoreDelSistemaDAO gestore_del_sistema_DAO;
    private UtenteDAO utente_DAO;
    private Password otp_functions;
    private Password password_functions;
    private GestoreDelSistemaDTO gestore_del_sistema;
    private CodiceFiscale codice_fiscale_functions;
    private AuditingDAO auditing_DAO;
    
    public GestoreDelSistemaHomeController(GestoreDelSistemaDTO gestore_del_sistema, Stage login_stage) {
    	this.login_stage = login_stage;
    	this.gestore_del_sistema = gestore_del_sistema;
    	sessione_di_voto_DAO = SessioneDiVotoDAOImplementation.get_singleton_instance();
    	partito_DAO = PartitoDAOImplementation.get_singleton_instance();
    	gestore_del_sistema_DAO = GestoreDelSistemaDAOImplementation.get_singleton_instance();
    	codice_fiscale_functions = CodiceFiscale.get_singleton_instance();
    	utente_DAO = UtenteDAOImplementation.get_singleton_instance();
    	otp_functions = PasswordOTP.get_singletone_instance();
    	password_functions = AccountPassword.get_singleton_instance();
    	auditing_DAO = AuditingDAOImplementation.get_singleton_instance();
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lb_username.setText(gestore_del_sistema.username);
		
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
		
		listView_sessioni_di_voto.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent click) {

		        if (click.getClickCount() == 2) {
		           SessioneDiVotoDTO currentItemSelected = listView_sessioni_di_voto.getSelectionModel().getSelectedItem();
		           Stage stage_gestione_votazione = new Stage();
		           stage_gestione_votazione.setResizable(false);
		           stage_gestione_votazione.initModality(Modality.WINDOW_MODAL);
		           stage_gestione_votazione.initOwner(((Node)click.getSource()).getScene().getWindow());
		           Parent root;
		         
		           stage_gestione_votazione.setTitle("Gestione Sessione di voto: " + currentItemSelected.nome);
		           FXMLLoader loader = new FXMLLoader(getClass().getResource("schermata_gestione_sessione_di_voto.fxml"));
		           GestioneSessioneDiVotoController controller = new GestioneSessioneDiVotoController(gestore_del_sistema, currentItemSelected);
		           loader.setController(controller);
					try {
						root = loader.load();
						stage_gestione_votazione.setScene(new Scene(root, 549, 484));
					} catch (IOException e) {
						e.printStackTrace();
					}
					 stage_gestione_votazione.showAndWait();
					 
					 try {
						if(sessione_di_voto_DAO.check_vincitore_gia_calcolato(currentItemSelected)) {
							 listView_sessioni_di_voto.getItems().remove(currentItemSelected);
						 }
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
		        }
		    }
		});
		
		listView_partiti.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent click) {

		        if (click.getClickCount() == 2) {
		           PartitoDTO currentItemSelected = listView_partiti.getSelectionModel().getSelectedItem();
		           Stage stage_persone_partito = new Stage();
		           stage_persone_partito.setResizable(false);
		           stage_persone_partito.initModality(Modality.WINDOW_MODAL);
		           stage_persone_partito.initOwner(((Node)click.getSource()).getScene().getWindow() );
		           Parent root;
		           stage_persone_partito.setTitle("Partito");
		           FXMLLoader loader = new FXMLLoader(getClass().getResource("schermata_persone_del_partito.fxml"));	      
					try {
						PersoneDelPartitoController controller = new PersoneDelPartitoController(currentItemSelected);
						loader.setController(controller);
						root = loader.load();
						stage_persone_partito.setScene(new Scene(root, 307, 446));
					} catch (Exception e) {
						e.printStackTrace();
					}
					 stage_persone_partito.show();
		           
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
    private void fill_sessioni_di_voto_listview(ActionEvent event) throws ClassNotFoundException, SQLException {
    	ObservableList<SessioneDiVotoDTO> sessioni =   FXCollections.observableArrayList(sessione_di_voto_DAO.get_sessioni_di_voto_con_vincitore_non_calcolato());
    	listView_sessioni_di_voto.getItems().clear();
    	listView_sessioni_di_voto.setItems(sessioni);
    }
  
    
    @FXML
    private void fill_sessioni_di_voto_vincitori_listview(ActionEvent event) throws ClassNotFoundException, SQLException { 
    	riempi_sessioni_di_voto_vincitori_listview(listView_sessioni_di_voto_vincitori);
    }
    
    @FXML
    private void modifica_password(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
    	modifica_password(gestore_del_sistema, tx_modifica_pass_attuale, tx_modifica_pass_nuova, tx_modifica_pass_ripeti);
    }
    
    @FXML
    void fill_partiti_listview(ActionEvent event) throws ClassNotFoundException, SQLException {
    	ObservableList<PartitoDTO> partiti = FXCollections.observableArrayList(partito_DAO.get_partiti());
    	listView_partiti.getItems().clear();
    	listView_partiti.setItems(partiti);
    }
    
    @FXML
    void aggiungi_persona(ActionEvent event) throws ClassNotFoundException, SQLException, IOException, LogException {
    	PartitoDTO currentItemSelected = listView_partiti.getSelectionModel().getSelectedItem();
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle("Persona");
    	alert.setHeaderText("Nuova Persona");
    	if(currentItemSelected==null) {
        	alert.setContentText("selezionare dalla lista il partito a cui apparterrà la nuova persona");
        	alert.show();
        	return;
    	}
		String nome_persona = tx_nuova_persona_nome.getText();
		String cognome_persona = tx_nuova_persona_cognome.getText();
		
		if(nome_persona.length() <= 0 || nome_persona.length() > 30) {
			
			alert.setContentText("il nome della persona non puo' essere vuoto o maggiore di 30");
			alert.show();
			return;
		}
		
		if(cognome_persona.length()<=0 || cognome_persona.length()>30) {
			
			alert.setContentText("il cognome della persona non puo' essere vuoto o maggiore di 30");
			alert.show();
			return;
		}
		
		if(partito_DAO.aggiungi_persona_ad_un_partito(gestore_del_sistema, nome_persona, cognome_persona, currentItemSelected)) {
			alert.setAlertType(AlertType.INFORMATION);
			alert.setContentText("persona aggiunta con successo");
			clear_campi_nuova_persona();	
		}else {
			alert.setAlertType(AlertType.ERROR);
			alert.setContentText("non è stato possibile aggiungere la persona");
			clear_campi_nuova_persona();
		}
		
		alert.show();
    	
    }
    
    private void clear_campi_nuova_persona() {
    	tx_nuova_persona_nome.clear();
    	tx_nuova_persona_cognome.clear();
    }
    
    private void clear_campi_nuovo_partito() {
    	tx_nuovo_partito_nome.clear();
    	tx_nuovo_partito_descrizione.clear();
    	tx_nuovo_partito_nome_persona.clear();
    	tx_nuovo_partito_cognome_persona.clear();
    }
    
    @FXML
    private void aggiungi_partito(ActionEvent event) throws ClassNotFoundException, SQLException, IOException, LogException{ 
    	String nome_partito = tx_nuovo_partito_nome.getText();
    	
    	String nome_persona = tx_nuovo_partito_nome_persona.getText();
    	String cognome_persona = tx_nuovo_partito_cognome_persona.getText();
    	String descrizione = tx_nuovo_partito_descrizione.getText();
    	    	
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle("Partito");
    	alert.setHeaderText("Nuovo Partito");
    	
    	if(nome_partito.length() <= 0 || nome_partito.length() > 30) {
    		
    		alert.setContentText("il nome del partito non può essere vuoto o maggiore di 30");
    		alert.show();
    		
			return;
    	}
    	
    	if(descrizione.length() <= 0 || descrizione.length() > 40) {
    		
    		alert.setContentText("la descrizione del partito non può essere vuota o maggiore di 40");
    		alert.show();
    		
			return;
    	}
    	    	if(nome_partito.toLowerCase().equals("non_calcolato") || nome_partito.toLowerCase().equals("nessuno")) {
    		alert.setContentText("il nome del partito non può essere 'non_calcolato' e 'nessuno'");
    		alert.show();
    		clear_campi_nuovo_partito();
			return;
    	}
    	
    	if(nome_persona.length() <= 0 || nome_persona.length() > 30 ) {
	
			alert.setContentText("il nome della persona non puo' essere vuoto o maggiore di 30");
			alert.show();
			return;
		}
		
		if(cognome_persona.length()<=0 || cognome_persona.length()>30) {
			
			alert.setContentText("il cognome della persona non puo' essere vuoto o maggiore di 30");
			alert.show();
			return;
		}
		    	
    	
    	if(partito_DAO.aggiungi_partito(gestore_del_sistema, nome_partito, descrizione)) {
    		
    		partito_DAO.aggiungi_persona_ad_un_partito(gestore_del_sistema, nome_persona, cognome_persona, partito_DAO.get_partito(nome_partito));
    		alert.setAlertType(AlertType.INFORMATION);
    		alert.setContentText("il partito è stato aggiunto");
    		fill_partiti_listview(new ActionEvent());
    	}else {
    		alert.setAlertType(AlertType.ERROR);
    		alert.setContentText("il nome del partito è già utilizzato da un atro partito");
    		
    	}
    	clear_campi_nuovo_partito();
    	alert.show();
    	
    	
    }
    
    @FXML
    void registra_nuovo_elettore(ActionEvent event) throws IOException, ClassNotFoundException, SQLException, LogException {
    	String username = tx_nuovo_elettore_username.getText();
    	String email = tx_nuovo_elettore_email.getText();
    	String nome = tx_nuovo_elettore_nome.getText();
    	String cognome = tx_nuovo_elettore_cognome.getText();
    	String comune_nascita = tx_nuovo_elettore_comune_nascita.getText();
    	String provincia_nascita = tx_nuovo_elettore_provincia.getText();
    	String codice_fiscale = tx_nuovo_elettore_codice_fiscale.getText().toUpperCase();
    	LocalDate data_nascita = dt_nuovo_elettore_data_nascita.getValue();
    	LocalDate today = LocalDate.now(ZoneId.systemDefault());
    	String stringa_sesso = tx_nuovo_elettore_sesso.getText();
    	
    	
    	
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle("Nuovo Elettore");
    	alert.setHeaderText("Registrazione nuovo elettore");
    	
    	if(username.length()<6 || username.length()>30) {
    		alert.setContentText("Il campo username non puo' essere di lunghezzza inferiore a 6 o maggiore di 30 caratteri");
    		alert.show();
    		return;
    	}
    	
    	if(username.contains(" ")) {
    		alert.setContentText("Il campo username non puo' contenere spazi");
    		alert.show();
    		return;
    	}
    	
    	if(utente_DAO.check_username_gia_usato(username)) {
    		alert.setContentText("L' username inserito è gia' in uso da un'altro account");
    		alert.show();
    		return;
    	}
    	
    	if(email.length()<=0 || email.length()>50) {
    		alert.setContentText("Il campo email non puo' essere vuoto e non puo' essere di lunghezza maggiore di 50 caratteri");
    		alert.show();
    		return;
    	}
    	
    	if(!mail_sender.check_email(email)) {
    		alert.setContentText("Il campo email non corrisponde ad una vera e-mail");
    		alert.show();
    		return;
    	}
    	
    	if(utente_DAO.check_email_gia_usata(email)) {
    		alert.setContentText("l'email è già utilizzata da un altro account");
    		alert.show();
    		return;
    	}
    	
    	
		if(nome.length()<=0 || nome.length()>20) {
			alert.setContentText("Il campo nome non puo' essere vuoto e non puoì essere di lunghezza maggiore di 20 caratteri");
			alert.show();
    		return;
		}
		
		if(cognome.length()<=0 || cognome.length()>20) {
			alert.setContentText("Il campo cognome non puo' essere vuoto e non puoì essere di lunghezza maggiore di 20 caratteri");
			alert.show();
    		return;
		}
		
		if(comune_nascita.length()<=0 || comune_nascita.length()>20) {
			alert.setContentText("Il campo comune nascita non puo' essere vuoto e non puoì essere di lunghezza maggiore di 20 caratteri");
			alert.show();
    		return;
		}
		
		if(provincia_nascita.length()!=2) {
			alert.setContentText("Il campo identificatore provincia deve essere di due caratteri");
			alert.show();
    		return;
		}
		
		if(data_nascita==null || !data_nascita.isBefore(today)) {
			alert.setContentText("Il campo data nascita deve contenere una data precedente alla data attuale");
			alert.show();
    		return;
		}
		
		if(!stringa_sesso.toLowerCase().contentEquals("m") && !stringa_sesso.toLowerCase().contentEquals("f")){
			alert.setContentText("Il campo Sesso nascita deve contenere la lettera \"M\" o la lettera \"F\" (anche minuscole)");
			alert.show();
    		return;
		}
		
		boolean is_maschio=false;
		if(stringa_sesso.toLowerCase().equals("m"))
			is_maschio=true;
		
		if(!codice_fiscale_functions.Check_CF(nome, cognome, is_maschio, data_nascita, "italia", codice_fiscale)) {
			alert.setContentText("Il campo codice fiscale non corrisponde ai dati inseriti");
			alert.show();
    		return;
		}
		
		if(utente_DAO.check_codice_fiscale_gia_usato(codice_fiscale)) {
    		alert.setContentText("il codice fiscale inserito è gia' in uso da un'altro account");
    		alert.show();
    		return;
    	}
		
		String otp=otp_functions.generateRandomPassword();		
		OTPDialogController otp_dialog = new OTPDialogController(email, otp, ((Node)event.getSource()).getScene().getWindow(), false);
		otp_dialog.show_and_wait();
		boolean result = otp_dialog.get_result();
		
		alert.setAlertType(AlertType.ERROR);
		
		if(result) {
			String new_password = password_functions.generateRandomPassword();
			boolean registrato = gestore_del_sistema_DAO.registra_nuovo_elettore(gestore_del_sistema, username, email, nome, cognome, is_maschio, data_nascita, comune_nascita, provincia_nascita, codice_fiscale, new_password);
			if(registrato) {
				alert.setAlertType(AlertType.INFORMATION);
				alert.setContentText("Codice O.T.P. corretto, l'elettore è stato registrato!\nla password generata automaticamente sarà inviata via e-mail all'elettore");
				mail_sender.send_async_mail(email, "Registrazione Confermata!", "il tuo Account è stato registrato con successo!\nLa password per accedervi è: " + new_password);
				clear_campi_nuovo_elettore();
			}else {
				alert.setContentText("Non è stato possibile registrare l'elettore");
				clear_campi_nuovo_elettore();
			}
		}else {
			alert.setContentText("Codice O.T.P. errato, l'elettore non è stato registrato!");
		}
		
		alert.show();
	
    }
    
    private void clear_campi_nuovo_elettore() {
    	tx_nuovo_elettore_username.clear();
    	tx_nuovo_elettore_email.clear();
    	tx_nuovo_elettore_nome.clear();
    	tx_nuovo_elettore_cognome.clear();
    	tx_nuovo_elettore_comune_nascita.clear();
    	tx_nuovo_elettore_provincia.clear();
    	tx_nuovo_elettore_codice_fiscale.clear();
    	dt_nuovo_elettore_data_nascita.getEditor().clear();
    	tx_nuovo_elettore_sesso.clear();
    }
    
    @FXML
    private void nuovo_referendum(ActionEvent event) {
        Stage stage_nuovo_referendum = new Stage();
        stage_nuovo_referendum.setResizable(false);
        //stage_nuovo_referendum.initModality(Modality.WINDOW_MODAL);
        //stage_nuovo_referendum.initOwner(((Node)click.getSource()).getScene().getWindow() );
        Parent root;
        stage_nuovo_referendum.setTitle("Nuovo referendum");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("schermata_nuova_sessione_di_voto.fxml"));	      
			try {
				NuovaSessioneDiVotoController controller = new NuovaSessioneDiVotoController(gestore_del_sistema, false);
				loader.setController(controller);
				root = loader.load();
				stage_nuovo_referendum.setScene(new Scene(root, 805, 453));
			} catch (Exception e) {
				e.printStackTrace();
			}
			 stage_nuovo_referendum.show();
    }
    
    @FXML
    private void nuova_votazione(ActionEvent event) {
        Stage stage_nuovo_referendum = new Stage();
        stage_nuovo_referendum.setResizable(false);
        //stage_nuovo_referendum.initModality(Modality.WINDOW_MODAL);
        //stage_nuovo_referendum.initOwner(((Node)click.getSource()).getScene().getWindow() );
        Parent root;
        stage_nuovo_referendum.setTitle("Nuova votazione");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("schermata_nuova_sessione_di_voto.fxml"));	      
			try {
				NuovaSessioneDiVotoController controller = new NuovaSessioneDiVotoController(gestore_del_sistema, true);
				loader.setController(controller);
				root = loader.load();
				stage_nuovo_referendum.setScene(new Scene(root, 1170, 453));
			} catch (Exception e) {
				e.printStackTrace();
			}
			 stage_nuovo_referendum.show();
    }
    
    @FXML
    private void aggiorna_logs(ActionEvent event) throws ClassNotFoundException, SQLException {
    	
    	ObservableList<LogDTO> logs =   FXCollections.observableArrayList(auditing_DAO.get_logs());
    	listView_logs.getItems().clear();
    	listView_logs.setItems(logs);

    }
    
    @FXML
    private void clear_logs(ActionEvent event) throws ClassNotFoundException, SQLException {
    	auditing_DAO.delete_all_logs();
    	aggiorna_logs(new ActionEvent());
    }

}

