package xyz.ctbt.lifeTake.listeners;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.ctbt.lifeTake.Main;
import xyz.ctbt.lifeTake.data.PlayerDataManager;
import xyz.ctbt.lifeTake.util.TablistManager;


public class PlayerKillListener implements Listener {
    private final Main plugin;
    private final PlayerDataManager dataManager;

    public PlayerKillListener(Main plugin, PlayerDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    private void dropTokenAtLocation(Player victim) {
        ItemStack token = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = token.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Life Token");
            token.setItemMeta(meta);
        }

        // Drop the token at the victim's death location
        var world = victim.getWorld();
        var location = victim.getLocation();
        var droppedItem = world.dropItemNaturally(location, token);

        // Make it resistant to fire, lava, explosions, etc.
        droppedItem.setInvulnerable(true);         // Immune to explosions, etc.
        droppedItem.setFireTicks(0);               // Not burning
        droppedItem.setGlowing(true);              // Optional: makes it visible
        droppedItem.setPickupDelay(20);            // Slight delay before pickup
    }


    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        dropTokenAtLocation(victim);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!victim.isOnline()) return;

            // Handle heart loss
            var attr = victim.getAttribute(Attribute.MAX_HEALTH);
            if (attr != null) {
                double currentMax = attr.getBaseValue();
                if (currentMax > 20.0) {
                    attr.setBaseValue(currentMax - 2.0);
                    victim.setHealth(attr.getBaseValue());
                    victim.sendMessage(ChatColor.RED + "You lost a heart! Max hearts: " + (int)(attr.getBaseValue() / 2));
                    TablistManager.updatePlayer(plugin, victim);
                } else {
                    victim.sendMessage(ChatColor.YELLOW + "You are at the minimum heart limit (10 hearts).");
                }
            }

            // Handle life loss
            int lives = PlayerDataManager.getLives(plugin, victim);

            if (lives <= 1) {
                victim.kickPlayer(ChatColor.DARK_RED + "You have run out of lives.");
                Bukkit.getBanList(BanList.Type.NAME).addBan(victim.getName(), "Out of lives", null, null);
            } else {
                PlayerDataManager.setLives(plugin, victim, lives - 1);
                victim.sendMessage(ChatColor.RED + "You lost a life. Lives remaining: " + (lives - 1));
                TablistManager.updatePlayer(plugin, victim);
            }
        }, 20L);
    }
}
