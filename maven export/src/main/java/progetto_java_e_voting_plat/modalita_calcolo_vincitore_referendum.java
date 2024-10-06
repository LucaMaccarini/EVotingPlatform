package progetto_java_e_voting_plat;

public enum modalita_calcolo_vincitore_referendum {
	referendum_senza_quorum,
	referendum_con_quorum;
	
	@Override
    public String toString() {
        String output = "";
        switch (this) {
        case referendum_senza_quorum:
        	output = "Senza quorum";
            break;
        case referendum_con_quorum:
        	output = "Con quorum";
            break;
        }
        return output;
    }
}
