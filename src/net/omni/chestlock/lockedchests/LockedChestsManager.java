package net.omni.chestlock.lockedchests;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.omni.chestlock.ChestLockPlugin;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LockedChestsManager {
    private final List<LockedChest> lockedChests = new ArrayList<>();

    private final ChestLockPlugin plugin;

    public LockedChestsManager(ChestLockPlugin plugin) {
        this.plugin = plugin;

        load();
    }

    public void load() {
        ConfigurationSection section = plugin.getLockedChestsConfig().getConfig().getConfigurationSection("lockedChests");

        if (section == null) {
            plugin.sendConsole("&cCould not load locked chests. Configuration Section not found.");
            return;
        }

        for (String owner : section.getKeys(false)) {
            if (owner == null)
                continue;

            String path = "lockedChests." + owner + ".";

            String locationString = plugin.getLockedChestsConfig().getString(path + "location");

            if (locationString == null) {
                plugin.sendConsole("&cCould not find location.");
                continue;
            }

            String[] split = locationString.split(",");

            if (split.length <= 2) {
                plugin.sendConsole("&cCould not find location.");
                continue;
            }

            World world = Bukkit.getWorld(split[0]);

            if (world == null) {
                plugin.sendConsole("&cCould not find world '" + split[0] + "'");
                continue;
            }

            int x;
            int y;
            int z;

            try {
                x = Integer.parseInt(split[1]);
                y = Integer.parseInt(split[2]);
                z = Integer.parseInt(split[3]);
            } catch (NumberFormatException e) {
                plugin.sendConsole("&cSomething went wrong getting location of " + owner + "'s locations.");
                continue;
            }

            LockedChest lockedChest = new LockedChest(owner, world.getName(), x, y, z);

            for (String user : plugin.getLockedChestsConfig().getConfig().getStringList(path + "users")) {
                if (user != null)
                    lockedChest.getPlayers().add(user);
            }

            lockedChests.add(lockedChest);
        }

        plugin.sendConsole("&aSuccessfully loaded locked chests.");
    }

    public void createLockedChest(Location location, String owner) {
        if (isLockedChest(location))
            return;

        Validate.notNull(location.getWorld());

        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        lockedChests.add(new LockedChest(owner, world.getName(), x, y, z));

        plugin.getLockedChestsConfig().setNoSave("lockedChests." + owner + ".location",
                world.getName() + "," + x + "," + y + "," + z);
        plugin.getLockedChestsConfig().setNoSave("lockedChests." + owner + ".users", Lists.newArrayList());
        plugin.getLockedChestsConfig().save();

        plugin.sendConsole("&aCreated locked chest at " + world.getName() + " | X: " + x + " | Y: " + y + " | Z: " + z);
    }

    public void unlockChest(Location location, String owner) {
        if (!isOwner(location, owner))
            return;

        if (!isLockedChest(location))
            return;

        LockedChest lockedChest = getLockedChest(location);

        if (lockedChest == null)
            return;

        lockedChests.remove(lockedChest);
        plugin.getLockedChestsConfig().set("lockedChests." + owner, null);
    }

    public boolean isOwner(Location location, String player) {
        String owner = getOwner(location);

        return owner != null && owner.equals(player);
    }

    public String getOwner(Location location) {
        if (!isLockedChest(location))
            return null;

        String owner = null;

        for (LockedChest lockedChest : lockedChests) {
            if (lockedChest == null)
                continue;

            if (lockedChest.getX() == location.getBlockX()
                    && lockedChest.getY() == location.getBlockY()
                    && lockedChest.getZ() == location.getBlockZ())
                owner = lockedChest.getOwner();
        }

        return owner;
    }

    public void addPlayer(Location location, String player) {
        LockedChest lockedChest = getLockedChest(location);

        if (lockedChest != null)
            lockedChest.getPlayers().add(player);
    }

    public void removePlayer(Location location, String player) {
        LockedChest lockedChest = getLockedChest(location);

        if (lockedChest != null)
            lockedChest.getPlayers().remove(player);
    }

    public boolean isPlayer(Location location, String player) {
        LockedChest lockedChest = getLockedChest(location);

        if (lockedChest != null)
            return lockedChest.isPlayer(player);
        else
            return false;
    }

    public boolean isLockedChest(Location location) {
        return getLockedChest(location) != null;
    }

    public LockedChest getLockedChest(Location location) {
        return lockedChests.stream().filter(chest -> chest.getX() == location.getBlockX()
                && chest.getY() == location.getBlockY() && chest.getZ() == location.getBlockZ()).findFirst().
                orElse(null);
    }

    public Set<LockedChest> getLockedChestsOf(Player player) {
        if (player == null)
            return Sets.newHashSet();

        return lockedChests.stream().filter(chest -> chest.getOwner().equals(player.getName())).collect(Collectors.toSet());
    }

    public void flush() {
        for (LockedChest lockedChest : lockedChests) {
            if (lockedChest == null)
                continue;

            plugin.getLockedChestsConfig().setNoSave("lockedChests." + lockedChest.getOwner() + ".users",
                    new ArrayList<>(lockedChest.getPlayers()));

            lockedChest.flush();
        }

        plugin.getLockedChestsConfig().save();

        lockedChests.clear();
    }
}
