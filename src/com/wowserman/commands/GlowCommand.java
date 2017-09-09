package com.wowserman.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.inventivetalent.glow.GlowAPI.Color;

import com.wowserman.TownyLegacy;

public class GlowCommand implements CommandExecutor {

	/*
	 * Colors
	 * 
	 * 0 Red
	 * 1 Orange
	 * 2 Yellow
	 * 3 Green
	 * 4 Blue
	 * 5 Purple
	 * 6 White
	 * 7 Gray
	 * 8 Black
	 */
	
	public TownyLegacy plugin;
	
	final public static String menuName = "Pick a Color.";
	
	private Inventory menu = Bukkit.createInventory(null, 9, menuName);
	
	private Inventory staffMenu = Bukkit.createInventory(null, 18, menuName);
		
	final public static String getNameForColor(Color color) {
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
		case PURPLE:
			return "§5Purple";
		case WHITE:
			return "§fWhite";
		case GRAY:
			return "§8Gray";
		case BLACK:
			return "§0Black";
		default:
			return "§4Clear";
		}
	}
	
	final public static byte getByteForColor(Color color) {
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
		case PURPLE:
			return (byte) 10;
		case WHITE:
			return (byte) 0;
		case GRAY:
			return (byte) 7;
		case BLACK:
			return (byte) 15;
		default:
			return 0;
		}
	}
	
	public static ItemStack getClickableItemFor(Color color) {
		
		ItemStack item = color == Color.NONE ? new ItemStack(Material.BARRIER, 1):new ItemStack(Material.STAINED_GLASS_PANE, 1);
		
		ItemMeta meta = item.getItemMeta();;
		
		meta.setDisplayName(getNameForColor(color));
		
		item.setItemMeta(meta);
		
		item.setDurability(getByteForColor(color));		
		
		return item;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		
//		if (sender.hasPermission("extras.glow") == false) {
//			sender.sendMessage("§cYou're lacking Permisison to perform this!");
//			return true;
//		}
		
		if (sender instanceof Player == false) {
			sender.sendMessage("§cOnly Players can perform this command!");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (sender.hasPermission("extras.glow.staff"))
			player.openInventory(staffMenu);
		else player.openInventory(menu);
		
		return true;
	}
	
	public GlowCommand(TownyLegacy instance) {
		
		this.plugin = instance;
		
		menu.setItem(1, getClickableItemFor(Color.RED));
		menu.setItem(2, getClickableItemFor(Color.GOLD));
		menu.setItem(3, getClickableItemFor(Color.YELLOW));
		menu.setItem(4, getClickableItemFor(Color.NONE));
		menu.setItem(5, getClickableItemFor(Color.GREEN));
		menu.setItem(6, getClickableItemFor(Color.BLUE));
		menu.setItem(7, getClickableItemFor(Color.PURPLE));
		
		// Staff Menu
		
		staffMenu.setItem(1, getClickableItemFor(Color.RED));
		staffMenu.setItem(2, getClickableItemFor(Color.GOLD));
		staffMenu.setItem(3, getClickableItemFor(Color.YELLOW));
		staffMenu.setItem(4, getClickableItemFor(Color.NONE));
		staffMenu.setItem(5, getClickableItemFor(Color.GREEN));
		staffMenu.setItem(6, getClickableItemFor(Color.BLUE));
		staffMenu.setItem(7, getClickableItemFor(Color.PURPLE));
		staffMenu.setItem(12, getClickableItemFor(Color.WHITE));
		staffMenu.setItem(13, getClickableItemFor(Color.GRAY));
		staffMenu.setItem(14, getClickableItemFor(Color.BLACK));
	}
}
