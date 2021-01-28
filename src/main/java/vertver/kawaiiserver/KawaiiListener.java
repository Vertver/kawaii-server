package vertver.kawaiiserver;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.player.*;
import org.bukkit.event.*;
import java.util.Map;

public class KawaiiListener implements Listener {
    public KawaiiServer plugin;

    public KawaiiListener(KawaiiServer instance) {
        plugin = instance;
    }

    private boolean checkPlayerOutOfBounds(Player currentPlayer)
    {
        Location playerLocation = currentPlayer.getLocation();
        if (currentPlayer.getWorld().getEnvironment() == World.Environment.NETHER) {
            if (!currentPlayer.hasPermission("kawaii.antinether.bypass") || !currentPlayer.isOp()) {
                return !(playerLocation.getY() >= 128);
            }
        }

        return true;
    }

    String[] getBlockedUseItems() {
        return new String[]{ "IC2_MINING_LASER", "IMMERSIVEENGINEERING_REVOLVER" };
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().isOp() && ((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.RIGHT_CLICK_AIR))) {
            for (String idBlocked : getBlockedUseItems()) {
                ItemStack thisItem = event.getPlayer().getInventory().getItemInMainHand();
                if (thisItem != null && thisItem.getType().name().equals(idBlocked))  {
                    event.setCancelled(true);
                    ServerChat.sendMessage(plugin.getServer(), ChatColor.YELLOW + thisItem.getType().name() + " from player " + event.getPlayer().getName() + " canceled.");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e)
    {
        Player currentPlayer = e.getPlayer();
        if (!checkPlayerOutOfBounds(currentPlayer)) {
            ServerChat.sendMessage(plugin.getServer(), ChatColor.YELLOW + "player with nickname " + currentPlayer.getName() + " tried to explore hell... Well, good luck!");
            currentPlayer.setHealth(0.0);
        }
    }
}
