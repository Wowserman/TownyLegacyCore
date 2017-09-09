package com.wowserman.unlockables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.wowserman.PlayerCache;
import com.wowserman.TownyLegacy;

public class UnlockablesManager {
	
	final static Unlockable[] UNLOCKABLES = new Unlockable[] {
			
	};
	
	public static void load(TownyLegacy plugin) {
		
		for (Unlockable unlockable:UnlockablesManager.UNLOCKABLES) { 
			if (unlockable instanceof Listener) {
				Bukkit.getPluginManager().registerEvents((Listener) unlockable, plugin);
				plugin.getLogger().info("LOADED UNLOCKABLE & REGISTERED LISTENER" + unlockable.getName() + ".");
			} else plugin.getLogger().info("LOADED UNLOCKABLE " + unlockable.getName() + ".");
		}
	}

	public static Unlockable getUnlockable(String name) {
		for (Unlockable unlockable:UnlockablesManager.UNLOCKABLES)
			if (unlockable.getName().equals(name))
				return unlockable;
		return null;
	}
	
	public static void openMenu(Player player) {
		PlayerCache cache = PlayerCache.get(player);
	}
}
