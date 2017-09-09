package com.wowserman.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import com.wowserman.TownyLegacy;

public class RenameCommand implements CommandExecutor {

public TownyLegacy plugin;
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		
		if (sender.hasPermission("extras.rename") == false) {
			sender.sendMessage("§cYou're lacking Permisison to perform this!");
			return true;
		}
		
		if (sender instanceof Player == false) {
			sender.sendMessage("§cOnly Players can perform this command!");
			return true;
		}
		
		if (args.length >= 1 == false) {
			sender.sendMessage("§cYou're missing a name for the Item. Try adding a Name.");
			return true;
		}
		
		String newName = String.join(" ", args);
		
		newName = newName.contains("&") ? newName.replaceAll("&", "§") : newName;
		
		Player player = (Player) sender;
		
		if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
			sender.sendMessage("§cYou must be holding an Item to perform this command!");
			return true;
		}
		
		if (player.getItemInHand().getAmount() != 1) {
			sender.sendMessage("§cYou can only be holding 1 of that Item in your hand to perform this command!");
			return true;
		}
		
		ItemMeta meta = player.getItemInHand().getItemMeta();
		
		meta.setDisplayName(newName);

		player.getItemInHand().setItemMeta(meta);
		
		player.updateInventory();
		
		player.setGlowing(true);
		
		sender.sendMessage("§aItems Name was changed to '" + newName + "§a'.");
		
		return true;
	}
	
	public RenameCommand(TownyLegacy instance) {
		
		this.plugin = instance;
		
	}
}
