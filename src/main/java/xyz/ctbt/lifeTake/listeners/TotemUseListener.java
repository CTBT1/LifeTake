package xyz.ctbt.lifeTake.listeners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.entity.Player;
public class TotemUseListener implements Listener{
    @EventHandler
    public void onTotemUse(EntityResurrectEvent event) {
        if (event.getEntity() instanceof Player) {
            // Cancel the event to prevent the totem from working
            event.setCancelled(true);
        }
    }
}
