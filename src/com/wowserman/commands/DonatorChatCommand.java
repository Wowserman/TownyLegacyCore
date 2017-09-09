package com.wowserman.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wowserman.TownyLegacy;

public class DonatorChatCommand implements CommandExecutor {

	public TownyLegacy plugin;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		
		if (sender.hasPermission("extras.donatorchat") == false) {
			sender.sendMessage("§cYou're lacking Permisison to perform this!");
			return true;
		}
		
		if (sender instanceof Player == false) {
			sender.sendMessage("§cOnly Players can perform this command!");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (!plugin.getDonatorChatListener().isInChat(player)) {
			plugin.getDonatorChatListener().join(player);
			sender.sendMessage("§aNow Talking in Donator Chat");
		} else {
			plugin.getDonatorChatListener().leave(player);
			sender.sendMessage("§aNow Talking in Global Chat");
		}
		
		return true;
	}
	
	public DonatorChatCommand(TownyLegacy instance) {
		
		this.plugin = instance;
		
	}
}
