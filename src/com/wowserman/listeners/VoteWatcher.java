package com.wowserman.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.vexsoftware.votifier.model.VotifierEvent;
import com.wowserman.PlayerCache;
import com.wowserman.TownyLegacy;
import com.wowserman.commands.VoteCommand;
import com.wowserman.storage.VoteStorage;
import com.wowserman.storage.VoteStorage.VoteSite;

public class VoteWatcher implements Listener {
	
	private TownyLegacy plugin;
	
	public VoteWatcher(TownyLegacy instance) {
		Bukkit.getPluginManager().registerEvents(this, this.plugin = instance);
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (ChatColor.stripColor(e.getInventory().getTitle()).equals(VoteCommand.VOTE_TITLE)==false) {
			System.out.print("Wrong Inventory");
			return;
		}
		
		if (e.getCurrentItem()==null) {
			System.out.print("Item Null");
			return;
		}

		Player player = (Player) e.getWhoClicked();
		
		if (e.getCurrentItem().getType()!=Material.EMERALD) {
			player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 3f, 3f);
			e.setCancelled(true);
			return;
		}
		
		VoteSite site = VoteSite.get(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
		
		player.sendMessage("§a" + site.getName());
		player.sendMessage("§7" + site.getUrl());
		
		player.closeInventory();

	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (e.getInventory().getTitle().equals(VoteStorage.REWARDS_TITLE)==false)
			return;
		
		this.plugin.getVoteStorage().setRewards(e.getInventory());
		
		e.getPlayer().sendMessage("§aUpdated Vote Party Rewards.");
	}
	
	@EventHandler
	public void vote(VotifierEvent e) {
		
		if (Bukkit.getPlayer(e.getVote().getUsername())==null) 
			return;
		
		VoteSite site = null;
		
		for (VoteSite s:VoteSite.getSites()) {
			if (s.getName().toLowerCase().contains(e.getVote().getServiceName().toLowerCase())) {
				site = s;
				break;
			}
		}
		
		if (site == null && (site = VoteSite.get(e.getVote().getServiceName()))==null) {
			plugin.getLogger().severe("Vote from Unknown VoteSite '" + e.getVote().getServiceName() + "'.");
			return;
		}
		
		PlayerCache.get(Bukkit.getPlayer(e.getVote().getUsername())).updateVote(site);
	}
	
}
