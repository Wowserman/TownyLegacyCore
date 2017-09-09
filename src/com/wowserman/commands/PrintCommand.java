package com.wowserman.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.wowserman.TownyLegacy;

public class PrintCommand implements CommandExecutor {

	public TownyLegacy plugin;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		
		if (sender.hasPermission("extras.print") == false) {
			sender.sendMessage("§cYou're lacking Permisison to perform this!");
			return true;
		}
		
		String msg = String.join(" ", args);
		
		msg = msg.contains("&") ? msg.replaceAll("&", "§") : msg;
		
		plugin.announce(msg);
		
		return true;
	}
	
	public PrintCommand(TownyLegacy instance) {
		
		this.plugin = instance;
		
	}

}
