package progetto_java_e_voting_plat;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountPassword implements Password {
	
	private static AccountPassword singleton_instance;
	private static String lettere_minuscole = "abcdefghijklmnopqrstuvxyz";
	private static String lettere_maiuscole = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static String numeri = "0123456789";
	private static String caratteri_speciali = "ùàòì!£$%&/()=#°+*{}[]ç;,.:-_<>^";
	private static String tutti_i_caratteri = lettere_maiuscole + lettere_minuscole + numeri + caratteri_speciali;
	
	public static AccountPassword get_singleton_instance() {
		if(singleton_instance == null)
			singleton_instance = new AccountPassword();
		return singleton_instance;
	}

	
	public boolean checkPassword(String pass) {
		 if(pass.length() < 6 || pass.length() > 20 )
			 return false;
		 
	    	Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
	    	Matcher m = p.matcher(pass);
	    	 
	    	if (m.find()) {
	    	
		    	char ch;
		        boolean capitalFlag = false;
		        boolean numberFlag = false;
		        for(int i=0;i < pass.length();i++) {
		            ch = pass.charAt(i);
		            if( Character.isDigit(ch)) {
		                numberFlag = true;
		            }
		            
		            if (Character.isUpperCase(ch)) {
		                capitalFlag = true;
		            }
		            
		            if(numberFlag && capitalFlag)
		                return true;
		        }
		        
	    	}
	    	
	    	return false;
	    }
	 
	 public String generateRandomPassword() {
		 Random r = new Random();
		 int low = 10;
		 int high = 15;
		 int length = r.nextInt(high-low) + low;

		 String output = 	random_string(length/3-1, tutti_i_caratteri) +
							random_string(1, lettere_maiuscole) +
							random_string(length/3-1, tutti_i_caratteri) +
							random_string(1, numeri)+
							random_string(length/3-1, tutti_i_caratteri) +
							random_string(1, caratteri_speciali);
		 
		 if(length % 3 != 0) {
			 int mancanti = length - (length/3)*3; 	//la divisione è intera, quindi rimoltiplicando per lo stesso numero e 
			 										//sottraendo il tutto alla lunghezza originaria trovo i carattei che ho perso
			 output += random_string(mancanti, tutti_i_caratteri);
		 }
		 return output;
	 }
	 
    private String random_string(int n, String caratteri_possibili)
    {
    
        StringBuilder sb = new StringBuilder(n);
  
        for (int i = 0; i < n; i++) {
            int index
                = (int)(caratteri_possibili.length()
                        * Math.random());
 
            sb.append(caratteri_possibili
                          .charAt(index));
        }
  
        return sb.toString();
    }
}
