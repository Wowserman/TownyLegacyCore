package com.wowserman.storage;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.wowserman.TownyLegacy;

public class StorageFile {
	
	private TownyLegacy plugin;
	
	private FileConfiguration configfile = null;
	private File fileobject = null;

	public String getName() {
		return "configuration-file";
	}
	
	public TownyLegacy getPlugin() {
		return plugin;
	}
	
	public void attemptToCreateFile(Plugin plugin) {

		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		fileobject = new File(plugin.getDataFolder(), getName() + ".yml");

		if (!fileobject.exists()) {
			try {
				fileobject.createNewFile();
			}
			catch (IOException e) {
				Bukkit.getServer().getLogger().severe(ChatColor.RED  + "Could not create " + getName() + ".yml!");
			}
		}

		configfile = YamlConfiguration.loadConfiguration(fileobject);
	}

	public void saveFile() {
		try {
			configfile.save(fileobject);
		}
		catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED  + "Could not save " + getName() + ".yml!");
		}
	}

	public void reloadData() {
		configfile = YamlConfiguration.loadConfiguration(fileobject);
	}

	public StorageFile(TownyLegacy instance) {
		this.attemptToCreateFile(instance);
		this.plugin = instance;
	}

	public FileConfiguration getConfig() {
		return configfile;
	}
}
