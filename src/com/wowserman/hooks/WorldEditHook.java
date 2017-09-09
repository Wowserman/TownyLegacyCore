package com.wowserman.hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.wowserman.TownyLegacy;
import com.wowserman.hooks.WorldEditHook.Selection.Vector2d;

public class WorldEditHook implements PluginHook {

	private WorldEditPlugin plugin;
	
	private boolean enabled = false;
	
	public static class Selection {
		
		public static class Vector2d {
			private double x, z;
			
			public double getX() {
				return x;
			}
			
			public double getZ() {
				return z;
			}
			
			public Vector2d(double x, double z) {
				this.x = x;
				this.z = z;
			}
			
			public static Vector2d getVector2dFrom(Location location) {
				return new Vector2d(location.getX(), location.getZ());
			}
		}
		
		private Vector2d corner1;
		private Vector2d corner2;
		private String world;
		
		public Vector2d getCorner1() {
			return corner1;
		}

		public Vector2d getCorner2() {
			return corner2;
		}

		public String getWorld() {
			return world;
		}
		
		public Selection(Vector2d corner1, Vector2d corner2, String world) {
			this.corner1 = corner1;
			this.corner2 = corner2;
			this.world = world;
		}
		
		public static Selection ZERO(String world) {
			return new Selection(new Vector2d(0,0), new Vector2d(0,0), "world");
		}
	}
	
	
	public Selection getSelection(Player player) {
		if (this.isEnabled()==false || plugin.getSelection(player)==null)
			return Selection.ZERO(player.getWorld().getName());
		
		com.sk89q.worldedit.bukkit.selections.Selection worldEditSelection = plugin.getSelection(player);
		
		return new Selection(Vector2d.getVector2dFrom(worldEditSelection.getMinimumPoint()), Vector2d.getVector2dFrom(worldEditSelection.getMaximumPoint()), worldEditSelection.getWorld().getName());	
	}
	
	public WorldEditHook(TownyLegacy instance) {
		
	}
	
	@Override
	public void setEnabled(boolean value) {
		this.enabled = value;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String getName() {
		return "WorldEdit";
	}

	@Override
	public boolean initialize() {
		if (Bukkit.getPluginManager().getPlugin(getName())!=null) {
			
			plugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin(getName());
			
			return true;
			
		} else return false;
	}

}
