package com.wowserman.tools;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.object.TownyPermission.ActionType;
import com.wowserman.TownyLegacy;

public class TownyTools {

	private static TownyLegacy plugin;
	
	public static void setPlugin(TownyLegacy instance) {
		TownyTools.plugin = instance;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean can(Player player, Block block, ActionType action) {
		if (TownyTools.plugin.getTownyHook().isEnabled()) {
			try {
				return TownyTools.plugin.getTownyHook().getPlugin().getCache(player).getCachePermission(block.getTypeId(), block.getData(), action);
			} catch (NullPointerException e) {
				return false;
			}
		}
		return false;
	}
}
