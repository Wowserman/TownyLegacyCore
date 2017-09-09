package com.wowserman.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.wowserman.PlayerCache;
import com.wowserman.TownyLegacy;
import com.wowserman.tasks.PlayerMessageTask;

public class PlayerJoin implements Listener {
	
	private TownyLegacy plugin;
	
	public PlayerJoin(TownyLegacy plugin) {
		
		Bukkit.getPluginManager().registerEvents(this, this.plugin = plugin);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void join(PlayerJoinEvent e) {

		PlayerCache cache = PlayerCache.get(e.getPlayer());
				
		cache.open();
		
		new PlayerMessageTask(plugin, e.getPlayer(), plugin.getVoteStorage().getMessage()).runTaskTimer(plugin, 0, 20);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				plugin.getReferalExecutor().check(e.getPlayer());
			}
		}.runTaskLater(plugin, 50);
		
		if (plugin.getChestStorage().hasChests(e.getPlayer())) {
			new BukkitRunnable() {
				@Override
				public void run() {
					if (e.getPlayer().isOnline()) {
						plugin.getChestStorage().notifyPlayer(e.getPlayer());
					}
				}
				
			}.runTaskLater(plugin, 50);
		}
		
		if (plugin.getJobsHook().isEnabled() && plugin.getJobsBoosterExecutor().getModifiers().isEmpty())
			return;
		
		// TODO: Jobs Booster Message
		e.getPlayer().sendMessage("§aCurrent Global Jobs Booster: " + (plugin.getJobsBoosterExecutor().getTotalModifier() / 0.01) + "%");
	}
}
