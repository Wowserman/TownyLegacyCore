package com.wowserman.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.wowserman.TownyLegacy;

public class PlayerInteract implements Listener {

	private TownyLegacy plugin;
	
	private String world;
	
	@EventHandler
	public void click(PlayerInteractEvent e) {
		if (e.getAction()!=Action.LEFT_CLICK_AIR)
			return;
		
		if (e.getPlayer().getWorld().getName().equalsIgnoreCase(world)==false)
			return;
		
		if (e.getItem()==null)
			return;
	
	}
	
	public PlayerInteract(TownyLegacy instance) {
		Bukkit.getPluginManager().registerEvents(this, this.plugin=instance);
		this.world = this.plugin.getConfig().getString("PvP World", "pvp");
	}
}
