package progetto_java_e_voting_plat;

public enum modalita_voto {
	voto_ordinale,
	voto_categorico,
	voto_categorico_con_preferenze;
	
	
	@Override
    public String toString() {
        String output = "";
        switch (this) {
	        case voto_ordinale:
	        	output = "Voto ordinale";
	            break;
	        case voto_categorico:
	        	output = "Voto categorico";
	            break;
	        case voto_categorico_con_preferenze:
	        	output = "Voto categorico con preferenze";
	            break;
        }
        return output;
    }
}
