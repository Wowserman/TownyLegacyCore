package com.wowserman.hooks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.wowserman.PlayerCache;
import com.wowserman.TownyLegacy;

public class TownyHook implements PluginHook {
	
	private Towny plugin;
	
	public Towny getPlugin() {
		return plugin;
	}
	
	@Override
	public String getName() {
		return "Towny";
	}

	@Override
	public boolean initialize() {
		
		if (Bukkit.getPluginManager().getPlugin(getName())!=null) {
			
			plugin = (Towny) Bukkit.getPluginManager().getPlugin(getName());
			return true;
			
		} else return false;
	}
	
	public TownyHook(TownyLegacy instance) {
		
	}
	
	public String getFormatedTownAndNation(Player player) {
		String town = null, nation = null;
		
		try {
			Resident resident = TownyUniverse.getDataSource().getResident(player.getName());
			town = resident.hasTown() ? resident.getTown().getName():null;
			nation = resident.hasNation() ? resident.getTown().getNation().getName():null;
		} catch (NotRegisteredException e) {
			
		}
		
		return (town != null ? "§b" + town:"") + (town!=null && nation!=null ? "§f, §6" + nation:"") + (town==null ? "§7§oTownless":"");
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
