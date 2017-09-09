package com.wowserman.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.wowserman.TownyLegacy;

public class PlayerMessageTask extends BukkitRunnable {

//	private Extras plugin;
	
	private Player player;
	
//	private String message;
	
	private int index = 0;
	
	public PlayerMessageTask(TownyLegacy instance, Player player, String message) {
		this.player = player;
//		this.message = message;
//		this.plugin = instance;
	}
	
	@Override
	public void run() {
		if (player.isOnline()==false || index == 5) {
			System.out.print("Canceling Task.");
			this.cancel();
			return;
		}

//		String m = message;
//		m = m.contains("<votes>") ? m.replaceAll("<votes>", VoteCache.get(player).getVotes() + ""):m;
//		ActionBar.send(player, m);
//		
		index++;
	}

}
