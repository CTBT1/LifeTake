package xyz.ctbt.lifeTake.items;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public class TotemAppleManager implements Listener {

    private final JavaPlugin plugin;

    public TotemAppleManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        registerRecipe();
    }

    private void makeUnstackable(ItemMeta meta) {
        NamespacedKey key = new NamespacedKey(plugin, "unique_id");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, UUID.randomUUID().toString());
    }

    public ItemStack getTotemApple() {
        ItemStack item = new ItemStack(Material.APPLE);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Totem Apple");
            meta.setLore(List.of(ChatColor.GRAY + "Eat to fully restore hunger and saturation."));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            makeUnstackable(meta);
            item.setItemMeta(meta);
        }

        item.setAmount(1); // Ensure only 1 per item
        return item;
    }

    private void registerRecipe() {
        ItemStack result = getTotemApple();
        NamespacedKey key = new NamespacedKey(plugin, "totem_apple");

        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("AAA", "ATA", "AAA");
        recipe.setIngredient('A', Material.APPLE);
        recipe.setIngredient('T', Material.TOTEM_OF_UNDYING);

        Bukkit.addRecipe(recipe);
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (event.getRecipe() == null) return;
        ItemStack result = event.getRecipe().getResult();

        if (result == null || result.getType() != Material.APPLE) return;

        ItemMeta meta = result.getItemMeta();
        if (!(ChatColor.GOLD + "Totem Apple").equals(meta.getDisplayName())) return;

        // Clone the result and assign new unique ID to make it unstackable
        ItemStack newResult = result.clone();
        ItemMeta newMeta = newResult.getItemMeta();

        if (newMeta != null) {
            makeUnstackable(newMeta);
            newResult.setItemMeta(newMeta);
            newResult.setAmount(1);
            event.getInventory().setResult(newResult);
        }
    }

    @EventHandler
    public void onTotemAppleUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.APPLE) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !(ChatColor.GOLD + "Totem Apple").equals(meta.getDisplayName())) return;

        // Cancel the normal eating action and apply effects manually
        event.setCancelled(true);

        player.setFoodLevel(20);
        player.setSaturation(20f);
        player.sendMessage(ChatColor.GREEN + "Your hunger and saturation have been fully restored!");
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1.0f, 1.0f);

        // Consume one apple
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
    }
}
