package com.wowserman.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.wowserman.TownyLegacy;

public class BlockBreak implements Listener {
	
	private TownyLegacy plugin;
	
	public BlockBreak(TownyLegacy instance) {
		Bukkit.getPluginManager().registerEvents(this, this.plugin = instance);	
	}

	@EventHandler
	public void destroy(BlockBreakEvent e) {
		
		if (e.getBlock().getType()==Material.DIAMOND_ORE)
			plugin.actionAnnounce("§c§oPlayer Watcher §f" + e.getPlayer().getName() + " destroyed Diamond Ore at " + plugin.getPlayerShopStorage().getStringFrom(e.getBlock().getLocation()) + "." , "extras.playerwatcher");
		
		else if (e.getBlock().getType()==Material.GOLD_ORE)
			plugin.actionAnnounce("§c§oPlayer Watcher §f" + e.getPlayer().getName() + " destroyed Gold Ore at " + plugin.getPlayerShopStorage().getStringFrom(e.getBlock().getLocation()) + "." , "extras.playerwatcher");
		
		else if (e.getBlock().getType()==Material.EMERALD_ORE)
			plugin.actionAnnounce("§c§oPlayer Watcher §f" + e.getPlayer().getName() + " destroyed Emerald Ore at " + plugin.getPlayerShopStorage().getStringFrom(e.getBlock().getLocation()) + "." , "extras.playerwatcher");
		
		else if (e.getBlock().getType()==Material.IRON_ORE)
			plugin.actionAnnounce("§c§oPlayer Watcher §f" + e.getPlayer().getName() + " destroyed Iron Ore at " + plugin.getPlayerShopStorage().getStringFrom(e.getBlock().getLocation()) + "." , "extras.playerwatcher");
		
		else if (e.getBlock().getType()==Material.MOB_SPAWNER)
			plugin.actionAnnounce("§c§oPlayer Watcher §f" + e.getPlayer().getName() + " destroyed a Mob Spawner at " + plugin.getPlayerShopStorage().getStringFrom(e.getBlock().getLocation()) + "." , "extras.playerwatcher");
	}
}
