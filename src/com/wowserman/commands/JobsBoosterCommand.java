package com.wowserman.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wowserman.TownyLegacy;

public class JobsBoosterCommand implements CommandExecutor {

	public TownyLegacy plugin;
	
	public Double getTotalModifier() {
		double total = 0.0;
		
		for (String data:modifiers) {
			total += getModifierFrom(data);
		}
		
		return total;
	}
	
	// Wowserman,60,0.1
	private List<String> modifiers = new ArrayList<String>();
	
	public List<String> getModifiers() {
		return modifiers;
	}
	
	public String getNameFrom(String data) {
		return data.split(",")[0];
	}
	
	public Integer getMinutesLeftFrom(String data) {
		return Integer.parseInt(data.split(",")[1]);
	}
	
	public Double getModifierFrom(String data) {
		return Double.parseDouble(data.split(",")[2]);
	}
	
	public List<String> getModifierNames() {
		
		List<String> l = new ArrayList<String>();
		
		for (String entry:modifiers) {
			String[] data = entry.split(",");
			l.add(data[0]);
		}
		
		return l;
	}
	
	/**
	 * @param modifierName
	 * @return
	 */
	public String getDataFrom(String modifierName) {
		for (String data:modifiers) {
			if (getNameFrom(modifierName).equals(getNameFrom(data)))
				return data;
		}
		
		return null;
	}
	
	
	/**
	 * Can be Negative, Cannot be used to Create Modifiers.
	 * 
	 * @param modifierName
	 * @param amount
	 */
	public void addToModifierOfName(String modifierName, Integer minutes, Double modifier) {

		int index = -1;
		String newData = null;
		
		for (int i = 0; i < modifiers.size(); i++) {
			if (getNameFrom(modifiers.get(i)).equals(modifierName)) {
				index = i;
				newData = modifiers.get(i);
				
				newData = getNameFrom(newData) + "," + (getMinutesLeftFrom(newData) + minutes) + "," + (getModifierFrom(newData) + modifier);
			}
		}
		
		if (index == -1)
			return;
			
		modifiers.remove(index);
		
		modifiers.add(newData);
		
		check(getDataFrom(modifierName));		
	}
	
	public void removeModifierByName(String modifierName) {
		int index = -1;
		
		for (int i = 0; i < modifiers.size(); i++) {
			if (getNameFrom(modifiers.get(i)).equals(modifierName))
				index = i;
		}
		
		if (index == -1)
			return;
		
		modifiers.remove(index);
	}
	
	public void check(String data) {
		if (getMinutesLeftFrom(data) <= 0) {
			plugin.announce("§aJobs Pay Modifier from " + getNameFrom(data) + " of " + (getModifierFrom(data) / 0.01) + "% has expired.");
			removeModifierByName(getNameFrom(data));
		}
	}
	
	public void start(String modifierName, Integer minutes, Double modifier) {
		plugin.announce("§a" + modifierName + " has started a " + (modifier / 0.01) + "% Jobs Pay Modifier for " + minutes + " minutes.");
		modifiers.add(modifierName + "," + minutes + "," + modifier);
	}
	
	public Integer getMinutesLeftForModifier(String modifierName) {
		
		for (String entry:modifiers) {
			String[] data = entry.split(",");
			
			if (data[0].equals(modifierName))
				return Integer.parseInt(data[1]);
		}
		
		return 0;
	}
	
	private Double getModifierFromName(String modifierName) {

		for (String entry:modifiers) {
			String[] data = entry.split(",");
			
			if (data[0].equals(modifierName))
				return Double.parseDouble(data[2]);
		}
		
		return 0.0;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		
		if (sender.hasPermission("extras.job-booster") == false && sender instanceof Player) {
			sender.sendMessage("§cYou're lacking Permisison to perform this!");
			return true;
		}
		
		if (!plugin.getJobsHook().isEnabled()) {
			sender.sendMessage("§cThis feature isn't enabled.");
			return true;
		}
		
		if (args.length==0) {
			sender.sendMessage("§cYou're missing your 1st Argument. Try adding 'check', 'add', 'remove', or 'reset'");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("check")) {
			
			if (args.length!=2) {
				
				sender.sendMessage("§aCurrent Global Jobs Booster: " + (this.getTotalModifier() / 0.01) + "%");
				
				this.plugin.getJobsHook().updateJobsWageAmplifier();
				
				return true;
			}
			
			if (this.getDataFrom(args[1])==null) {
				
				sender.sendMessage("§cPlayer by that name has no active Modifier.");
				
				return true;
			}
			
			sender.sendMessage("§aModifier from " + args[1] + ": " + (this.getModifierFromName(args[1]) / 0.01) + "%");
			
			return true;
			
		}
		
		if (args[0].equalsIgnoreCase("add")) {
			
			if (args.length==1) {
				
				sender.sendMessage("§cYou're missing your 2nd Argument. Try adding a Player's Name.");
				
				return true;
			}
			
			if (args.length==2) {
				
				sender.sendMessage("§cYou're missing your 3rd Argument. Try adding an Integer.");
				
				return true;
			}
						
			try {
				Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				
				sender.sendMessage("§cYour 3rd Argument is not a Integer.");
				
				return true;
			}
			
			if (args.length==3) {
				
				sender.sendMessage("§cYou're missing your 4th Argument. Try adding a Double");
				
				return true;
			}
			
			try {
				Double.parseDouble(args[3]);
			} catch (NumberFormatException e) {
				
				sender.sendMessage("§cYour 4th Argument is not a Double.");
				
				return true;
			}
			
			if (this.getDataFrom(args[1])==null) {
				
				this.start(args[1], Integer.parseInt(args[2]), Double.parseDouble(args[3]));
				
				this.plugin.getJobsHook().updateJobsWageAmplifier();
				
				return true;
			}
			
			plugin.announce("§a" + args[2] + " minutes and a " + args[3] + " modifier was added to " + args[1]);
			
			this.addToModifierOfName(args[1], Integer.parseInt(args[2]), Double.parseDouble(args[3]));
			
			this.plugin.getJobsHook().updateJobsWageAmplifier();
			
			return true;
			
		}
		
		if (args[0].equalsIgnoreCase("remove")) {
			
			if (args.length==1) {
				
				sender.sendMessage("§cYou're missing your 2nd Argument. Try adding a Player's Name.");
				
				return true;
			}
			
			this.removeModifierByName(args[1]);
			
			sender.sendMessage("§aBooster removed from " + args[1]);
			
			return true;
			
		}

		if (args[0].equalsIgnoreCase("reset")) {
			
			modifiers = new ArrayList<String>();
			
			plugin.announce("§aAll Job Wage Boosters have Ended.");
			
			this.plugin.getJobsHook().updateJobsWageAmplifier();
			
			return true;
		}
		
		
		
		return false;
	}

	public JobsBoosterCommand(TownyLegacy instance) {
		
		this.plugin = instance;
		
		this.modifiers = plugin.getConfig().getStringList("current-modifiers")==null ? new ArrayList<String>() : plugin.getConfig().getStringList("current-modifiers");
	}

	public void close() {
		plugin.getConfig().set("current-modifiers", modifiers);
		
		plugin.saveConfig();	
	}


}
