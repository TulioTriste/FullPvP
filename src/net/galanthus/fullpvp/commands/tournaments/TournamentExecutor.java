package net.galanthus.fullpvp.commands.tournaments;

import net.galanthus.fullpvp.commands.tournaments.arguments.TournamentCancelArgument;
import net.galanthus.fullpvp.commands.tournaments.arguments.TournamentCreateArgument;
import net.galanthus.fullpvp.commands.tournaments.arguments.TournamentHostArgument;
import net.galanthus.fullpvp.commands.tournaments.arguments.TournamentJoinArgument;
import net.galanthus.fullpvp.commands.tournaments.arguments.TournamentLeaveArgument;
import net.galanthus.fullpvp.commands.tournaments.arguments.TournamentSetArgument;
import net.galanthus.fullpvp.commands.tournaments.arguments.TournamentStatusArgument;
import net.galanthus.fullpvp.utilities.ArgumentExecutor;
import net.galanthus.fullpvp.utilities.CommandArgument;

public class TournamentExecutor extends ArgumentExecutor {

	public TournamentExecutor() {
		super("tournament");
		this.addArgument((CommandArgument)new TournamentCreateArgument());
        this.addArgument((CommandArgument)new TournamentCancelArgument());
        this.addArgument((CommandArgument)new TournamentStatusArgument());
        this.addArgument((CommandArgument)new TournamentJoinArgument());
        this.addArgument((CommandArgument)new TournamentLeaveArgument());
        this.addArgument((CommandArgument)new TournamentSetArgument());
        this.addArgument((CommandArgument)new TournamentHostArgument());
	}

}
