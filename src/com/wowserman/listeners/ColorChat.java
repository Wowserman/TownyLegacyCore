package com.wowserman.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.wowserman.PlayerCache;
import com.wowserman.TownyLegacy;

public class ColorChat implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void chat(AsyncPlayerChatEvent e) {
		e.setMessage(PlayerCache.get(e.getPlayer()).getChatColor() + e.getMessage());
	}
	
	public ColorChat(TownyLegacy instance) {
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
}
