package com.wowserman.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.wowserman.TownyLegacy;

public class DonatorChat implements Listener {

	private TownyLegacy plugin;
	
	private List<String> players = new ArrayList<String>();
	
	public void join(Player player) {
		this.plugin.getStaffChatListener().leave(player);
		if (!players.contains(player.getUniqueId().toString()))
			players.add(player.getUniqueId().toString());
	}
	
	public void leave(Player player) {
		players.remove(player.getUniqueId().toString());
	}
	
	public void send(Player sender, String message) {
		plugin.announce("§7(§bDonator Chat§7) " + sender.getDisplayName() + "§8:§f " + message, "extras.donatorchat");
	}
	
	public boolean isInChat(Player player) {
		return players.contains(player.getUniqueId().toString());
	}
	
	@EventHandler
	public void chat(AsyncPlayerChatEvent e) {
		if (!isInChat(e.getPlayer())) 
			return;
		
		e.setCancelled(true);
		
		send(e.getPlayer(), e.getMessage());
	}
	
	public DonatorChat(TownyLegacy instance) {
		Bukkit.getPluginManager().registerEvents(this, this.plugin = instance);
	}
}
