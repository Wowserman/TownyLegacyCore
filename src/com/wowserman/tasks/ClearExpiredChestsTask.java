package com.wowserman.tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import com.wowserman.TownyLegacy;

public class ClearExpiredChestsTask extends BukkitRunnable {
	
	private TownyLegacy plugin;
	
	private static List<String> trash = new ArrayList<String>();
	
	private int progress = 0;
	
	private Iterator<String> UUIDSections;
	
	public ClearExpiredChestsTask(TownyLegacy extras) {
		this.plugin = extras;
		
		UUIDSections = plugin.getChestStorage().getConfig().getKeys(false).iterator();
		
		plugin.getLogger().info("Starting Expired Chest Cleaning in Storage File. This may take a while.");
	}
	
	public void finish(boolean cancel) {

		for (String expired:trash) {
			plugin.getChestStorage().getConfig().set(expired, null);
		}
		
		plugin.getChestStorage().saveFile();
		
		plugin.getChestStorage().reloadData();
		
		plugin.getLogger().info("Finished Cleaning Expired Chests (Removed " + trash.size() + " Chests from " + progress + " total Chests.)");
		
		trash.clear();
		
		if (cancel)
			this.cancel();
	}
	
	public static void runAll(TownyLegacy extras) {
		extras.getLogger().info("Starting Expired Chest Cleaning in Storage File. Cleaning all at once (May cause some Lag).");
		
		Iterator<String> UUIDSectionz = extras.getChestStorage().getConfig().getKeys(false).iterator();
		
		while (UUIDSectionz.hasNext()) {
			String UUIDSection = UUIDSectionz.next();
			
			for (String chest:extras.getChestStorage().getConfig().getConfigurationSection(UUIDSection).getKeys(false)) {
				
				final String path = UUIDSection + "." + chest;
				
				if (extras.getChestStorage().check(path) || extras.getChestStorage().wasReclaimed(path))
					trash.add(path);
			}
		}
		
		for (String expired:trash) {
			extras.getChestStorage().getConfig().set(expired, null);
		}
		
		extras.getChestStorage().saveFile();
		
		extras.getChestStorage().reloadData();
		
		extras.getLogger().info("Finished Cleaning Expired Chests (Removed " + trash.size() + ").");
		
		trash.clear();
	}

	@Override
	public void run() {
		
		if (!UUIDSections.hasNext()) {
			finish(true);
			return;
		}
		
		String UUIDSection = UUIDSections.next();
		
		for (String chest:plugin.getChestStorage().getConfig().getConfigurationSection(UUIDSection).getKeys(false)) {
			
			final String path = UUIDSection + "." + chest;
			
			if (plugin.getChestStorage().check(path) || plugin.getChestStorage().wasReclaimed(path))
				trash.add(path);
		}
		
		progress++;
	}

}
