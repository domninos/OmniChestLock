package net.omni.chestlock.util;

import net.omni.chestlock.ChestLockPlugin;

import java.util.List;

public class MessagesUtil {

    private final ChestLockPlugin plugin;

    private String prefix;
    private String playerOnly;
    private String playerNotFound;
    private String locking;
    private String stoppedLocking;
    private String unlocking;
    private String stoppedUnlocking;
    private String checking;
    private String stoppedChecking;
    private String needTurningOff;
    private String alreadyLocked;
    private String locked;
    private String unlocked;
    private String memberOnlyLock;
    private String memberOnlyUnlock;
    private String ownerOnlyUnlock;
    private String notLocked;
    private String checkFormat;
    private String addedPlayer;
    private String removedPlayer;
    private String onlyChest;
    private String cantOpenChest;
    private String staffOpen;
    private String playerNotMember;

    public MessagesUtil(ChestLockPlugin plugin) {
        this.plugin = plugin;

        load();
    }

    public void load() {
        this.prefix = plugin.translate(getString("prefix"));
        this.playerOnly = plugin.translate(getString("playerOnly"));
        this.playerNotFound = plugin.translate(getString("playerNotFound"));
        this.locking = plugin.translate(getString("locking"));
        this.stoppedLocking = plugin.translate(getString("stoppedLocking"));
        this.unlocking = plugin.translate(getString("unlocking"));
        this.stoppedUnlocking = plugin.translate(getString("stoppedUnlocking"));
        this.checking = plugin.translate(getString("checking"));
        this.stoppedChecking = plugin.translate(getString("stoppedChecking"));
        this.needTurningOff = plugin.translate(getString("needTurningOff"));
        this.alreadyLocked = plugin.translate(getString("alreadyLocked"));
        this.locked = plugin.translate(getString("locked"));
        this.unlocked = plugin.translate(getString("unlocked"));
        this.memberOnlyLock = plugin.translate(getString("memberOnlyLock"));
        this.memberOnlyUnlock = plugin.translate(getString("memberOnlyUnlock"));
        this.ownerOnlyUnlock = plugin.translate(getString("ownerOnlyUnlock"));
        this.notLocked = plugin.translate(getString("notLocked"));

        List<String> checkFormat = plugin.getConfig().getStringList("messages.checkFormat");

        this.checkFormat = plugin.translate(String.join("\n", checkFormat));
        this.addedPlayer = plugin.translate(getString("addedPlayer"));
        this.removedPlayer = plugin.translate(getString("removedPlayer"));
        this.onlyChest = plugin.translate(getString("onlyChest"));
        this.cantOpenChest = plugin.translate(getString("cantOpenChest"));
        this.staffOpen = plugin.translate(getString("staffOpen"));
        this.playerNotMember = plugin.translate(getString("playerNotMember"));
    }

    public String getPrefix() {
        return prefix;
    }

    public String getPlayerOnly() {
        return playerOnly;
    }

    public String getPlayerNotFound() {
        return playerNotFound;
    }

    public String getLocking() {
        return locking;
    }

    public String getStoppedLocking() {
        return stoppedLocking;
    }

    public String getUnlocking() {
        return unlocking;
    }

    public String getStoppedUnlocking() {
        return stoppedUnlocking;
    }

    public String getChecking() {
        return checking;
    }

    public String getStoppedChecking() {
        return stoppedChecking;
    }

    public String getNeedTurningOff(String command) {
        return needTurningOff.replace("%command%", command);
    }

    public String getAlreadyLocked() {
        return alreadyLocked;
    }

    public String getLocked() {
        return locked;
    }

    public String getUnlocked() {
        return unlocked;
    }

    public String getMemberOnlyLock() {
        return memberOnlyLock;
    }

    public String getMemberOnlyUnlock() {
        return memberOnlyUnlock;
    }

    public String getOwnerOnlyUnlock() {
        return ownerOnlyUnlock;
    }

    public String getNotLocked() {
        return notLocked;
    }

    public String getCheckFormat(int x, int y, int z, String owner, String users) {
        return checkFormat.replace("{x}", String.valueOf(x))
                .replace("{y}", String.valueOf(y))
                .replace("{z}", String.valueOf(z))
                .replace("{owner}", owner)
                .replace("{users}", users);
    }

    public String getAddedPlayer(String added) {
        return addedPlayer.replace("{player}", added);
    }

    public String getRemovedPlayer(String removed) {
        return removedPlayer.replace("{player}", removed);
    }

    public String getOnlyChest() {
        return onlyChest;
    }

    public String getCantOpenChest() {
        return cantOpenChest;
    }

    public String getStaffOpen() {
        return staffOpen;
    }

    public String getPlayerNotMember() {
        return playerNotMember;
    }

    private String getString(String path) {
        return plugin.getConfig().getString("messages." + path);
    }
}
