package net.omni.chestlock.util;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtil {
    private final List<String> checkingPlayers = new ArrayList<>();

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
        checkingPlayers.clear();
    }
}
