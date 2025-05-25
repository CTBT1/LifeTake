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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;

public class EnchantedGoldenTotemAppleManager implements Listener {

    private final JavaPlugin plugin;
    private final NamespacedKey identifierKey;

    private static final String ITEM_NAME = ChatColor.LIGHT_PURPLE + "Enchanted Golden Totem Apple";
    private static final String RAW_ITEM_NAME = ChatColor.stripColor(ITEM_NAME);

    public EnchantedGoldenTotemAppleManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.identifierKey = new NamespacedKey(plugin, "totem_apple_id");
    }

    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        registerRecipe();
    }

    private void makeUnstackable(ItemMeta meta) {
        meta.getPersistentDataContainer().set(identifierKey, PersistentDataType.STRING, UUID.randomUUID().toString());
    }

    private boolean isCustomTotemApple(ItemStack item) {
        if (item == null || item.getType() != Material.ENCHANTED_GOLDEN_APPLE) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        return meta.getPersistentDataContainer().has(identifierKey, PersistentDataType.STRING);
    }

    public ItemStack getEnchantedGoldenTotemApple() {
        ItemStack item = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ITEM_NAME);
            meta.setLore(List.of(ChatColor.GRAY + "Eat to gain cool effects"));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            makeUnstackable(meta);
            item.setItemMeta(meta);
        }

        item.setAmount(1);
        return item;
    }

    private void registerRecipe() {
        ItemStack result = getEnchantedGoldenTotemApple();
        NamespacedKey key = new NamespacedKey(plugin, "enchanted_golden_totem_apple");

        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("GGG", "GTG", "GGG");
        recipe.setIngredient('G', Material.ENCHANTED_GOLDEN_APPLE);
        recipe.setIngredient('T', Material.TOTEM_OF_UNDYING);

        Bukkit.addRecipe(recipe);
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (event.getRecipe() == null) return;

        ItemStack result = event.getRecipe().getResult();
        if (result == null || result.getType() != Material.ENCHANTED_GOLDEN_APPLE) return;

        ItemMeta meta = result.getItemMeta();
        if (meta == null || !RAW_ITEM_NAME.equals(ChatColor.stripColor(meta.getDisplayName()))) return;

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
    public void onEnchantedGoldenTotemAppleUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!isCustomTotemApple(item)) return;

        event.setCancelled(true);

        // Effects
        player.setFoodLevel(20);
        player.setSaturation(20f);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 6000, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 6000, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 0));

        // Grant temporary immunity
        player.setMetadata("damageImmune", new FixedMetadataValue(plugin, true));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.removeMetadata("damageImmune", plugin);
        }, 100L);

        player.sendMessage(ChatColor.GREEN + "I bet that tasted pretty good!");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.0f, 1.0f);

        // Consume item
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.setItemInHand(null);
        }
    }
}
