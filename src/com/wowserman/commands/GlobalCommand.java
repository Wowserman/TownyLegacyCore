package com.wowserman.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wowserman.TownyLegacy;

public class GlobalCommand implements CommandExecutor {

	public TownyLegacy plugin;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		
		if (sender instanceof Player == false) {
			sender.sendMessage("§cOnly Players can perform this command!");
			return true;
		}
		
		Player player = (Player) sender;
		
		this.plugin.getDonatorChatListener().leave(player);
		this.plugin.getStaffChatListener().leave(player);
		
		sender.sendMessage("§aNow Talking in Global Chat");
		
		return true;
	}
	
	public GlobalCommand(TownyLegacy instance) {
		
		this.plugin = instance;
		
	}
}
