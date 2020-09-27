package net.omni.chestlock.listeners;

import net.omni.chestlock.ChestLockPlugin;
import net.omni.chestlock.lockedchests.LockedChest;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final ChestLockPlugin plugin;

    public PlayerListener(ChestLockPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onOpenChest(PlayerInteractEvent event) {
        if (!event.getAction().name().startsWith("RIGHT_CLICK"))
            return;

        Block block = event.getClickedBlock();

        if (block == null || block.getType() == Material.AIR)
            return;

        if (block.getType() != Material.CHEST)
            return;

        if (!(block.getState() instanceof Chest))
            return;

        Player player = event.getPlayer();
        Location location = block.getLocation();
        String name = player.getName();

        if (plugin.getLockedChestsManager().isLockedChest(location)) {
            if (player.isOp())
                plugin.sendMessage(player, plugin.getMessagesUtil().getCannotOpenOp());
            else if (!player.hasPermission("chestlock.bypass")) { // no staff perms
                if (!plugin.getLockedChestsManager().isPlayer(location, player.getName())
                        && !plugin.getLockedChestsManager().isOwner(location, player.getName())) {
                    event.setCancelled(true);
                    player.closeInventory();
                    plugin.sendMessage(player, plugin.getMessagesUtil().getCantOpenChest());
                }
            } else
                plugin.sendMessage(player, plugin.getMessagesUtil().getStaffOpen());
        }

        if (plugin.getPlayerUtil().isChecking(name)) {
            event.setCancelled(true);
            player.closeInventory();

            if (!plugin.getLockedChestsManager().isLockedChest(location)) {
                plugin.sendMessage(player, plugin.getMessagesUtil().getNotLocked());
                return;
            }

            LockedChest lockedChest = plugin.getLockedChestsManager().getLockedChest(location);

            plugin.sendMessage(player,
                    plugin.getMessagesUtil().getCheckFormat(lockedChest.getX(),
                            lockedChest.getY(), lockedChest.getZ(), lockedChest.getOwner(),
                            StringUtils.join(lockedChest.getPlayers(), ", ")));
            plugin.getPlayerUtil().removeChecking(name);
            plugin.sendMessage(player, plugin.getMessagesUtil().getStoppedChecking());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (plugin.getLockedChestsManager().isLockedChest(block.getLocation())) {
            if (player.isOp()) {
                event.setCancelled(true);
                plugin.sendMessage(player, plugin.getMessagesUtil().getCannotBreakOp());
                return;
            }

            if (plugin.getLockedChestsManager().isOwner(block.getLocation(), player.getName())
                    || plugin.getLockedChestsManager().isPlayer(block.getLocation(), player.getName())) {
                plugin.getLockedChestsManager().unlockChest(block.getLocation(), player.getName());
                plugin.sendMessage(player, plugin.getMessagesUtil().getUnlocked());
            } else {
                event.setCancelled(true);
                plugin.sendMessage(player, plugin.getMessagesUtil().getOwnerOnlyUnlock());
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (plugin.getPlayerUtil().isChecking(player.getName()))
            plugin.getPlayerUtil().removeChecking(player.getName());
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
