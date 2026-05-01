package net.william278.huskhomes.hook;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import net.william278.huskhomes.HuskHomes;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

@PluginHook(
        name = "WorldGuard",
        register = PluginHook.Register.ON_LOAD
)
public class WorldGuardHook extends Hook {

    public static StateFlag HUSKHOMES_SETHOME_DENY;

    public WorldGuardHook(@NotNull HuskHomes plugin) {
        super(plugin);
    }

    @Override
    public void load() {
        final FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            final StateFlag flag = new StateFlag("huskhomes-sethome-denied", false);
            registry.register(flag);
            HUSKHOMES_SETHOME_DENY = flag;
        } catch (FlagConflictException e) {
            final com.sk89q.worldguard.protection.flags.Flag<?> existing = registry.get("huskhomes-sethome-denied");
            if (existing instanceof StateFlag) {
                HUSKHOMES_SETHOME_DENY = (StateFlag) existing;
            } else {
                plugin.log(Level.WARNING, "WorldGuard flag conflict for 'huskhomes-sethome-denied'. Cannot use.");
            }
        }
    }

    @Override
    public void unload() {
    }

    public boolean isBlockedByWorldGuard(@NotNull Player player, @NotNull Location location) {
        if (HUSKHOMES_SETHOME_DENY == null) {
            return false;
        }
        final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        final RegionQuery query = container.createQuery();
        final StateFlag.State state = query.queryState(BukkitAdapter.adapt(location), null, HUSKHOMES_SETHOME_DENY);
        return state == StateFlag.State.DENY;
    }
}
