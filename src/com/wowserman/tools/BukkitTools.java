package com.wowserman.tools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_11_R1.Material;

public class BukkitTools {

	public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                   blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }
	
	public static List<Block> getNearbyBlocks2d(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
        	for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                blocks.add(location.getWorld().getBlockAt(x, location.getBlockY(), z));
             }
        }
        return blocks;
    }

	public static void damage(ItemStack item, int damage) {
		if (item.getDurability() <= 1)
			item = null;
		else item.setDurability((short) (item.getDurability()-damage));
		
	}
	
}
