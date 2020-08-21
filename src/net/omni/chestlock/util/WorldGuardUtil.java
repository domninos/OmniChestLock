package net.omni.chestlock.util;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import net.omni.chestlock.ChestLockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardUtil {

    private final ChestLockPlugin plugin;
    private boolean hooked = false;

    public WorldGuardUtil(ChestLockPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        if (Bukkit.getPluginManager().isPluginEnabled("WorldEdit")
                && Bukkit.getPluginManager().isPluginEnabled("WorldGuard"))
            this.hooked = true;

        if (!isHooked())
            plugin.sendConsole("&cCould not hook into WorldEdit/WorldGuard.");
    }

    public boolean isMember(Player player, Location location) {
        if (!isHooked())
            return false;

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(location);

        for (ProtectedRegion region : query.getApplicableRegions(loc).getRegions()) {
            if (region != null)
                return region.isMember(localPlayer);
        }

        return false;
    }

    public boolean isOwner(Player player, Location location) {
        if (!isHooked())
            return false;

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(location);

        for (ProtectedRegion region : query.getApplicableRegions(loc).getRegions()) {
            if (region != null)
                return region.isOwner(localPlayer);
        }

        return false;
    }

    public boolean isHooked() {
        return hooked;
    }
}
