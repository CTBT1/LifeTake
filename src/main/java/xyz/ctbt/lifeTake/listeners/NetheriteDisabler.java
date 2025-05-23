package xyz.ctbt.lifeTake.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class NetheriteDisabler implements Listener {

    private static final Set<Material> BLOCKED_ITEMS = Set.of(
            Material.NETHERITE_HELMET,
            Material.NETHERITE_CHESTPLATE,
            Material.NETHERITE_LEGGINGS,
            Material.NETHERITE_BOOTS,
            Material.NETHERITE_SWORD
    );

    // Cancel PvP damage from Netherite Axe
    @EventHandler
    public void onNetheriteAxeDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            ItemStack weapon = player.getInventory().getItemInMainHand();
            if (weapon.getType() == Material.NETHERITE_AXE && event.getEntity() instanceof Player) {
                event.setCancelled(true); // Cancel only if hitting a player
            }
        }
    }

    // Cancel all damage from Netherite Sword
    @EventHandler
    public void onNetheriteSwordDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            ItemStack weapon = player.getInventory().getItemInMainHand();
            if (weapon.getType() == Material.NETHERITE_SWORD) {
                event.setCancelled(true);
            }
        }
    }

    // Prevent crafting Netherite armor and sword
    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        var result = event.getInventory().getResult();
        if (result != null && BLOCKED_ITEMS.contains(result.getType())) {
            event.getInventory().setResult(null);
        }
    }

    // Prevent equipping Netherite items via inventory
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack current = event.getCurrentItem();
        if (current != null && BLOCKED_ITEMS.contains(current.getType())) {
            event.setCancelled(true);
            if (event.getWhoClicked() instanceof Player player) {
                player.sendMessage("§cYou cannot use Netherite gear on this server.");
            }
        }
    }

    // Prevent equipping Netherite items via right-click
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item != null && BLOCKED_ITEMS.contains(item.getType())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYou cannot use Netherite gear on this server.");
        }
    }

    // Strip Netherite armor from players when they join
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ItemStack[] armor = player.getInventory().getArmorContents();
        boolean removed = false;

        for (int i = 0; i < armor.length; i++) {
            if (armor[i] != null && BLOCKED_ITEMS.contains(armor[i].getType())) {
                armor[i] = null;
                removed = true;
            }
        }

        if (removed) {
            player.getInventory().setArmorContents(armor);
            player.sendMessage("§cNetherite armor is disabled and has been removed.");
        }
    }
}
