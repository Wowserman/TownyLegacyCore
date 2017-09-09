package com.wowserman.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import com.palmergames.bukkit.towny.event.TownRemoveResidentEvent;
import com.wowserman.PlayerCache;
import com.wowserman.TownyLegacy;

public class TownChange implements Listener {
	
	@EventHandler
	public void townAdd(TownAddResidentEvent e) {
		
		Player player = Bukkit.getPlayer(e.getResident().getName());
		
		if (player==null) 
			return;
		
		PlayerCache.get(player).setTown(e.getTown().getName());
	}
	
	@EventHandler
	public void townRemove(TownRemoveResidentEvent e) {
		
		Player player = Bukkit.getPlayer(e.getResident().getName());
		
		if (player==null) 
			return;
		
		PlayerCache.get(player).setTown(null);
	}
	
	public TownChange(TownyLegacy instance) {
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
}
