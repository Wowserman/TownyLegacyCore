package com.wowserman.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wowserman.TownyLegacy;

public class StoreCommand implements CommandExecutor {

	public TownyLegacy plugin;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		
		if (sender.hasPermission("extras.store") == false) {
			sender.sendMessage("§cYou're lacking Permisison to perform this!");
			return true;
		}
		
		if (sender instanceof Player == false) {
			sender.sendMessage("§cOnly Players can perform this command!");
			return true;
		}
		
		final Player player = (Player) sender;
		
		Block targetBlock = plugin.getTargetBlock(player, 10);
		
		if (targetBlock.getState() == null || targetBlock.getState() instanceof Chest == false) {
			sender.sendMessage("§cTarget Block isn't a Chest!");
			return true;
		}
		
		Chest chest = (Chest) targetBlock.getState();
		
		if (args.length==0) {
			
			sender.sendMessage("§cYou're missing your 1st Argument. Try adding a Player's Name.");
			
			return true;
		}
		
		String targetName = args[0];
		
		plugin.getChestStorage().write(chest, targetName, player);
		
		chest.getInventory().clear();

		chest.update();
		
		targetBlock.setType(Material.AIR, true);
		
		sender.sendMessage("§aSaved Chest to Storage of " + targetName + ".");
		
		if (Bukkit.getPlayer(targetName)!=null)
			plugin.getChestStorage().notifyPlayer(Bukkit.getPlayer(targetName));
		
		return true;
	}
	
	public ItemStack getItemStackForStoredChest(String chest) {
		
		ItemStack item = new ItemStack(Material.CHEST, 1);
		
		if (!plugin.getChestStorage().exists(chest))
			return item;
		
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName("§bStored Chest");
		meta.setLore(Arrays.asList(new String[]{"§8ID:", "§8" + chest}));
		
		item.setItemMeta(meta);
		
		return item;
	}
	
	public String getStoredChestFromItemStack(ItemStack item) {
		
		if (item==null)
			return null;
		
		if (!item.hasItemMeta())
			return null;
		
		if (item.getItemMeta().getDisplayName() != "§bStored Chest")
			return null;
		
		if (item.getItemMeta().getLore().size() != 2)
			return null;
		
		return ChatColor.stripColor(item.getItemMeta().getLore().get(1));
	}
	
	public StoreCommand(TownyLegacy instance) {
		
		this.plugin = instance;
		
	}
}
