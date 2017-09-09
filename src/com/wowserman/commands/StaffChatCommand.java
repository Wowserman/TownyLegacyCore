package com.wowserman.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wowserman.TownyLegacy;

public class StaffChatCommand implements CommandExecutor {

	public TownyLegacy plugin;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		
		if (sender.hasPermission("extras.staffchat") == false) {
			sender.sendMessage("§cYou're lacking Permisison to perform this!");
			return true;
		}
		
		if (sender instanceof Player == false) {
			sender.sendMessage("§cOnly Players can perform this command!");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (!plugin.getStaffChatListener().isInChat(player)) {
			plugin.getStaffChatListener().join(player);
			sender.sendMessage("§aNow Talking in Staff Chat");
		} else {
			plugin.getStaffChatListener().leave(player);
			sender.sendMessage("§aNow Talking in Global Chat");
		}
		
		return true;
	}
	
	public StaffChatCommand(TownyLegacy instance) {
		
		this.plugin = instance;
		
	}
}
