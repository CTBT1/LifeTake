package xyz.ctbt.lifeTake.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.ctbt.lifeTake.util.GlobalPvPManager;
import xyz.ctbt.lifeTake.util.PvPImmunityManager;

public class PvPDamageListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        // Block PvP globally if disabled
        if (!GlobalPvPManager.isPvPEnabled()) {
            event.setCancelled(true);
            return;
        }

        // Block PvP if either player is immune
        if (PvPImmunityManager.isImmune(victim) || PvPImmunityManager.isImmune(attacker)) {
            event.setCancelled(true);
        }
    }
}
