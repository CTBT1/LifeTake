package xyz.ctbt.lifeTake.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DamageImmunityListener implements Listener {

    private final JavaPlugin plugin;

    public DamageImmunityListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if (player.hasMetadata("damageImmune")) {
            event.setCancelled(true);
            player.sendMessage("§bYou're immune to damage!");
        }
    }
}
