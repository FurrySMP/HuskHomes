package net.william278.huskhomes.hook;

import net.william278.huskhomes.HuskHomes;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tfagaming.projects.minecraft.homestead.managers.ChunkManager;
import tfagaming.projects.minecraft.homestead.structure.Region;

@PluginHook(
        name = "Homestead",
        register = PluginHook.Register.ON_ENABLE
)
public class HomesteadHook extends Hook {

    public HomesteadHook(@NotNull HuskHomes plugin) {
        super(plugin);
    }

    @Override
    public void load() {
    }

    @Override
    public void unload() {
    }

    public boolean isBlockedByHomestead(@NotNull Player player, @NotNull Location location) {
        if (location.getWorld() == null) return false;
        final tfagaming.projects.minecraft.homestead.structure.serializable.SerializableChunk sChunk = 
            new tfagaming.projects.minecraft.homestead.structure.serializable.SerializableChunk(location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4);
            
        for (Region region : tfagaming.projects.minecraft.homestead.managers.RegionManager.getAll()) {
            if (tfagaming.projects.minecraft.homestead.managers.ChunkManager.isChunkClaimedByRegion(region, sChunk)) {
                return !region.isOwner(player) && !region.isPlayerMember(player);
            }
        }
        return false;
    }
}
