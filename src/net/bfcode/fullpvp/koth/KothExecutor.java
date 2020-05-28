package net.bfcode.fullpvp.koth;

import net.bfcode.fullpvp.FullPvP;
import net.bfcode.fullpvp.koth.argument.KothCancelArgument;
import net.bfcode.fullpvp.koth.argument.KothCreateArgument;
import net.bfcode.fullpvp.koth.argument.KothDeleteArgument;
import net.bfcode.fullpvp.koth.argument.KothListArgument;
import net.bfcode.fullpvp.koth.argument.KothSetCapZoneArgument;
import net.bfcode.fullpvp.koth.argument.KothSetLocationArgument;
import net.bfcode.fullpvp.koth.argument.KothStartArgument;
import net.bfcode.fullpvp.utilities.ArgumentExecutor;
import net.bfcode.fullpvp.utilities.CommandArgument;

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
