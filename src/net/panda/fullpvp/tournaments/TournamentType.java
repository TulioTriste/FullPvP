package net.panda.fullpvp.tournaments;

public enum TournamentType {
	
    SUMO("SUMO", 0, "Sumo"),
    FFA("FFA", 1, "FFA");
    
    private String name;
    
    private TournamentType(final String s, final int n, final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
