package net.omni.chestlock.commands;

import net.omni.chestlock.ChestLockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

public class LockCommand implements CommandExecutor {
    private final ChestLockPlugin plugin;

    public LockCommand(ChestLockPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.sendMessage(sender, plugin.getMessagesUtil().getPlayerOnly());
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            if (plugin.getPlayerUtil().isUnlocking(player.getName())) {
                plugin.sendMessage(player, plugin.getMessagesUtil().getNeedTurningOff("unlock"));
                return true;
            }

            if (plugin.getPlayerUtil().isLocking(player.getName())) {
                plugin.getPlayerUtil().removeLocking(player.getName());
                plugin.sendMessage(player, plugin.getMessagesUtil().getStoppedLocking());
                return true;
            }

            plugin.getPlayerUtil().addLocking(player.getName());
            plugin.sendMessage(player, plugin.getMessagesUtil().getLocking());
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("add"))
                plugin.sendMessage(player, "&cUsage: /lock add <player>");
            else if (args[0].equalsIgnoreCase("remove"))
                plugin.sendMessage(player, "&cUsage: /lock remove <player>");
            else if (args[0].equalsIgnoreCase("list")) {
                if (plugin.getPlayerUtil().isChecking(player.getName())) {
                    plugin.getPlayerUtil().removeChecking(player.getName());
                    plugin.sendMessage(player, plugin.getMessagesUtil().getStoppedChecking());
                    return true;
                }

                plugin.getPlayerUtil().addChecking(player.getName());
                plugin.sendMessage(player, plugin.getMessagesUtil().getChecking());
            } else
                plugin.sendMessage(player, "&cUsage: /lock <add|remove|list> <player>");

            return true;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    plugin.sendMessage(player, plugin.getMessagesUtil().getPlayerNotFound());
                    return true;
                }

                Block targetBlock = player.getTargetBlock(null, 5);

                if (targetBlock.getType() != Material.CHEST) {
                    plugin.sendMessage(player, plugin.getMessagesUtil().getOnlyChest());
                    return true;
                }

                if (!plugin.getLockedChestsManager().isLockedChest(targetBlock.getLocation())) {
                    plugin.sendMessage(player, plugin.getMessagesUtil().getNotLocked());
                    return true;
                }

                plugin.getLockedChestsManager().addPlayer(targetBlock.getLocation(), target.getName());
                plugin.sendMessage(player, plugin.getMessagesUtil().getAddedPlayer(target.getName()));
            } else if (args[0].equalsIgnoreCase("remove")) {
                String toRemove = args[1];
                Player target = Bukkit.getPlayer(toRemove);
                boolean found = true;

                if (target == null)
                    found = false;

                Block targetBlock = player.getTargetBlock(null, 5);

                if (targetBlock.getType() != Material.CHEST) {
                    plugin.sendMessage(player, plugin.getMessagesUtil().getOnlyChest());
                    return true;
                }

                if (!plugin.getLockedChestsManager().isLockedChest(targetBlock.getLocation())) {
                    plugin.sendMessage(player, plugin.getMessagesUtil().getNotLocked());
                    return true;
                }

                if (!plugin.getLockedChestsManager().isOwner(targetBlock.getLocation(), player.getName())) {
                    plugin.sendMessage(player, plugin.getMessagesUtil().getOwnerOnlyUnlock());
                    return true;
                }

                if (found)
                    toRemove = target.getName();

                if (!plugin.getLockedChestsManager().isPlayer(targetBlock.getLocation(), toRemove)
                        || plugin.getLockedChestsManager().isOwner(targetBlock.getLocation(), toRemove)) {
                    plugin.sendMessage(player, plugin.getMessagesUtil().getPlayerNotMember());
                    return true;
                }

                plugin.getLockedChestsManager().removePlayer(targetBlock.getLocation(), toRemove);
                plugin.sendMessage(player, plugin.getMessagesUtil().getRemovedPlayer(toRemove));
            } else
                plugin.sendMessage(player, "&cUsage: /lock <add|remove> <player>");

            return true;
        }
        return true;
    }

    public void register() {
        PluginCommand pluginCommand = plugin.getCommand("lock");

        if (pluginCommand != null && !(pluginCommand.getExecutor() instanceof LockCommand))
            pluginCommand.setExecutor(this);
        else
            plugin.sendConsole("&cCould not register /lock because it's not found and/or a plugin is already using /lock.");
    }
}
