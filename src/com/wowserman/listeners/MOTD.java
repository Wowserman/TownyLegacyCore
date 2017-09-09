package com.wowserman.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import com.wowserman.TownyLegacy;

public class MOTD implements Listener {

	private TownyLegacy plugin;
	
	private String line1;
	private String line2;
	
	public void setLine1(String value) {
		line1 = value;
	}
	
	public void setLine2(String value) {
		line2 = value;
	}
	
	public String getLine1() {
		return line1;
	}
	
	public String getLine2() {
		return line2;
	}
	
	public String getFormattedLine(String input) {
		
		String output = input;
		
		if (input.contains("&")) 
			output = output.replaceAll("&", "\u00A7");
		
		if (input.contains("#BOOST"))
			output = output.replaceAll("#BOOST", (plugin.getJobsBoosterExecutor().getTotalModifier() / 0.01) + "");
		
		if (input.contains("#TOTALMONEY"))
			output = output.replaceAll("#TOTALMONEY", plugin.getVaultHook().getFormattedServerTotal());
		
		if (input.contains("#RES"))
			output = output.replaceAll("#RES", plugin.getTownyHook().getPlugin().getResidents() + "");
		
		if (input.contains("#TOWN"))
			output = output.replaceAll("#TOWN", plugin.getTownyHook().getPlugin().getTowns() + "");
		
		if (input.contains("#NATION"))
			output = output.replaceAll("#NATION", plugin.getTownyHook().getPlugin().getNations() + "");
		
		return output;
	}
	
	public void load() {
		line1 = plugin.getConfig().getString("motd line 1") == null ? "" : plugin.getConfig().getString("motd line 1");
		line2 = plugin.getConfig().getString("motd line 2") == null ? "" : plugin.getConfig().getString("motd line 2");
	}
	
	public void save() {
		plugin.getConfig().set("motd line 1", line1);
		plugin.getConfig().set("motd line 2", line2);
		
		plugin.saveConfig();
	}
	
	@EventHandler
	public void ping(ServerListPingEvent e) {
		e.setMotd(this.getFormattedLine(line1) + "\n" + this.getFormattedLine(line2));
	}
	
	public MOTD(TownyLegacy instance) {
		this.plugin = instance;
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
		load();
	}
}
