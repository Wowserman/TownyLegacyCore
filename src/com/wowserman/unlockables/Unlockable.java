package com.wowserman.unlockables;

import org.bukkit.Material;

public interface Unlockable {
	
	public Material getIconMaterial();
	
	public String getName();
	
	public String[] getDescription();
	
	public int getMaxLevel();
	
	public double getUnlockCost();
	
	public default double getCost(int level) {
		return this.getUnlockCost() * (level * 0.1 + 0.9);
	}
	
}
