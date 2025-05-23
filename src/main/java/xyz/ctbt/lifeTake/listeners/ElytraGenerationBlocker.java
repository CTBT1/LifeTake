package xyz.ctbt.lifeTake.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.Location;

public class ElytraGenerationBlocker implements Listener {

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        World world = event.getWorld();

        // Only act in the End
        if (!world.getEnvironment().equals(World.Environment.THE_END)) return;

        for (Entity entity : event.getChunk().getEntities()) {
            if (entity instanceof ItemFrame itemFrame) {
                ItemStack item = itemFrame.getItem();
                if (item != null && item.getType() == Material.ELYTRA) {
                    itemFrame.setItem(new ItemStack(Material.AIR)); // Remove Elytra
                }
            }
        }
    }
}
