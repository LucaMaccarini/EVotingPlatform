package progetto_java_e_voting_plat;

import java.sql.SQLException;



public interface UtenteDAO {
	public UtenteDTO login_utente(String username, String password) throws ClassNotFoundException, SQLException;
	public boolean modifica_password(UtenteDTO utente, String password, String nuova_password) throws SQLException, ClassNotFoundException;
	public boolean reset_password(String username, String nuova_password) throws SQLException, ClassNotFoundException;
	public String get_email(String username) throws SQLException, ClassNotFoundException;
	public boolean check_email_gia_usata(String email) throws SQLException, ClassNotFoundException;
	public boolean check_username_gia_usato(String username) throws SQLException, ClassNotFoundException;
	public boolean check_codice_fiscale_gia_usato(String codice_fiscale) throws SQLException, ClassNotFoundException;
}
