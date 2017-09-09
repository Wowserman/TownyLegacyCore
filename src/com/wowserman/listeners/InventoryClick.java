package com.wowserman.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.inventivetalent.glow.GlowAPI.Color;

import com.wowserman.PlayerCache;
import com.wowserman.TownyLegacy;
import com.wowserman.commands.ChatColorCommand;
import com.wowserman.commands.GlowCommand;

public class InventoryClick implements Listener {
	
	public InventoryClick(TownyLegacy plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		
		if (e.getInventory().getName().equals(GlowCommand.menuName) == false)
			return;
		
		ItemStack item = e.getCurrentItem();
		
		if (item==null || item.getType()==Material.AIR) 
			return;
		
		if (item.getItemMeta().getDisplayName().equals(GlowCommand.getNameForColor(Color.RED))) {
			
			if (e.getWhoClicked().hasPermission("extras.glow.red") == false && e.getWhoClicked().hasPermission("extras.glow.all") == false) {
				
				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
				
				e.setCancelled(true);
				
				e.getWhoClicked().closeInventory();
				
				return;
			}
			
			PlayerCache.get((Player) e.getWhoClicked()).setGlow(Color.RED);
			e.getWhoClicked().sendMessage("§oYou are now Glowing in " + GlowCommand.getNameForColor(Color.RED));
		}
		else if (item.getItemMeta().getDisplayName().equals(GlowCommand.getNameForColor(Color.GOLD))) {
			
			if (e.getWhoClicked().hasPermission("extras.glow.orange") == false && e.getWhoClicked().hasPermission("extras.glow.all") == false) {
				
				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
				
				e.setCancelled(true);
				
				e.getWhoClicked().closeInventory();
				
				return;
			}
			PlayerCache.get((Player) e.getWhoClicked()).setGlow(Color.GOLD);
			e.getWhoClicked().sendMessage("§oYou are now Glowing in " + GlowCommand.getNameForColor(Color.GOLD));
		}
		else if (item.getItemMeta().getDisplayName().equals(GlowCommand.getNameForColor(Color.YELLOW))) {
			
			if (e.getWhoClicked().hasPermission("extras.glow.yellow") == false && e.getWhoClicked().hasPermission("extras.glow.all") == false) {
				
				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
				
				e.setCancelled(true);
				
				e.getWhoClicked().closeInventory();
				
				return;
			}
			PlayerCache.get((Player) e.getWhoClicked()).setGlow(Color.YELLOW);
			e.getWhoClicked().sendMessage("§oYou are now Glowing in " + GlowCommand.getNameForColor(Color.YELLOW));
		}
		else if (item.getItemMeta().getDisplayName().equals(GlowCommand.getNameForColor(Color.GREEN))) {
			
			if (e.getWhoClicked().hasPermission("extras.glow.green") == false && e.getWhoClicked().hasPermission("extras.glow.all") == false) {
				
				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
				
				e.setCancelled(true);
				
				e.getWhoClicked().closeInventory();
				
				return;
			}
			PlayerCache.get((Player) e.getWhoClicked()).setGlow(Color.GREEN);
			e.getWhoClicked().sendMessage("§oYou are now Glowing in " + GlowCommand.getNameForColor(Color.GREEN));
		}
		else if (item.getItemMeta().getDisplayName().equals(GlowCommand.getNameForColor(Color.BLUE))) {
			
			if (e.getWhoClicked().hasPermission("extras.glow.blue") == false && e.getWhoClicked().hasPermission("extras.glow.all") == false) {
				
				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
				
				e.setCancelled(true);
				
				e.getWhoClicked().closeInventory();
				
				return;
			}
			
			PlayerCache.get((Player) e.getWhoClicked()).setGlow(Color.BLUE);
			e.getWhoClicked().sendMessage("§oYou are now Glowing in " + GlowCommand.getNameForColor(Color.BLUE));
		}
		else if (item.getItemMeta().getDisplayName().equals(GlowCommand.getNameForColor(Color.PURPLE))) {
			
			if (e.getWhoClicked().hasPermission("extras.glow.purple") == false && e.getWhoClicked().hasPermission("extras.glow.all") == false) {
				
				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
				
				e.setCancelled(true);
				
				e.getWhoClicked().closeInventory();
				
				return;
			}
			PlayerCache.get((Player) e.getWhoClicked()).setGlow(Color.PURPLE);
			e.getWhoClicked().sendMessage("§oYou are now Glowing in " + GlowCommand.getNameForColor(Color.PURPLE));
		}
		
		// all COLORS
		else if (item.getItemMeta().getDisplayName().equals(GlowCommand.getNameForColor(Color.WHITE))) {
			
			if (e.getWhoClicked().hasPermission("extras.glow.all") == false) {
				
				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
				
				e.setCancelled(true);
				
				e.getWhoClicked().closeInventory();
				
				return;
			}
			PlayerCache.get((Player) e.getWhoClicked()).setGlow(Color.WHITE);
			e.getWhoClicked().sendMessage("§oYou are now Glowing in " + GlowCommand.getNameForColor(Color.WHITE));
		}
		else if (item.getItemMeta().getDisplayName().equals(GlowCommand.getNameForColor(Color.GRAY))) {
			
			if (e.getWhoClicked().hasPermission("extras.glow.all") == false) {
				
				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
				
				e.setCancelled(true);
				
				e.getWhoClicked().closeInventory();
				
				return;
			}
			PlayerCache.get((Player) e.getWhoClicked()).setGlow(Color.GRAY);
			e.getWhoClicked().sendMessage("§oYou are now Glowing in " + GlowCommand.getNameForColor(Color.GRAY));
		}
		else if (item.getItemMeta().getDisplayName().equals(GlowCommand.getNameForColor(Color.BLACK))) {
			
			if (e.getWhoClicked().hasPermission("extras.glow.all") == false) {
				
				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
				
				e.setCancelled(true);
				
				e.getWhoClicked().closeInventory();
				
				return;
			}
			PlayerCache.get((Player) e.getWhoClicked()).setGlow(Color.BLACK);
			e.getWhoClicked().sendMessage("§oYou are now Glowing in " + GlowCommand.getNameForColor(Color.BLACK));
		}
		
		
		else if (item.getItemMeta().getDisplayName().equals(GlowCommand.getNameForColor(Color.NONE))) {
			
			PlayerCache.get((Player) e.getWhoClicked()).setGlow(Color.NONE);
			e.getWhoClicked().sendMessage("§oYou are now Glowing in " + GlowCommand.getNameForColor(Color.NONE));
		}
		
		e.setCancelled(true);
		
		e.getWhoClicked().closeInventory();
		
		
	}
	
