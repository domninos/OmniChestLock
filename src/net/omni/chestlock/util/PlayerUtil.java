package net.omni.chestlock.util;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtil {
    private final List<String> unlockingPlayers = new ArrayList<>();
    private final List<String> checkingPlayers = new ArrayList<>();

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

    public void flush() {
        unlockingPlayers.clear();
        checkingPlayers.clear();
    }
}
