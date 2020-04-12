package net.galanthus.fullpvp.utilities.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftItem;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.net.InetAddresses;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import gnu.trove.procedure.TObjectIntProcedure;
import gnu.trove.procedure.TObjectLongProcedure;
import net.galanthus.fullpvp.FullPvP;
import net.galanthus.fullpvp.kit.Kit;
import net.galanthus.fullpvp.utilities.GenericUtils;
import net.galanthus.fullpvp.utilities.PersistableLocation;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class BaseUser extends ServerParticipator
{
    private final List<String> addressHistories;
    private final List<NameHistory> nameHistories;
    private final TObjectIntMap<UUID> kitUseMap;
    private final TObjectLongMap<UUID> kitCooldownMap;
    private List<String> notes;
    private Location backLocation;
    private boolean messagingSounds;
    private boolean hasStarter;
    private boolean glintEnabled;
    private long lastGlintUse;
    private ChatColor chatColor = null;
    
    public BaseUser(final UUID uniqueID) {
        super(uniqueID);
        this.notes = new ArrayList<String>();
        this.hasStarter = false;
        this.addressHistories = new ArrayList<String>();
        this.nameHistories = new ArrayList<NameHistory>();
        this.glintEnabled = true;
        this.kitUseMap = new TObjectIntHashMap<UUID>();
        this.kitCooldownMap = new TObjectLongHashMap<UUID>();
    }
    
    public BaseUser(final Map<String, Object> map) {
        super(map);
        this.notes = new ArrayList<String>();
        this.addressHistories = new ArrayList<String>();
        this.nameHistories = new ArrayList<NameHistory>();
        this.glintEnabled = true;
        this.kitUseMap = new TObjectIntHashMap<UUID>();
        this.kitCooldownMap = new TObjectLongHashMap<UUID>();
        this.notes.addAll(GenericUtils.createList(map.get("notes"), String.class));
        this.addressHistories.addAll(GenericUtils.createList(map.get("addressHistories"), String.class));
        Object object = map.get("nameHistories");
        if (object != null) {
            this.nameHistories.addAll(GenericUtils.createList(object, NameHistory.class));
        }
        if ((object = map.get("backLocation")) instanceof PersistableLocation) {
            final PersistableLocation persistableLocation = (PersistableLocation)object;
            if (persistableLocation.getWorld() != null) {
                this.backLocation = ((PersistableLocation)object).getLocation();
            }
        }
        if ((object = map.get("starter")) instanceof Boolean) {
            this.hasStarter = (boolean)object;
        }
        if ((object = map.get("messagingSounds")) instanceof Boolean) {
            this.messagingSounds = (boolean)object;
        }
        if ((object = map.get("glintEnabled")) instanceof Boolean) {
            this.glintEnabled = (boolean)object;
        }
        if ((object = map.get("lastGlintUse")) instanceof String) {
            this.lastGlintUse = Long.parseLong((String)object);
        }
        for (final Map.Entry<String, Integer> entry : GenericUtils.castMap(map.get("kit-use-map"), String.class, Integer.class).entrySet()) {
            this.kitUseMap.put(UUID.fromString(entry.getKey()), (int)entry.getValue());
        }
        for (final Map.Entry<String, String> entry2 : GenericUtils.castMap(map.get("kit-cooldown-map"), String.class, String.class).entrySet()) {
            this.kitCooldownMap.put(UUID.fromString(entry2.getKey()), Long.parseLong(entry2.getValue()));
        }
    }
    
    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> map = super.serialize();
        map.put("addressHistories", this.addressHistories);
        map.put("notes", this.notes);
        map.put("starter", this.hasStarter);
        map.put("nameHistories", this.nameHistories);
        if (this.backLocation != null && this.backLocation.getWorld() != null) {
            map.put("backLocation", new PersistableLocation(this.backLocation));
        }
        map.put("messagingSounds", this.messagingSounds);
        map.put("glintEnabled", this.glintEnabled);
        map.put("lastGlintUse", Long.toString(this.lastGlintUse));
        final Map<String, Integer> kitUseSaveMap = new HashMap<String, Integer>(this.kitUseMap.size());
        this.kitUseMap.forEachEntry((uuid, value) -> {
            kitUseSaveMap.put(uuid.toString(), value);
            return true;
        });
        new TObjectIntProcedure<UUID>() {
            public boolean execute(final UUID uuid, final int value) {
                kitUseSaveMap.put(uuid.toString(), value);
                return true;
            }
        };
        final Map<String, String> kitCooldownSaveMap = new HashMap<String, String>(this.kitCooldownMap.size());
        this.kitCooldownMap.forEachEntry((uuid, value) -> {
            kitCooldownSaveMap.put(uuid.toString(), Long.toString(value));
            return true;
        });
        new TObjectLongProcedure<UUID>() {
            public boolean execute(final UUID uuid, final long value) {
                kitCooldownSaveMap.put(uuid.toString(), Long.toString(value));
                return true;
            }
        };
        map.put("kit-use-map", kitUseSaveMap);
        map.put("kit-cooldown-map", kitCooldownSaveMap);
        return map;
    }
    
    public long getRemainingKitCooldown(final Kit kit) {
        final long remaining = this.kitCooldownMap.get(kit.getUniqueID());
        if (remaining == this.kitCooldownMap.getNoEntryValue()) {
            return 0L;
        }
        return remaining - System.currentTimeMillis();
    }
    
    public void updateKitCooldown(final Kit kit) {
        this.kitCooldownMap.put(kit.getUniqueID(), System.currentTimeMillis() + kit.getDelayMillis());
    }
    
    public int getKitUses(final Kit kit) {
        final int result = this.kitUseMap.get(kit.getUniqueID());
        return (result == this.kitUseMap.getNoEntryValue()) ? 0 : result;
    }
    
    public int incrementKitUses(final Kit kit) {
        return this.kitUseMap.adjustOrPutValue(kit.getUniqueID(), 1, 1);
    }
    
    @Override
    public String getName() {
        return this.getLastKnownName();
    }

    public ChatColor getChatColor(){
        return chatColor;
    }

    public void setChatColor(ChatColor chatColor){
        if(!Objects.equals(this.chatColor, chatColor)){
            this.chatColor = chatColor;
            update();
        }
    }

    public void update(){
        FullPvP plugin = FullPvP.getPlugin();
        if (plugin == null) {
            return;
        }
        if(getUniqueId() != null){
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                plugin.getUserManager();
            });
        }
    }
    
    public List<NameHistory> getNameHistories() {
        return this.nameHistories;
    }
    
    public void tryLoggingName(final Player player) {
        Preconditions.checkNotNull(player, "Cannot log null player");
        final String playerName = player.getName();
        for (final NameHistory nameHistory : this.nameHistories) {
            if (nameHistory.getName().contains(playerName)) {
                return;
            }
        }
        this.nameHistories.add(new NameHistory(playerName, System.currentTimeMillis()));
    }
    
    public List<String> getNotes() {
        return this.notes;
    }
    
    public void setNote(final String note) {
        this.notes.add(note);
    }
    
    public boolean tryRemoveNote() {
        this.notes.clear();
        return true;
    }
    
    public List<String> getAddressHistories() {
        return this.addressHistories;
    }
    
    public void setStarterKit(final boolean val) {
        this.hasStarter = val;
    }
    
    public boolean hasStartKit() {
        return this.hasStarter;
    }
    
    public void tryLoggingAddress(final String address) {
        Preconditions.checkNotNull(address, "Cannot log null address");
        if (!this.addressHistories.contains(address)) {
            Preconditions.checkArgument(InetAddresses.isInetAddress(address), "Not an Inet address");
            this.addressHistories.add(address);
        }
    }
    
    public Location getBackLocation() {
        return (this.backLocation == null) ? null : this.backLocation.clone();
    }
    
    public void setBackLocation(final Location backLocation) {
        this.backLocation = backLocation;
    }
    
    public boolean isMessagingSounds() {
        return this.messagingSounds;
    }
    
    public void setMessagingSounds(final boolean messagingSounds) {
        this.messagingSounds = messagingSounds;
    }
    
    public boolean isGlintEnabled() {
        return this.glintEnabled;
    }
    
    public void setGlintEnabled(final boolean glintEnabled) {
        this.setGlintEnabled(glintEnabled, true);
    }
    
    public void setGlintEnabled(final boolean glintEnabled, final boolean sendUpdatePackets) {
        final Player player = this.toPlayer();
        if (player == null || !player.isOnline()) {
            return;
        }
        this.glintEnabled = glintEnabled;
        if (FullPvP.getPlugin().getServerHandler().useProtocolLib) {
            final int viewDistance = Bukkit.getViewDistance();
            final PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
            for (final Entity entity : player.getNearbyEntities(viewDistance, viewDistance, viewDistance)) {
                if (entity instanceof Item) {
                    final Item item = (Item)entity;
                    if (!(item instanceof CraftItem)) {
                        continue;
                    }
                    connection.sendPacket((Packet<?>)new PacketPlayOutEntityMetadata(entity.getEntityId(), ((CraftItem)item).getHandle().getDataWatcher(), true));
                }
                else {
                    if (!(entity instanceof Player)) {
                        continue;
                    }
                    if (entity.equals(player)) {
                        continue;
                    }
                    final Player target = (Player)entity;
                    final PlayerInventory inventory = target.getInventory();
                    final int entityID = entity.getEntityId();
                    final ItemStack[] armour = inventory.getArmorContents();
                    for (int i = 0; i < armour.length; ++i) {
                        final ItemStack stack = armour[i];
                        if (stack != null && stack.getType() != Material.AIR) {
                            connection.sendPacket((Packet<?>)new PacketPlayOutEntityEquipment(entityID, i + 1, CraftItemStack.asNMSCopy(stack)));
                        }
                    }
                    final ItemStack stack2 = inventory.getItemInHand();
                    if (stack2 == null) {
                        continue;
                    }
                    if (stack2.getType() == Material.AIR) {
                        continue;
                    }
                    connection.sendPacket((Packet<?>)new PacketPlayOutEntityEquipment(entityID, 0, CraftItemStack.asNMSCopy(stack2)));
                }
            }
        }
    }
    
    public long getLastGlintUse() {
        return this.lastGlintUse;
    }
    
    public void setLastGlintUse(final long lastGlintUse) {
        this.lastGlintUse = lastGlintUse;
    }
    
    public String getLastKnownName() {
        return ((NameHistory)Iterables.getLast(this.nameHistories)).getName();
    }
    
    public Player toPlayer() {
        return Bukkit.getPlayer(this.getUniqueId());
    }
}