	@EventHandler
	public void click2(InventoryClickEvent e) {
		
		if (e.getInventory().getName().equals(ChatColorCommand.menuName) == false)
			return;
		
		ItemStack item = e.getCurrentItem();
		
		if (item==null || item.getType()==Material.AIR) 
			return;
		
		if (item.getItemMeta().getDisplayName().equals(ChatColorCommand.getNameForColor(ChatColor.RED))) {
			
			if (e.getWhoClicked().hasPermission("extras.chatcolor.red") == false && e.getWhoClicked().hasPermission("extras.chatcolor.all") == false) {
				
				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
				
				e.setCancelled(true);
				
				e.getWhoClicked().closeInventory();
				
				return;
			}
			
			PlayerCache.get((Player) e.getWhoClicked()).setChatColor(ChatColor.RED);
			e.getWhoClicked().sendMessage("§oYou are now Talking in " + ChatColorCommand.getNameForColor(ChatColor.RED));
		}
		else if (item.getItemMeta().getDisplayName().equals(ChatColorCommand.getNameForColor(ChatColor.GOLD))) {
			
			if (e.getWhoClicked().hasPermission("extras.chatcolor.orange") == false && e.getWhoClicked().hasPermission("extras.chatcolor.all") == false) {
				
				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
				
				e.setCancelled(true);
				
				e.getWhoClicked().closeInventory();
				
				return;
			}
			PlayerCache.get((Player) e.getWhoClicked()).setChatColor(ChatColor.GOLD);
			e.getWhoClicked().sendMessage("§oYou are now Talking in " + ChatColorCommand.getNameForColor(ChatColor.GOLD));
		}
		else if (item.getItemMeta().getDisplayName().equals(ChatColorCommand.getNameForColor(ChatColor.YELLOW))) {
			
			if (e.getWhoClicked().hasPermission("extras.chatcolor.yellow") == false && e.getWhoClicked().hasPermission("extras.chatcolor.all") == false) {
				
				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
				
				e.setCancelled(true);
				
				e.getWhoClicked().closeInventory();
				
				return;
			}
			PlayerCache.get((Player) e.getWhoClicked()).setChatColor(ChatColor.YELLOW);
			e.getWhoClicked().sendMessage("§oYou are now Talking in " + ChatColorCommand.getNameForColor(ChatColor.YELLOW));
		}
		else if (item.getItemMeta().getDisplayName().equals(ChatColorCommand.getNameForColor(ChatColor.GREEN))) {
			
			if (e.getWhoClicked().hasPermission("extras.chatcolor.green") == false && e.getWhoClicked().hasPermission("extras.chatcolor.all") == false) {
				
				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
				
				e.setCancelled(true);
				
				e.getWhoClicked().closeInventory();
				
				return;
			}
			PlayerCache.get((Player) e.getWhoClicked()).setChatColor(ChatColor.GREEN);
			e.getWhoClicked().sendMessage("§oYou are now Talking in " + ChatColorCommand.getNameForColor(ChatColor.GREEN));
		}
		else if (item.getItemMeta().getDisplayName().equals(ChatColorCommand.getNameForColor(ChatColor.BLUE))) {
			
			if (e.getWhoClicked().hasPermission("extras.chatcolor.blue") == false && e.getWhoClicked().hasPermission("extras.chatcolor.all") == false) {
				
				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
				
				e.setCancelled(true);
				
				e.getWhoClicked().closeInventory();
				
				return;
			}
			
			PlayerCache.get((Player) e.getWhoClicked()).setChatColor(ChatColor.BLUE);
			e.getWhoClicked().sendMessage("§oYou are now Talking in " + ChatColorCommand.getNameForColor(ChatColor.BLUE));
		}
		else if (item.getItemMeta().getDisplayName().equals(ChatColorCommand.getNameForColor(ChatColor.DARK_PURPLE))) {
			
			if (e.getWhoClicked().hasPermission("extras.chatcolor.purple") == false && e.getWhoClicked().hasPermission("extras.chatcolor.all") == false) {
				
				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
				
				e.setCancelled(true);
				
				e.getWhoClicked().closeInventory();
				
				return;
			}
			PlayerCache.get((Player) e.getWhoClicked()).setChatColor(ChatColor.DARK_PURPLE);
			e.getWhoClicked().sendMessage("§oYou are now Talking in " + ChatColorCommand.getNameForColor(ChatColor.DARK_PURPLE));
		}
		
		// all COLORS
//		else if (item.getItemMeta().getDisplayName().equals(ChatColorCommand.getNameForColor(ChatColor.WHITE))) {
//			
//			if (e.getWhoClicked().hasPermission("extras.chatcolor.all") == false) {
//				
//				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
//				
//				e.setCancelled(true);
//				
//				e.getWhoClicked().closeInventory();
//				
//				return;
//			}
//			plugin.getChatColorStorage().setColor((Player) e.getWhoClicked(),  ChatColor.WHITE);
//			e.getWhoClicked().sendMessage("§oYou are now Talking in " + ChatColorCommand.getNameForColor(ChatColor.WHITE));
//		}
//		else if (item.getItemMeta().getDisplayName().equals(ChatColorCommand.getNameForColor(ChatColor.GRAY))) {
//			
//			if (e.getWhoClicked().hasPermission("extras.chatcolor.all") == false) {
//				
//				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
//				
//				e.setCancelled(true);
//				
//				e.getWhoClicked().closeInventory();
//				
//				return;
//			}
//			plugin.getChatColorStorage().setColor((Player) e.getWhoClicked(), ChatColor.GRAY);
//			e.getWhoClicked().sendMessage("§oYou are now Talking in " + ChatColorCommand.getNameForColor(ChatColor.GRAY));
//		}
//		else if (item.getItemMeta().getDisplayName().equals(ChatColorCommand.getNameForColor(ChatColor.BLACK))) {
//			
//			if (e.getWhoClicked().hasPermission("extras.chatcolor.all") == false) {
//				
//				e.getWhoClicked().sendMessage("§cYou're lacking Permisison to perform this!");
//				
//				e.setCancelled(true);
//				
//				e.getWhoClicked().closeInventory();
//				
//				return;
//			}
//			plugin.getChatColorStorage().setColor((Player) e.getWhoClicked(), ChatColor.BLACK);
//			e.getWhoClicked().sendMessage("§oYou are now Talking in " + ChatColorCommand.getNameForColor(ChatColor.BLACK));
//		}
		
		
		else if (item.getItemMeta().getDisplayName().equals(ChatColorCommand.getNameForColor(ChatColor.WHITE))) {
			
			PlayerCache.get((Player) e.getWhoClicked()).setChatColor(ChatColor.WHITE);
			e.getWhoClicked().sendMessage("§oYou are now Talking in " + ChatColorCommand.getNameForColor(ChatColor.WHITE));
		}
		
		e.setCancelled(true);
		
		e.getWhoClicked().closeInventory();
		
		
	}
}
