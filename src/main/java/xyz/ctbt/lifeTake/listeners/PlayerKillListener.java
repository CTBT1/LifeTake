package xyz.ctbt.lifeTake.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.ctbt.lifeTake.Main;
import xyz.ctbt.lifeTake.util.PvPImmunityManager;

public class PlayerKillListener implements Listener {
    private final Main plugin;

    public PlayerKillListener(Main plugin) {
        this.plugin = plugin;
    }

    private void dropTokenAtLocation(Player victim) {
        ItemStack token = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = token.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Life Token");
            token.setItemMeta(meta);
        }

        var world = victim.getWorld();
        var location = victim.getLocation();
        var droppedItem = world.dropItemNaturally(location, token);

        droppedItem.setInvulnerable(true);
        droppedItem.setFireTicks(0);
        droppedItem.setGlowing(true);
        droppedItem.setPickupDelay(20);
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        dropTokenAtLocation(victim);

        // Only apply PvP immunity — no lives/hearts here!
        PvPImmunityManager.grantImmunity(victim, 18000L); // 15 mins in ticks
    }
}
