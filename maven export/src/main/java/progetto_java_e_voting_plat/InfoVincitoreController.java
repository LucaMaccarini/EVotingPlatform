package progetto_java_e_voting_plat;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class InfoVincitoreController implements Initializable {
	 	@FXML
	    private Label lb_data;

	    @FXML
	    private Label lb_info;

	    @FXML
	    private Label lb_tipo;

	    @FXML
	    private Label lb_username_proprietario;

	    @FXML
	    private Label lb_vincitore;
	    
	    @FXML
	    private Label lb_mod_vincitore;
	    
	    @SuppressWarnings("rawtypes")
		@FXML
	    private ListView listView_classifica;	 
	    
	    
	    private SessioneDiVotoDTO sessione;
	    
	    public InfoVincitoreController(SessioneDiVotoDTO sessione) {
	    	this.sessione=sessione;
	    }

		@SuppressWarnings("unchecked")
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			lb_data.setText(sessione.get_formatted_data_string());
			lb_username_proprietario.setText(sessione.proprietario);
			if(sessione instanceof SessioneDiVotazioneDTO) {
				lb_vincitore.setText(((SessioneDiVotazioneDTO)sessione).get_vincitore().nome);
			}else if (sessione instanceof ReferendumDTO) {
				lb_vincitore.setText(((ReferendumDTO)sessione).get_vincitore());
			}
			
			
			if(sessione instanceof ReferendumDTO)	{
				ReferendumDTO la_sessione = (ReferendumDTO)sessione;
				lb_tipo.setText("Referendum");
				lb_info.setText(la_sessione.quesito);
				
				switch(la_sessione.modalita_vincitore) {
		    		case referendum_senza_quorum: 
		    			lb_mod_vincitore.setText("senza quorum");
		    			break;
		    		case referendum_con_quorum:
		    			lb_mod_vincitore.setText("con quorum");
		    			break;
				}
				
			}else if(sessione instanceof SessioneDiVotazioneDTO) {
				SessioneDiVotazioneDTO votazione = (SessioneDiVotazioneDTO)sessione;
				lb_info.setText(votazione.descrizione);
				switch(votazione.modalita_voto) {
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
				
				switch(votazione.modalita_vincitore) {
		    		case maggioranza: 
		    			lb_mod_vincitore.setText("Maggioranza");
		    			break;
		    		case maggioranza_assoluta:
		    			lb_mod_vincitore.setText("Maggioranza assoluta");
		    			break;
				}
				
				if(votazione.modalita_voto == modalita_voto.voto_ordinale) {
					PartitoDAO partito_DAO = PartitoDAOImplementation.get_singleton_instance();
					ObservableList<PartitoDTO> partiti;
					try {
						partiti = FXCollections.observableArrayList(partito_DAO.get_classifica_partiti_candidati(votazione));
						listView_classifica.setItems(partiti);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
					
				}else if(votazione.modalita_voto == modalita_voto.voto_categorico_con_preferenze) {
					PersonaDAO sessione_di_voto =PersonaDAOImplementation.get_singleton_instance();
					ObservableList<PersonaDTO> persone;
					try {
						persone = FXCollections.observableArrayList(sessione_di_voto.get_classifica_persone_partito_vincitore(votazione));
						listView_classifica.setItems(persone);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
				}
				
			}
			
			
		}
	    
	    
	    
	    
}
