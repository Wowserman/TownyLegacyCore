package com.wowserman.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.wowserman.TownyLegacy;

public class AnnouncerCommand implements CommandExecutor {

	public TownyLegacy plugin;
	
	private long interval = 20;
	
	private String prefix = "";
	
	int index = 0;
	
	private List<String> messages = new ArrayList<String>();
	
	public void update() {
		index = 0;
		plugin.reRunAnnouncerTask();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		
		if (sender.hasPermission("extras.announcer") == false) {
			sender.sendMessage("§cYou're lacking Permisison to perform this!");
			return true;
		}
		
		if (args.length==0) {
			sender.sendMessage("§cYou're missing your 1st Argument. Try adding a 'interval', 'prefix', 'list', 'add, 'say', or 'remove'.");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("interval")) {

			int i = -1;
			
			try {
				i = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				sender.sendMessage("§cYour 2nd Argument is not a Integer.");
				return true;
			}
			
			if (interval < 1) {
				sender.sendMessage("§cInterval must be greater than 0 Ticks!");
				return true;
			}
			
			this.interval = i;
			
			sender.sendMessage("§aSet the Interval to " + interval + " ticks.");
			
			this.update();
			
		}

		if (args[0].equalsIgnoreCase("list")) {
			for (int i = 0; i < messages.size(); i++) 
				sender.sendMessage((i+1) + ": " + messages.get(i));
			
			return true;
		}
		
		if (args[0].equalsIgnoreCase("prefix")) {
			if (args.length==1) {
				sender.sendMessage("§cYou're missing a Prefix");
				return true;
			}
			
			String value = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
			
			value = value.contains("&") ? value.replaceAll("&", "§") : value;
			
			prefix = value;
			
			sender.sendMessage("§aSet Prefix to '" + value + "§a'.");
			
			this.update();
		}

		if (args[0].equalsIgnoreCase("add")) {

			if (args.length==1) {
				sender.sendMessage("§cYou're missing your Message to Add.");
				return true;
			}
			
			String value = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
			
			value = value.contains("&") ? value.replaceAll("&", "§") : value;
			
			messages.add(value);
			
			sender.sendMessage("§aAdded '" + value + "§a'.");
			
			this.update();
		}

		if (args[0].equalsIgnoreCase("remove")) {
			
			if (args.length==1) {
				sender.sendMessage("§cYou're missing a Index.");
				return true;
			}
			
			int line = -1;
			
			try {
				line = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				sender.sendMessage("§cYour 2nd Argument is not a Integer.");
				return true;
			}
			
			if (line < 1 || line > messages.size()) {
				sender.sendMessage("§cLine Must be from Range 1 to " + messages.size());
				return true;
			}
			
			messages.remove(line-1);
			
			sender.sendMessage("§aRemoved Line " + line);
			
			this.update();
			
			return true;
		}
		
		if (args[0].equalsIgnoreCase("say")) {
			
			if (args.length==1) {
				sender.sendMessage("§cYou're missing a Index.");
				return true;
			}
			
			int line = -1;
			
			try {
				line = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				sender.sendMessage("§cYour 2nd Argument is not a Integer.");
				return true;
			}
			
			if (line < 1 || line > messages.size()) {
				sender.sendMessage("§cLine Must be from Range 1 to " + messages.size());
				return true;
			}
			
			plugin.announce(prefix + " " + messages.get(line-1));
						
			this.update();
			
			return true;
		}
		
		return true;
	}
	
	public String next() {
		if (messages.size()==0)
			return "";
		
		index = index + 1;
		
		if (index == messages.size())
			index = 0;
		
		return prefix + " " + messages.get(index);
	}
	
	public long getInterval() {
		return interval;
	}
	
	public AnnouncerCommand(TownyLegacy instance) {
		
		this.plugin = instance;
		
		this.messages = this.plugin.getConfig().getStringList("Announcer Messages") == null ? messages:this.plugin.getConfig().getStringList("Announcer Messages");
		
		this.interval = this.plugin.getConfig().getLong("Announcer Interval", 20);
		
		this.prefix = this.plugin.getConfig().getString("Announcer Prefix", "");
		
	}
	
	public void close() {
		this.plugin.getConfig().set("Announcer Messages", this.messages);
		this.plugin.getConfig().set("Announcer Interval", this.interval);
		this.plugin.getConfig().set("Announcer Prefix", this.prefix);
		this.plugin.saveConfig();
	}
}
