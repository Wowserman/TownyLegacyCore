package com.wowserman.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.wowserman.PlayerCache;
import com.wowserman.TownyLegacy;
import com.wowserman.tools.ActionBar;

public class FlyRewardTask extends BukkitRunnable {

	private TownyLegacy plugin;
	
	@Override
	public void run() {
		
		
		for (Player player:Bukkit.getOnlinePlayers()) {
			PlayerCache cache = PlayerCache.get(player);
			
			if (cache.getMinutesLeft() > 1) {
				cache.setMinutesLeft(cache.getMinutesLeft() - 1);
				
				if (!player.hasPermission("extras.flybypass")) {
					String message = plugin.getVoteStorage().getPlayerStillHasTimeMessage();
					
					message = message.contains("&") ? message.replaceAll("&", "§"):message;
					message = message.contains("<player>") ? message.replaceAll("<player>", player.getName()):message;
					message = message.contains("<votes>") ? message.replaceAll("<votes>", "" + cache.getVotes()):message;
					message = message.contains("<minutes>") ? message.replaceAll("<minutes>", cache.getMinutesLeft() + ""):message;
					
					ActionBar.send(player, message);
					
				}
				
				continue;
			}
			
			else if (player.hasPermission("essentials.fly") && !player.hasPermission("extras.flybypass") && !player.isOp()) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "manudelp " + player.getName() + " essentials.fly");
				
				if (player.isFlying()) {
					player.setFlying(false);
				}
				
				String message = plugin.getVoteStorage().getExpiredMessage();
				message = message.contains("&") ? message.replaceAll("&", "§"):message;
				message = message.contains("<player>") ? message.replaceAll("<player>", player.getName()):message;
				
				player.sendTitle("", message, 0, 50, 0);
			}
		}
		
		return;
	}

	public FlyRewardTask(TownyLegacy instance) {
		this.plugin = instance;
	}
	
}
