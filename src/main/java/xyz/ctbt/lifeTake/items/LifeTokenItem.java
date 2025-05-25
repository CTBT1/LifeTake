package xyz.ctbt.lifeTake.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.ctbt.lifeTake.Main;

import java.util.List;

public class LifeTokenItem {

    public static ItemStack create() {
        ItemStack token = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = token.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Life Token");
            token.setItemMeta(meta);
        }
        return token;
    }

    public static void registerRecipe(Main plugin) {
        ItemStack result = create();

        NamespacedKey key = new NamespacedKey(plugin, "life_token");

        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("NSN", "SDS", "NSN");
        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        recipe.setIngredient('S', Material.NETHER_STAR);
        recipe.setIngredient('D', Material.DRAGON_HEAD);

        Bukkit.addRecipe(recipe);
    }
}
