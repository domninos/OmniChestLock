package net.omni.chestlock.lockedchests;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;

public class LockedChest {
    private final Set<String> players = new HashSet<>();
    private final String owner;
    private final World worldInstance;
    private final Location location;
    private final String world;
    private final int x;
    private final int y;
    private final int z;

    public LockedChest(String owner, String world, int x, int y, int z) {
        this.owner = owner;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;

        this.worldInstance = Bukkit.getWorld(world);

        Validate.notNull(worldInstance);

        this.location = new Location(worldInstance, x, y, z);
    }

    public String getOwner() {
        return owner;
    }

    public Location getLocation() {
        return location;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }

    public World getWorldInstance() {
        return worldInstance;
    }

    public boolean isPlayer(String player) {
        return players.contains(player);
    }

    public Set<String> getPlayers() {
        return players;
    }

    public void flush() {
        players.clear();
    }
}
