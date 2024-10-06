package progetto_java_e_voting_plat;

public enum modalita_calcolo_vincitore_sessione_di_votazione {
	maggioranza,
	maggioranza_assoluta;
	
	
	@Override
    public String toString() {
        String output = "";
        switch (this) {
        case maggioranza:
        	output = "maggioranza";
            break;
        case maggioranza_assoluta:
        	output = "Maggioranza assoluta";
            break;
        }
        return output;
    }
}
