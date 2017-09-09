package com.wowserman.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wowserman.TownyLegacy;

public class ChatColorCommand implements CommandExecutor {

	public TownyLegacy plugin;
	
	final public static String menuName = "Pick a Chat Color.";

	public static Inventory menu = Bukkit.createInventory(null, 9, menuName);
	
	final public static String getNameForColor(ChatColor color) {
		switch (color) {
		case RED:
			return "§cRed";
		case GOLD:
			return "§6Orange";
		case YELLOW:
			return "§eYellow";
		case GREEN:
			return "§aGreen";
		case BLUE:
			return "§bBlue";
		case DARK_PURPLE:
			return "§5Purple";
		case WHITE:
			return "§fWhite";
		case GRAY:
			return "§7Gray";
		case DARK_GRAY:
			return "§8Gray";
		case BLACK:
			return "§0Black";
		default:
			return "§4Clear";
		}
	}
	
	final public static byte getByteForColor(ChatColor color) {
		switch (color) {
		case RED:
			return (byte) 14;
		case GOLD:
			return (byte) 1;
		case YELLOW:
			return (byte) 4;
		case GREEN:
			return (byte) 5;
		case BLUE:
			return (byte) 3;
		case DARK_PURPLE:
			return (byte) 10;
		case WHITE:
			return (byte) 0;
		case GRAY:
			return (byte) 8;
		case DARK_GRAY:
			return (byte) 7;
		case BLACK:
			return (byte) 15;
		default:
			return 0;
		}
	}
	
	public static ItemStack getClickableItemFor(ChatColor color) {
		
		ItemStack item = color == ChatColor.WHITE ? new ItemStack(Material.BARRIER, 1):new ItemStack(Material.STAINED_GLASS_PANE, 1);
		
		ItemMeta meta = item.getItemMeta();;
		
		meta.setDisplayName(getNameForColor(color));
		
		item.setItemMeta(meta);
		
		item.setDurability(getByteForColor(color));		
		
		return item;
	}
	
	static {
		menu.setItem(1, getClickableItemFor(ChatColor.RED));
		menu.setItem(2, getClickableItemFor(ChatColor.GOLD));
		menu.setItem(3, getClickableItemFor(ChatColor.YELLOW));
		menu.setItem(4, getClickableItemFor(ChatColor.WHITE));
		menu.setItem(5, getClickableItemFor(ChatColor.GREEN));
		menu.setItem(6, getClickableItemFor(ChatColor.BLUE));
		menu.setItem(7, getClickableItemFor(ChatColor.DARK_PURPLE));
//		menu.setItem(12, getClickableItemFor(ChatColor.GRAY));
//		menu.setItem(13, getClickableItemFor(ChatColor.DARK_GRAY));
//		menu.setItem(14, getClickableItemFor(ChatColor.BLACK));
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		
//		if (sender.hasPermission("extras.chatcolor") == false) {
//			sender.sendMessage("§cYou're lacking Permisison to perform this!");
//			return true;
//		}
		
		if (sender instanceof Player == false) {
			sender.sendMessage("§cOnly Players can perform this command!");
			return true;
		}
		
		Player player = (Player) sender;
		
		player.openInventory(menu);
		
		
		return true;
	}
	
	public ChatColorCommand(TownyLegacy instance) {
		this.plugin = instance;
	}
}
