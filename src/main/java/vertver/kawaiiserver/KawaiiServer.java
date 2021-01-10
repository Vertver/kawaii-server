package vertver.kawaiiserver;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

class ServerChat {
    public static void sendMessage(Server server, String str) {
        server.getConsoleSender().sendMessage(ChatColor.GRAY + "impt_manager: " + str);
    }
}

class EnchantmentCheckerRunnable extends BukkitRunnable {
    @SuppressWarnings("unused")

    private Server server = null;
    private Inventory inventory = null;

    EnchantmentCheckerRunnable(Server server, Inventory inventory) {
        this.inventory = inventory;
        this.server = server;
    }

    private boolean checkEnchantment(ItemStack item, Enchantment elemItems) {
        return true;
    }

    private void deleteConflictedWithItems(ItemStack item) {
        for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
            if (!entry.getKey().canEnchantItem(item)) {
                item.removeEnchantment(entry.getKey());
            }
        }
    }

    private int getMaxEnchantmentValue() {
        return 5;
    }

    private void scanInventory(ItemStack item)
    {
        Map<Enchantment, Integer> mapItems = item.getEnchantments();
        deleteConflictedWithItems(item);
        for (Map.Entry<Enchantment, Integer> entry : mapItems.entrySet()) {
            if (!checkEnchantment(item, entry.getKey())) {
                ServerChat.sendMessage(server, "delete enchantment " + entry.getKey().toString() + "with " +
                        entry.getValue() + "status in " + item.getI18NDisplayName() + "item because enchantment conflict with others.");

                item.removeEnchantment(entry.getKey());
                continue;
            }

            if (entry.getValue() > getMaxEnchantmentValue()) {
                ServerChat.sendMessage(server, "delete enchantment " + entry.getKey().toString() + "with " +
                        entry.getValue() + "status in " + item.getI18NDisplayName() + "item because enchantment has over than max status value.");

                item.removeEnchantment(entry.getKey());
            }
        }
    }

    @Override
    public void run() {
        try {
            // we don't need to apply this function to players with creative
            for (HumanEntity humanEntity : inventory.getViewers()) {
                if (humanEntity.getGameMode() == GameMode.CREATIVE) {
                    return;
                }

                // scan only other chests (not ours) with items
                Inventory enderInventory = humanEntity.getEnderChest();
                if (!enderInventory.equals(inventory)) {
                    for (ItemStack item : enderInventory) {
                        scanInventory(item);
                    }
                }

                Inventory humanInventory = humanEntity.getInventory();
                if (!humanInventory.equals(inventory)) {
                    for (ItemStack item : humanInventory) {
                        scanInventory(item);
                    }
                }
            }

            for (ItemStack item : inventory.getContents()) {
                scanInventory(item);
            }
        } catch (Exception ex) {
            ServerChat.sendMessage(server, "exception in \"EnchantmentCheckerRunnable\" class: " + ex.getMessage());
        }
    }
}

class ImptovskiiUpdater {
    private KawaiiServer manager;

    ImptovskiiUpdater(KawaiiServer manager) {
        this.manager = manager;
    }

}

class SimpleCommand implements CommandExecutor {
    @SuppressWarnings("unused")
    private final KawaiiServer plugin;

    public SimpleCommand(KawaiiServer plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;
        PlayerInventory inventory = p.getInventory();

        return true;
    }
}

class ItemsCheckerRunnable implements Runnable {
    @SuppressWarnings("unused")

    private Server server = null;
    private Inventory inventory = null;

    ItemsCheckerRunnable(Server server, Inventory inventory) {
        this.inventory = inventory;
        this.server = server;
    }

    private void scanInventory(ItemStack item)
    {

    }

    @Override
    public void run() {
        try {
            if (inventory.contains(Material.BEDROCK)) {
                inventory.remove(Material.BEDROCK);
            }

            for (ItemStack item : inventory.getContents()) {
                scanInventory(item);
            }

        } catch (Exception ex) {
            ServerChat.sendMessage(server, "exception in \"ItemsCheckerRunnable\" class: " + ex.getMessage());
        }
    }
}

class ImptovskiiPlayersUpdater extends BukkitRunnable {
    private KawaiiServer manager;
    ImptovskiiPlayersUpdater(KawaiiServer manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        Server currentServer = manager.getServer();
        BukkitScheduler scheduler = currentServer.getScheduler();
        try {
            for (Player player : currentServer.getOnlinePlayers()) {
                scheduler.runTaskAsynchronously(manager, new ItemsCheckerRunnable(currentServer, player.getInventory()));
            }
        } catch (Exception ex) {
            ServerChat.sendMessage(currentServer, "exception in \"KawaiiServer\" class: " + ex.getMessage());
        }
    }
}

public final class KawaiiServer extends JavaPlugin {
    private BukkitRunnable asyncPlayerUpdater;

    @Override
    public void onEnable() {
        asyncPlayerUpdater = new ImptovskiiPlayersUpdater(this);
        asyncPlayerUpdater.runTaskTimerAsynchronously(this, 10, 1000);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
