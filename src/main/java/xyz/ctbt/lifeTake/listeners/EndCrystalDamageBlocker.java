package xyz.ctbt.lifeTake.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import xyz.ctbt.lifeTake.Main;

public class EndCrystalDamageBlocker implements Listener {

    private final Main plugin;

    public EndCrystalDamageBlocker(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCrystalExplosion(EntityExplodeEvent event) {
        if (!plugin.isCrystalPvPEnabled() && event.getEntity().getType() == EntityType.END_CRYSTAL) {
            event.setCancelled(true); // Prevents block damage
        }
    }

    @EventHandler
    public void onCrystalDamage(EntityDamageEvent event) {
        if (!plugin.isCrystalPvPEnabled() &&
                event.getCause() == DamageCause.ENTITY_EXPLOSION &&
                event.getEntity().getWorld().getNearbyEntities(event.getEntity().getLocation(), 5, 5, 5)
                        .stream().anyMatch(e -> e instanceof EnderCrystal)) {
            event.setCancelled(true); // Prevents damage from crystals
        }
    }

    @EventHandler
    public void onCrystalKnockback(EntityDamageByEntityEvent event) {
        if (!plugin.isCrystalPvPEnabled() &&
                event.getDamager().getType() == EntityType.END_CRYSTAL) {
            event.setCancelled(true); // Prevents knockback caused by the crystal itself
        }
    }
}
