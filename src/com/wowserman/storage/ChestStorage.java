package com.wowserman.storage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.wowserman.TownyLegacy;

public class ChestStorage extends StorageFile {

	final public DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	
	public ChestStorage(TownyLegacy instance) {
		super(instance);
	}
	
	public boolean isExpired(String date) {
		try {
			return (dateFormat.parse(date).before(DateUtils.addDays(new Date(), -14)));
		} catch (ParseException e) {
			return true;	
		}
	}
	
	public boolean hasChests(Player player) {
		return this.getConfig().get(player.getUniqueId().toString())!=null && getChestKeys(player).size() > 0;
	}
	
	public List<String> getChestKeys(Player player) {
		
		List<String> l = new ArrayList<String>(this.getConfig().getConfigurationSection(player.getUniqueId().toString()).getKeys(false));
		
		for (int i = 0; i < l.size(); i++) 
			l.set(i, player.getUniqueId().toString() + "." + l.get(i));
		
		for (int i = 0; i < l.size(); i++) 
			if (wasReclaimed(l.get(i))) {
				l.remove(i);
				i--;
			}
		return l;
		
	}
	
	public boolean check(String key) {
		return isExpired(this.getConfig().getString(key + ".expiration"));
	}
	
	public boolean exists(String key) {
		return this.getConfig().get(key)!=null;
	}
	
	public boolean wasReclaimed(String key) {
		return this.getConfig().getConfigurationSection(key).getBoolean("claimed");
	}
	
	public void reclaim(String key) {
		this.getConfig().set(key + ".claimed", true);
		
		this.saveFile();
		
		this.reloadData();
	}
	
	public void clear(String key) {
		this.getConfig().set(key, null);
		
		this.saveFile();
		
		this.reloadData();
	}
	
	public void notifyPlayer(Player player) {
		player.sendMessage("§bYou have §7" + this.getChestKeys(player).size() + "§b Chests to /reclaim!");
		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 3f, 3f);
	}
	
	public String getExpiration(String key) {
		return this.getConfig().getString(key + ".expiration");
	}
	
	public void write(Chest chest, String targetName, Player moderator) {
		
		@SuppressWarnings("deprecation")
		final String targetUUID = Bukkit.getServer().getOfflinePlayer(targetName).getUniqueId().toString();
		
		final String location = chest.getLocation().getBlockX() + " " + chest.getLocation().getBlockY() + " " + chest.getLocation().getBlockZ() + " " + chest.getWorld().getName();
		
		String expiration = dateFormat.format(DateUtils.addDays(Calendar.getInstance().getTime(), 14).getTime());
		
		this.getConfig().set(targetUUID + "." + location + ".expiration", expiration);
		
		for (int i = 0; i < chest.getInventory().getContents().length; i++) {
			this.getConfig().set(targetUUID + "." + location + ".items." + (i + 1), chest.getInventory().getItem(i));
		}
		
		this.saveFile();
		
		this.reloadData();
	}
	
	public Inventory read(String path) {
		Inventory inventory = Bukkit.createInventory(null, 27, path);
		
		if (this.getConfig().getConfigurationSection(path + ".items")==null)
			return inventory;
		
		for (String itemPath:this.getConfig().getConfigurationSection(path + ".items").getKeys(false)) {
			inventory.addItem(this.getConfig().getItemStack(path + ".items." + itemPath));
		}
		
		return inventory;
	}
	

	@Override
	public String getName() {
		return "chest-data";
	}
}
