package net.omni.chestlock.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerUtil {
    private final List<String> lockingPlayers = new ArrayList<>();
    private final List<String> unlockingPlayers = new ArrayList<>();
    private final List<String> checkingPlayers = new ArrayList<>();
    private final Map<String, String> addingPlayers = new HashMap<>();
    private final Map<String, String> removingPlayers = new HashMap<>();

    public void addLocking(String player) {
        if (!isLocking(player))
            lockingPlayers.add(player);
    }

    public boolean isLocking(String player) {
        return lockingPlayers.contains(player);
    }

    public void removeLocking(String player) {
        if (isLocking(player))
            lockingPlayers.remove(player);
    }

    public void addUnlocking(String player) {
        if (!isUnlocking(player))
            unlockingPlayers.add(player);
    }

    public boolean isUnlocking(String player) {
        return unlockingPlayers.contains(player);
    }

    public void removeUnlocking(String player) {
        if (isUnlocking(player))
            unlockingPlayers.remove(player);
    }

    public void addChecking(String player) {
        if (!isChecking(player))
            checkingPlayers.add(player);
    }

    public boolean isChecking(String player) {
        return checkingPlayers.contains(player);
    }

    public void removeChecking(String player) {
        if (isChecking(player))
            checkingPlayers.remove(player);
    }

    public void addAdding(String player, String adding) {
        addingPlayers.put(player, adding);
    }

    public void addRemoving(String player, String removing) {
        removingPlayers.put(player, removing);
    }

    public void flush() {
        lockingPlayers.clear();
        unlockingPlayers.clear();
        checkingPlayers.clear();
        addingPlayers.clear();
        removingPlayers.clear();
    }
}
