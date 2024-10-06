package progetto_java_e_voting_plat;

public interface MailSender {
	public void send_async_mail(String destinatario, String oggetto, String testo);
	public void send_email(String destinatario, String oggetto, String testo);
	public boolean check_email(String email);

}
