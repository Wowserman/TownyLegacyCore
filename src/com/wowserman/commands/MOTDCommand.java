package com.wowserman.commands;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.wowserman.TownyLegacy;

public class MOTDCommand implements CommandExecutor {

	public TownyLegacy plugin;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		
		if (sender.hasPermission("extras.motd") == false) {
			sender.sendMessage("§cYou're lacking Permisison to perform this!");
			return true;
		}
		
		if (args.length==0) {
			sender.sendMessage("§cYou're missing your 1st Argument. Try adding a Integer.");
			return true;
		}
		
		if (args.length==1) {
			sender.sendMessage("§cYou're Missing a Valid MOTD.");
			return true;
		}
		
		int line = -1;
		
		try {
			line = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			sender.sendMessage("§cYour 1st Argument is not a Integer.");
			return true;
		}
		
		if (line != 1 && line != 2) {
			sender.sendMessage("§cYou can only Edit Line can only be 1 or 2.");
			return true;
		}
		
		String value = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
		
		if (line == 1)
			plugin.getMOTDListener().setLine1(value);
		else plugin.getMOTDListener().setLine2(value);
		
		sender.sendMessage("§aMotd changed to:");
		sender.sendMessage(plugin.getMOTDListener().getFormattedLine(plugin.getMOTDListener().getLine1()) + "\n" + plugin.getMOTDListener().getFormattedLine(plugin.getMOTDListener().getLine2()));
		
		return true;
	}
	
	public MOTDCommand(TownyLegacy instance) {
		
		this.plugin = instance;
		
	}
}
