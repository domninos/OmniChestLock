package net.omni.chestlock.commands;

import net.omni.chestlock.ChestLockPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

public class UnlockCommand implements CommandExecutor {
    private final ChestLockPlugin plugin;

    public UnlockCommand(ChestLockPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.sendMessage(sender, plugin.getMessagesUtil().getPlayerOnly());
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 0) {
            plugin.sendMessage(player, "&cUsage: /unlock");
            return true;
        }

        if (plugin.getPlayerUtil().isUnlocking(player.getName())) {
            plugin.getPlayerUtil().removeUnlocking(player.getName());
            plugin.sendMessage(player, plugin.getMessagesUtil().getStoppedUnlocking());
            return true;
        }

        plugin.getPlayerUtil().addUnlocking(player.getName());
        plugin.sendMessage(player, plugin.getMessagesUtil().getUnlocking());
        return true;
    }

    public void register() {
        PluginCommand pluginCommand = plugin.getCommand("unlock");

        if (pluginCommand != null && !(pluginCommand.getExecutor() instanceof UnlockCommand))
            pluginCommand.setExecutor(this);
        else
            plugin.sendConsole("&cCould not register /unlock because it's not found and/or a plugin is already using /unlock.");
    }
}
