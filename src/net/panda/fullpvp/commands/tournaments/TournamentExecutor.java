package net.panda.fullpvp.commands.tournaments;

import net.panda.fullpvp.commands.tournaments.arguments.TournamentCancelArgument;
import net.panda.fullpvp.commands.tournaments.arguments.TournamentCreateArgument;
import net.panda.fullpvp.commands.tournaments.arguments.TournamentJoinArgument;
import net.panda.fullpvp.commands.tournaments.arguments.TournamentLeaveArgument;
import net.panda.fullpvp.commands.tournaments.arguments.TournamentSetArgument;
import net.panda.fullpvp.commands.tournaments.arguments.TournamentStatusArgument;
import net.panda.fullpvp.utilities.ArgumentExecutor;

public class TournamentExecutor extends ArgumentExecutor {

	public TournamentExecutor() {
		super("tournament");
		this.addArgument(new TournamentCreateArgument());
        this.addArgument(new TournamentCancelArgument());
        this.addArgument(new TournamentStatusArgument());
        this.addArgument(new TournamentJoinArgument());
        this.addArgument(new TournamentLeaveArgument());
        this.addArgument(new TournamentSetArgument());
	}
}
