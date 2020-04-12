package net.galanthus.fullpvp.tournaments;

public enum TournamentType {
	
	SUMO("Sumo"), 
	FFA("FFA"),
	TNTTAG("TNTTag");
    
    private String name;
    
    private TournamentType(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
