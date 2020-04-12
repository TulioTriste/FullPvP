package net.galanthus.fullpvp.staffmode;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import org.bukkit.entity.Player;

public enum StaffPriority {
    OWNER(6), HEADADMIN(5), STAFFMANAGER(4), ADMIN(3), MODERATOR(2), TRIAL(1), NONE(0);

    private static final ImmutableMap<Integer, StaffPriority> BY_ID;

    public static StaffPriority of(final int level) {
        return (StaffPriority) StaffPriority.BY_ID.get((Object) level);
    }

    public static StaffPriority of(final Player player) {
        for (final StaffPriority staffPriority : values()) {
            if (player.hasPermission("staffpriority." + staffPriority.priorityLevel)) {
                return staffPriority;
            }
        }
        return StaffPriority.NONE;
    }

    static {
        final ImmutableMap.Builder<Integer, StaffPriority> builder = (ImmutableMap.Builder<Integer, StaffPriority>) new Builder<Integer, StaffPriority>();
        for (final StaffPriority staffPriority : values()) {
            builder.put(staffPriority.priorityLevel, staffPriority);
        }
        BY_ID = builder.build();
    }

    private final int priorityLevel;

    private StaffPriority(final int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public int getPriorityLevel() {
        return this.priorityLevel;
    }

    public boolean isMoreThan(final StaffPriority other) {
        return this.priorityLevel > other.priorityLevel;
    }
}
