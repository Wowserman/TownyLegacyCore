package com.wowserman.listeners;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.palmergames.bukkit.towny.event.NationAddTownEvent;
import com.palmergames.bukkit.towny.event.NationRemoveTownEvent;
import com.palmergames.bukkit.towny.object.Resident;
import com.wowserman.PlayerCache;
import com.wowserman.TownyLegacy;

public class NationChange implements Listener {

	@EventHandler
	public void nationAdd(NationAddTownEvent e) {
		
		for (Resident resident:e.getTown().getResidents()) {
			
			@SuppressWarnings("deprecation")
			OfflinePlayer player = Bukkit.getOfflinePlayer(resident.getName());
			
			if (player==null)
				return;
			
			PlayerCache cache = PlayerCache.get(player.getUniqueId());
						
			cache.setNation(e.getNation().getName());
		}
	}
	
	@EventHandler
	public void nationRemove(NationRemoveTownEvent e) {
		
		for (Resident resident:e.getTown().getResidents()) {

			@SuppressWarnings("deprecation")
			OfflinePlayer player = Bukkit.getOfflinePlayer(resident.getName());
			
			if (player==null)
				return;
			
			PlayerCache cache = PlayerCache.get(player.getUniqueId());
						
			cache.setNation(null);
		}
	}
	
	public NationChange(TownyLegacy instance) {
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
}
