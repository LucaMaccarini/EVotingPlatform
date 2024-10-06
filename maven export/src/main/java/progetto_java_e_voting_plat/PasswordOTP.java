package progetto_java_e_voting_plat;

public class PasswordOTP implements Password {

	private static String numeri = "0123456789";
	private static PasswordOTP singleton_instance;
	
	
	public static PasswordOTP get_singletone_instance() {
		if(singleton_instance == null)
			singleton_instance = new PasswordOTP();
		return singleton_instance;
	}
	
	@Override
	public boolean checkPassword(String pass) {
		for(int i=0; i<pass.length(); i++) {
			if(!Character.isDigit(pass.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String generateRandomPassword() {
		return random_string(5, numeri);
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
