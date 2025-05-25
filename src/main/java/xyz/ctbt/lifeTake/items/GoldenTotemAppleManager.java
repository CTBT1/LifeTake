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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;

public class GoldenTotemAppleManager implements Listener {

    private final JavaPlugin plugin;

    public GoldenTotemAppleManager(JavaPlugin plugin) {
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

    public ItemStack getGoldenTotemApple() {
        ItemStack item = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Golden Totem Apple");
            meta.setLore(List.of(ChatColor.GRAY + "Eat to quench your hunger"));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            makeUnstackable(meta);
            item.setItemMeta(meta);
        }

        item.setAmount(1);
        return item;
    }

    private void registerRecipe() {
        ItemStack result = getGoldenTotemApple();
        NamespacedKey key = new NamespacedKey(plugin, "golden_totem_apple");

        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("GGG", "GTG", "GGG");
        recipe.setIngredient('G', Material.GOLDEN_APPLE);
        recipe.setIngredient('T', Material.TOTEM_OF_UNDYING);

        Bukkit.addRecipe(recipe);
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (event.getRecipe() == null) return;
        ItemStack result = event.getRecipe().getResult();

        if (result == null || result.getType() != Material.GOLDEN_APPLE) return;

        ItemMeta meta = result.getItemMeta();
        if (meta == null || !(ChatColor.GOLD + "Golden Totem Apple").equals(meta.getDisplayName())) return;

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
    public void onGoldenTotemAppleUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.GOLDEN_APPLE) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !(ChatColor.GOLD + "Golden Totem Apple").equals(meta.getDisplayName())) return;

        event.setCancelled(true);

        // Max hunger and saturation
        player.setFoodLevel(20);
        player.setSaturation(20f);

        // Add golden apple effects (like Regeneration, Absorption etc.)
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 1)); // 4 hearts = 8 absorption health (amplifier 3 = 4*2)

        player.sendMessage(ChatColor.GREEN + "Yum!");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.0f, 1.0f);

        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
    }
}
