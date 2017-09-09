package com.wowserman.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import com.wowserman.TownyLegacy;

public class JobsBoosterTimeoutTask extends BukkitRunnable {

	private TownyLegacy plugin;
	
	public JobsBoosterTimeoutTask(TownyLegacy instance) {
		plugin = instance;
	}
	
	@Override
	public void run() {
		
		if (plugin.getJobsHook().isEnabled()==false) {
			this.cancel();
			return;
		}
		
		for (String data:plugin.
				getJobsBoosterExecutor().
				getModifiers()) {
			plugin.getJobsBoosterExecutor().addToModifierOfName(plugin.getJobsBoosterExecutor().getNameFrom(data), -1, 0.0);
		}
		
		plugin.getJobsHook().updateJobsWageAmplifier();	
	}

}
