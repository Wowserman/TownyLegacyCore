package com.wowserman.storage;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wowserman.TownyLegacy;

public class PlayerShopStorage extends StorageFile {
	
	@Override
	public String getName() {
		return "playershop-data";
	}
	
	public class ShopData {
		
		private String data;
		
		public String getUUIDFromData() {
			return data.split("/")[0];
		}
		
		public String getPlayerNameFromData() {
			return data.split("/")[1];
		}
		
		@Override
		public String toString() {
			return data;
		}
		
		public ShopData(String data) {
			this.data = data;
		}
	}
	
	public PlayerShopStorage(TownyLegacy instance) {
		super(instance);
		
		if (instance.getConfig().get("Max Player Shops")==null) {
			instance.getConfig().set("Max Player Shops", 16);
			instance.saveConfig();
			instance.reloadConfig();
			this.max = 16;
		} else this.max = instance.getConfig().getInt("Max Player Shops");
	}
	
	public int getShopCount(Player player) {
		return this.getConfig().getInt("players." + player.getUniqueId().toString(), 0);
	}
	
	public int getShopCount(ShopData data) {
		return this.getConfig().getInt("players." + data.getUUIDFromData(), 0);
	}
	
	public String getStringFrom(Location location) {
		return location.getBlockX() + "/" + location.getBlockY() + "/" + location.getBlockZ() + "/" + location.getWorld().getName();
	}
	
	public ShopData getShopDataFromStorageFor(Location location) {
		if (this.getConfig().getString("shops." + this.getStringFrom(location))==null)
			return null;
		
		return new ShopData(this.getConfig().getString("shops." + this.getStringFrom(location)));
	}
	
	private int max = 0;
	
	public int getMax() {
		return max;
	}
	
	/*
	 * players:
	 *   UUID: 1
	 * 
	 * shops:
	 *   1/1/1/world: UUID/Wowserman
	 * 
	 * 
	 */
	
	public ShopData destroyShop(Location location) {
		ShopData data = this.getShopDataFromStorageFor(location);
		
		if (data==null)
			return null;
		
		this.getConfig().set(this.getStringFrom(location), null);
		
		int shopCount = this.getShopCount(data) - 1;
		
		if (shopCount > 0)
			this.getConfig().set("players." + data.getUUIDFromData(), shopCount);
		else this.getConfig().set("players." + data.getUUIDFromData(), null);
		
		this.saveFile();
		
		this.reloadData();
		
		return data;
	}
	
	public boolean createShop(Location location, Player player) {
		if (this.getShopCount(player) <= max == false)
			return false;
		
		int shopCount = this.getShopCount(player) + 1;
		
		if (shopCount > 0)
			this.getConfig().set("players." + player.getUniqueId().toString(), shopCount);
		else this.getConfig().set("players." + player.getUniqueId().toString(), null);
		
		this.getConfig().set("shops." + this.getStringFrom(location), player.getUniqueId() + "/" + player.getName());
		
		this.saveFile();
		
		this.reloadData();
		
		return true;
	}
}
