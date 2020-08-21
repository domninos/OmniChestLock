package net.omni.chestlock;

import net.omni.chestlock.commands.LockCommand;
import net.omni.chestlock.commands.UnlockCommand;
import net.omni.chestlock.listeners.PlayerListener;
import net.omni.chestlock.lockedchests.LockedChestsConfig;
import net.omni.chestlock.lockedchests.LockedChestsManager;
import net.omni.chestlock.util.MessagesUtil;
import net.omni.chestlock.util.PlayerUtil;
import net.omni.chestlock.util.WorldGuardUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestLockPlugin extends JavaPlugin {

    private MessagesUtil messagesUtil;
    private PlayerUtil playerUtil;
    private LockedChestsManager lockedChestsManager;
    private LockedChestsConfig lockedChestsConfig;
    private WorldGuardUtil worldGuardUtil;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.messagesUtil = new MessagesUtil(this);
        this.playerUtil = new PlayerUtil();
        this.lockedChestsConfig = new LockedChestsConfig(this);
        this.lockedChestsManager = new LockedChestsManager(this);

        this.worldGuardUtil = new WorldGuardUtil(this);

        worldGuardUtil.init();

        registerCommands();
        registerListeners();

        sendConsole("&aSuccessfully enabled ChestLock v" + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        lockedChestsManager.flush();
        playerUtil.flush();
        sendConsole("&aSuccessfully disabled ChestLock");
    }

    public void sendConsole(String message) {
        sendMessage(Bukkit.getConsoleSender(), message);
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(translate(messagesUtil.getPrefix() + "&r " + message));
    }

    public String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private void registerCommands() {
        new LockCommand(this).register();
        new UnlockCommand(this).register();
    }

    private void registerListeners() {
        new PlayerListener(this).register();
    }


    public MessagesUtil getMessagesUtil() {
        return messagesUtil;
    }

    public PlayerUtil getPlayerUtil() {
        return playerUtil;
    }

    public LockedChestsManager getLockedChestsManager() {
        return lockedChestsManager;
    }

    public LockedChestsConfig getLockedChestsConfig() {
        return lockedChestsConfig;
    }

    public WorldGuardUtil getWorldGuardUtil() {
        return worldGuardUtil;
    }
}
