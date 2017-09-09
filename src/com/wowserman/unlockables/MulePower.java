package com.wowserman.unlockables;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Donkey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;

import com.palmergames.bukkit.towny.object.TownyPermission.ActionType;
import com.wowserman.PlayerCache;
import com.wowserman.tools.BukkitTools;
import com.wowserman.tools.TownyTools;

public class MulePower implements Unlockable, Listener {

	@Override
	public Material getIconMaterial() {
		return Material.HAY_BLOCK;
	}

	@Override
	public String getName() {
		return "Mule Power";
	}
	
	@Override
	public String[] getDescription() {
		return new String[] {
				"Use your Mule to Help you Farm!",
				"",
				"While Riding a Mule...",
				"Right Click a Hoe to Harvest Crops.",
				"Left Click a Hoe to Till Land.",
				"Right Click Seeds to Plant.",
				"",
				"Leveling Upgrades...",
				"Level 1: 2 Block Radius.",
				"Level 2: 3 Block Radius.",
				"Level 3: 4 Block Radius.",
		};
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public double getUnlockCost() {
		return 50000;
	}
	
	@EventHandler
	public void click(PlayerInteractEvent event) {
		
		if (event.getPlayer().isInsideVehicle()==false)
			return;
		
		if (event.getPlayer().getVehicle() instanceof Donkey == false)
			return;
		
		ItemStack item = event.getItem();
		
		if (item==null)
			return;
		
		PlayerCache cache = PlayerCache.get(event.getPlayer());
		
		int level = cache.getUnlockableLevel(this);
		
		if (level < 1)
			return;
		
		if (item.getType() == Material.WOOD_HOE || item.getType() == Material.STONE_HOE
				|| item.getType() == Material.IRON_HOE || item.getType() == Material.GOLD_HOE
				|| item.getType() == Material.DIAMOND_HOE) {

			int radius = 5;
			
			final boolean rightClick = event.getAction()==Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK;
			
			for (Block block:BukkitTools.getNearbyBlocks2d(event.getPlayer().getVehicle().getLocation().add(0, 1, 0), radius)) {
				if (TownyTools.can(event.getPlayer(), block, ActionType.DESTROY)==false)
					continue;
				
				if (rightClick) {
					
					if (block.getState() instanceof Crops == false)
						continue;
					
					block.breakNaturally();
					
				} else {
					
					if (block.getRelative(0, 1, 0).getType() != Material.AIR)
						continue;
					
					if (block.getType() != Material.DIRT || block.getType() != Material.GRASS || block.getType() != Material.MYCEL)
						continue;
					
					block.setType(Material.SOIL);
					
				}
				
				BukkitTools.damage(item, 1);
				
				event.getPlayer().updateInventory();
			
			}
			
		}
		
	}

}
