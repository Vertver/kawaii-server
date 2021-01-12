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
import java.util.Map;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

class ServerChat {
    public static void sendMessage(Server server, String str) {
        server.broadcastMessage(ChatColor.GRAY + "kawaii_manager: " + str);
    }
}

public final class KawaiiServer extends JavaPlugin {
    private final KawaiiListener baseListener = new KawaiiListener(this);

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(baseListener, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
