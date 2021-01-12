package vertver.kawaiiserver;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
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
        if (!currentPlayer.isOp() || !currentPlayer.hasPermission("kawaii.antinether.bypass"))
        {
            if (currentPlayer.getWorld().getEnvironment() == World.Environment.NETHER)
            {
                return !(currentPlayer.getLocation().getY() >= 128);
            }
        }

        return true;
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
