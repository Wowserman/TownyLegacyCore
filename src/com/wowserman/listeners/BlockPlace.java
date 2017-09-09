package com.wowserman.listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.wowserman.TownyLegacy;

public class BlockPlace implements Listener {

	private TownyLegacy plugin;
	
	@EventHandler
	public void place(BlockPlaceEvent e) {
		if (plugin.getReclaimCommand().getStoredChestFromItemStack(e.getItemInHand()) != null) {
			Chest chest = (Chest) e.getBlock().getState();
			chest.getBlockInventory().setContents(plugin.getChestStorage().read(plugin.getReclaimCommand().getStoredChestFromItemStack(e.getItemInHand())).getContents());
			chest.update(true);
			plugin.getChestStorage().clear(plugin.getReclaimCommand().getStoredChestFromItemStack(e.getItemInHand()));
			e.getPlayer().sendMessage("§aYou have Successfully recovered your lost Chest!");
		}
	}
	
	public BlockPlace(TownyLegacy instance) {
		Bukkit.getPluginManager().registerEvents(this, this.plugin = instance);	
	}
}
