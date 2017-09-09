package com.wowserman.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wowserman.TownyLegacy;

public class PlayerShopCommand implements CommandExecutor {

	private TownyLegacy plugin;
	
	public PlayerShopCommand(TownyLegacy instance) {
		this.plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		
		if (sender instanceof Player == false) {
			sender.sendMessage("§cOnly Players can perform this command!");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (sender.hasPermission("extras.playershop") == false || args.length == 0) {
			sender.sendMessage("§aYou have created " + plugin.getPlayerShopStorage().getShopCount(player) + "/" + plugin.getPlayerShopStorage().getMax() + " avalible ChestShops in Shop");
			return true;
		} else {
			
			if (args[0].equalsIgnoreCase("set")) {
				
				if (plugin.getWorldEditHook().isEnabled()==false) {
					sender.sendMessage("§cWorld Edit isn't Enabled.");
					return true;
				}
				
				plugin.getPlayerShopListener().setBorders(plugin.getWorldEditHook().getSelection(player));
				sender.sendMessage("§aUpdated Player Shop Boundaries to " + plugin.getPlayerShopListener().getBorders());
				return true;
			}
			
			if (args[0].equalsIgnoreCase("check")) {
				sender.sendMessage("§a" + plugin.getPlayerShopListener().getBorders());
				return true;
			}
			
			sender.sendMessage("§cYou're missing a valid 1st Argument. Try adding 'set' or 'check'");
			
			return true;
		}
	}
}
