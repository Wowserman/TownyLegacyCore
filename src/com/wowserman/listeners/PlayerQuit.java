package com.wowserman.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.wowserman.PlayerCache;
import com.wowserman.TownyLegacy;

public class PlayerQuit implements Listener {
	
	public PlayerQuit(TownyLegacy plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);	
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		
		PlayerCache.get(e.getPlayer()).close();
	}
}
