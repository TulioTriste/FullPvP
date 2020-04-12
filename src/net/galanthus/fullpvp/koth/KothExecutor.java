package net.galanthus.fullpvp.koth;

import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.koth.argument.KothCancelArgument;
import net.galanthus.fullpvp.koth.argument.KothCreateArgument;
import net.galanthus.fullpvp.koth.argument.KothDeleteArgument;
import net.galanthus.fullpvp.koth.argument.KothListArgument;
import net.galanthus.fullpvp.koth.argument.KothSetCapZoneArgument;
import net.galanthus.fullpvp.koth.argument.KothSetLocationArgument;
import net.galanthus.fullpvp.koth.argument.KothStartArgument;
import net.galanthus.fullpvp.utilities.ArgumentExecutor;
import net.galanthus.fullpvp.utilities.CommandArgument;

public class KothExecutor extends ArgumentExecutor {
	
    public KothExecutor(FullPvP plugin) {
        super("koth");
        this.addArgument((CommandArgument)new KothCreateArgument(plugin));
        this.addArgument((CommandArgument)new KothDeleteArgument(plugin));
        this.addArgument((CommandArgument)new KothStartArgument(plugin));
        this.addArgument((CommandArgument)new KothCancelArgument(plugin));
        this.addArgument((CommandArgument)new KothSetCapZoneArgument(plugin));
        this.addArgument((CommandArgument)new KothSetLocationArgument(plugin));
        this.addArgument((CommandArgument)new KothListArgument(plugin));
    }
}
