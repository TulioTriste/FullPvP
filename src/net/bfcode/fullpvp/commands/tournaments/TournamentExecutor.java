package net.bfcode.fullpvp.commands.tournaments;

import net.bfcode.fullpvp.commands.tournaments.arguments.TournamentCancelArgument;
import net.bfcode.fullpvp.commands.tournaments.arguments.TournamentCreateArgument;
import net.bfcode.fullpvp.commands.tournaments.arguments.TournamentJoinArgument;
import net.bfcode.fullpvp.commands.tournaments.arguments.TournamentLeaveArgument;
import net.bfcode.fullpvp.commands.tournaments.arguments.TournamentSetArgument;
import net.bfcode.fullpvp.commands.tournaments.arguments.TournamentStatusArgument;
import net.bfcode.fullpvp.utilities.ArgumentExecutor;
import net.bfcode.fullpvp.utilities.CommandArgument;

public class TournamentExecutor extends ArgumentExecutor {

	public TournamentExecutor() {
		super("tournament");
		this.addArgument((CommandArgument)new TournamentCreateArgument());
        this.addArgument((CommandArgument)new TournamentCancelArgument());
        this.addArgument((CommandArgument)new TournamentStatusArgument());
        this.addArgument((CommandArgument)new TournamentJoinArgument());
        this.addArgument((CommandArgument)new TournamentLeaveArgument());
        this.addArgument((CommandArgument)new TournamentSetArgument());
	}

}
