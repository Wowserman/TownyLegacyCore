package com.wowserman.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import com.wowserman.TownyLegacy;

public class AnnouncerTask extends BukkitRunnable{

	private TownyLegacy plugin;
	
	@Override
	public void run() {
		plugin.announce(plugin.getAnnouncerCommand().next());
	}
	
	public AnnouncerTask(TownyLegacy instance) {
		this.plugin = instance;
	}

}
