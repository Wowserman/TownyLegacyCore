package com.wowserman.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.wowserman.TownyLegacy;

public class InfoCommand implements CommandExecutor {

	public TownyLegacy plugin;
	
	private List<String> traveller, citizen, knight, noble, royal, hero, legend, mystic;
	
	private static final String travellerKey = "Traveller Info", citizenKey = "Citizen Info", knightKey = "Knight Info", nobleKey = "Noble Info", royalKey = "Royal Info", heroKey = "Hero Info", legendKey = "Legend Info", mysticKey = "Mystic Info";
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender.isOp() && args.length > 0) {
			if (args[0].equalsIgnoreCase("reload")) {
				this.load(true);
				
				sender.sendMessage("§aReloaded Data.");
			} else sender.sendMessage("§cYour 1st Argument doesn't match any avalible Arguments. Try 'reload'.");
			
			return true;
		}
		
		if (label.equalsIgnoreCase("traveller")) {
			for (String message:traveller)
				sender.sendMessage(plugin.colify(message));
			return true;
		}
		
		if (label.equalsIgnoreCase("citizen")) {
			for (String message:citizen)
				sender.sendMessage(plugin.colify(message));
			return true;
		}
		
		if (label.equalsIgnoreCase("knight")) {
			for (String message:knight)
				sender.sendMessage(plugin.colify(message));
			return true;
		}
		
		if (label.equalsIgnoreCase("noble")) {
			for (String message:noble)
				sender.sendMessage(plugin.colify(message));
			return true;
		}
		
		if (label.equalsIgnoreCase("royal")) {
			for (String message:royal)
				sender.sendMessage(plugin.colify(message));
			return true;
		}
		
		if (label.equalsIgnoreCase("hero")) {
			for (String message:hero)
				sender.sendMessage(plugin.colify(message));
			return true;
		}
		
		if (label.equalsIgnoreCase("legend")) {
			for (String message:legend)
				sender.sendMessage(plugin.colify(message));
			return true;
		}
		
		if (label.equalsIgnoreCase("mystic")) {
			for (String message:mystic)
				sender.sendMessage(plugin.colify(message));
			return true;
		}
		
		return true;
	}
	
	public void load(boolean reload) {
		if (reload)
			this.plugin.reloadConfig();
		
		this.traveller = this.plugin.getConfig().getStringList(InfoCommand.travellerKey) != null ? this.plugin.getConfig().getStringList(InfoCommand.travellerKey):new ArrayList<String>();
		this.citizen = this.plugin.getConfig().getStringList(InfoCommand.citizenKey) != null ? this.plugin.getConfig().getStringList(InfoCommand.citizenKey):new ArrayList<String>();
		this.knight = this.plugin.getConfig().getStringList(InfoCommand.knightKey) != null ? this.plugin.getConfig().getStringList(InfoCommand.knightKey):new ArrayList<String>();
		this.noble = this.plugin.getConfig().getStringList(InfoCommand.nobleKey) != null ? this.plugin.getConfig().getStringList(InfoCommand.nobleKey):new ArrayList<String>();
		this.royal = this.plugin.getConfig().getStringList("Royal Info") != null ? this.plugin.getConfig().getStringList("Royal Info"):new ArrayList<String>();
		this.hero = this.plugin.getConfig().getStringList(InfoCommand.heroKey) != null ? this.plugin.getConfig().getStringList(InfoCommand.heroKey):new ArrayList<String>();
		this.legend = this.plugin.getConfig().getStringList(InfoCommand.legendKey) != null ? this.plugin.getConfig().getStringList(InfoCommand.legendKey):new ArrayList<String>();
		this.mystic = this.plugin.getConfig().getStringList(InfoCommand.mysticKey) != null ? this.plugin.getConfig().getStringList(InfoCommand.mysticKey):new ArrayList<String>();
	}
	
	public InfoCommand(TownyLegacy instance) {
		
		this.plugin = instance;
		
		this.load(false);

	}
	
	public void close() {
		this.plugin.getConfig().set(InfoCommand.travellerKey, traveller);
		this.plugin.getConfig().set(InfoCommand.citizenKey, citizen);
		this.plugin.getConfig().set(InfoCommand.knightKey, knight);
		this.plugin.getConfig().set(InfoCommand.nobleKey, noble);
		this.plugin.getConfig().set(InfoCommand.royalKey, royal);
		this.plugin.getConfig().set(InfoCommand.heroKey, hero);
		this.plugin.getConfig().set(InfoCommand.legendKey, legend);
		this.plugin.getConfig().set(InfoCommand.mysticKey, mystic);
		this.plugin.saveConfig();
	}
}
