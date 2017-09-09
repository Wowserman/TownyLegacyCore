package com.wowserman.hooks;

import org.bukkit.Bukkit;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.BoostMultiplier;
import com.gamingmesh.jobs.container.CurrencyType;
import com.gamingmesh.jobs.container.Job;
import com.wowserman.TownyLegacy;

public class JobsHook implements PluginHook {
	
	private Jobs plugin;
	
	private TownyLegacy extras;
	
	public void updateJobsWageAmplifier() {
		BoostMultiplier bm = new BoostMultiplier();
		bm.add(CurrencyType.MONEY, extras.getJobsBoosterExecutor().getTotalModifier());
		
		for (Job job:Jobs.getJobs()) {
			job.setBoost(bm);
		}
	}
	
	public Jobs getPlugin() {
		return plugin;
	}
	
	@Override
	public String getName() {
		return "Jobs";
	}

	@Override
	public boolean initialize() {
		
		if (Bukkit.getPluginManager().getPlugin(getName())!=null) {
			
			plugin = (Jobs) Bukkit.getPluginManager().getPlugin(getName());
			
			return true;
			
		} else return false;
	}
	
	public JobsHook(TownyLegacy instance) {
		this.extras = instance;
	}

	private boolean enabled;
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean value) {
		enabled = value;
	}

}
