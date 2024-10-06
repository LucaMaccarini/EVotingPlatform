package progetto_java_e_voting_plat;

import java.time.LocalDate;

public class CodiceFiscale {	
	private static CodiceFiscale singleton_instance;
		
		
	public static CodiceFiscale get_singleton_instance() {
		if(singleton_instance == null) {
			singleton_instance = new CodiceFiscale();
		}
		return singleton_instance;
	}
	
	public boolean Check_CF (String nome, String cognome, boolean is_maschio, LocalDate data_nascita, String nazione, String CF){
		int giorno = data_nascita.getDayOfMonth();
		int mese = data_nascita.getMonthValue();
		int anno = data_nascita.getYear();
		
		nome = nome.replaceAll("[^A-Za-z]+", "");
		cognome = cognome.replaceAll("[^A-Za-z]+", "");
		nazione = nazione.replaceAll("[^A-Za-z]+", "");

		if(CF == null)
			return false;
		
		if(CF.length()>16)
			return false;

		if(!Character.isLetter(CF.charAt(11)))
			return false;
		
		if(!nazione.toLowerCase().equals("italia"))
			if(!(Character.toLowerCase(CF.charAt(11)) =='z'))
				return false;
		
		if(!Character.isDigit(CF.charAt(12)) || !Character.isDigit(CF.charAt(13)) || !Character.isDigit(CF.charAt(14))){
			return false;
		}
		
		if(!Character.isLetter(CF.charAt(15)))
			return false;
		
		
		
		String generated_CF="";
		int consonanti=0;
		for(int i=0; i<cognome.length() && consonanti<3; i++){
			if(is_consonante(cognome.charAt(i))){
				generated_CF+=Character.toUpperCase(cognome.charAt(i));
				consonanti++;
			}
		}
		

		for(int i=0; i<cognome.length() && consonanti<3; i++){
			if(is_vocale(cognome.charAt(i))){
				generated_CF+=Character.toUpperCase(cognome.charAt(i));
				consonanti++;
			}
		}

		while(consonanti<3){
			generated_CF+="X";
			consonanti++;
		}
	
		consonanti=0;
		
		if(conta_consonanti(nome)>=4){
			for(int i=0; i<nome.length() && consonanti<4; i++){
				if(is_consonante(nome.charAt(i))){
					if(consonanti != 1)
						generated_CF+=Character.toUpperCase(nome.charAt(i));
					consonanti++;
				}
			}
		}else{
			for(int i=0; i<nome.length() && consonanti<3; i++){
				if(is_consonante(nome.charAt(i))){
					generated_CF+=Character.toUpperCase(nome.charAt(i));
					consonanti++;
				}
			}
			

			for(int i=0; i<nome.length() && consonanti<3; i++){
				if(is_vocale(nome.charAt(i))){
					generated_CF+=Character.toUpperCase(nome.charAt(i));
					consonanti++;
				}
			}

			while(consonanti<3){
				generated_CF+="X";
				consonanti++;
			}			
		}
		
		int appoggio = anno%100;
		
		if(appoggio<10)
			generated_CF+="0"+Integer.toString(appoggio);
		else
			generated_CF+=Integer.toString(appoggio);
		
		switch(mese){
			case 1:
				generated_CF+="A";
			break;
			
			case 2:
				generated_CF+="B";
			break;
			
			case 3:
				generated_CF+="C";
			break;
			
			case 4:
				generated_CF+="D";
			break;
			
			case 5:
				generated_CF+="E";
			break;
			
			case 6:
				generated_CF+="H";
			break;
			
			case 7:
				generated_CF+="L";
			break;
			
			case 8:
				generated_CF+="M";
			break;
			
			case 9:
				generated_CF+="P";
			break;
			
			case 10:
				generated_CF+="R";
			break;
			
			case 11:
				generated_CF+="S";
			break;
			
			case 12:
				generated_CF+="T";
			break;
		
		}
		
		if(is_maschio){
			if(giorno<10)
				generated_CF+="0";
			generated_CF+=Integer.toString(giorno);
		}else{
			generated_CF+=Integer.toString(giorno+40);
		}
		
		
		return generated_CF.equals(CF.substring(0, 11).toUpperCase());
		//return  generated_CF + " "+CF.substring(0, 11);
	}
	

	private boolean is_vocale(char c){
		c=Character.toLowerCase(c);
		if(c=='a' || c=='e' || c=='i' || c=='o' || c=='u'){
			return true;
		}
		return false;
		
	}
	
	private int conta_consonanti(String s){
		int count = 0;
		for (int i=0 ; i<s.length(); i++){
			char ch = Character.toLowerCase(s.charAt(i));
			if(ch != 'a' && ch != 'e' && ch != 'i' && ch != 'o' && ch != 'u'){
			    count ++;
			}
      }
      
      return count;
	}
	
	private boolean is_consonante(char c){
		return !is_vocale(c);
	}
}
