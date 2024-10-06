package progetto_java_e_voting_plat;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSenderImlementation implements MailSender{
	 	private static String USER_NAME = "alessandro.bonetti234"; //account gmail per inviare le email  
	    private static String PASSWORD = "PasswordPerEsame1#"; // Gmail password
	    
	    private static MailSenderImlementation singleton_instance;
	    
	    public static MailSenderImlementation get_singleton_instance() {
			if(singleton_instance == null)
				singleton_instance = new MailSenderImlementation();
			return singleton_instance;
		}
		
	    
	    public void send_async_mail(String destinatario, String oggetto, String testo) {
	    	new Thread(new Runnable() {
	    	    public void run() {
	    	    	send_email(destinatario, oggetto, testo);
	    	    }
	    	}).start();
	    }
	    
	    public void send_email(String destinatario, String oggetto, String testo) {
	    	if(!check_email(destinatario))
	    		return;
	        String from = USER_NAME;
	        String pass = PASSWORD;
	        String[] to = { destinatario }; //lista di destinatari
	        sendFromGMail(from, pass, to, oggetto, testo);
	    }
	    
	    public boolean check_email(String email) {
	    boolean result = true;
    	   try {
    	      InternetAddress emailAddr = new InternetAddress(email);
    	      emailAddr.validate();
    	   } catch (AddressException ex) {
    	      result = false;
    	   }
    	   return result;
	    }
	    
	    private void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
	        Properties props = System.getProperties();
	        String host = "smtp.gmail.com";
	        props.put("mail.smtp.host", host);
	        props.put("mail.smtp.port", "465");
	        props.put("mail.smtp.ssl.enable", "true");
	        props.put("mail.smtp.auth", "true");

	        Session session = Session.getDefaultInstance(props);
	        MimeMessage message = new MimeMessage(session);

	        try {
	            message.setFrom(new InternetAddress("Java E-Voting Platform" + "<" + "no-reply@JavaEVotingPlatform.com" + ">"));
	            InternetAddress[] toAddress = new InternetAddress[to.length];

	            
	            for( int i = 0; i < to.length; i++ ) {
	                toAddress[i] = new InternetAddress(to[i]);
	            }

	            for( int i = 0; i < toAddress.length; i++) {
	                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
	            }

	            message.setSubject(subject);
	            message.setText(body);
	            Transport transport = session.getTransport("smtp");
	            transport.connect(host, from, pass);
	            transport.sendMessage(message, message.getAllRecipients());
	            transport.close();
	        }
	        catch (AddressException ae) {
	            ae.printStackTrace();
	        }
	        catch (MessagingException me) {
	            me.printStackTrace();
	        }
	    }
}
