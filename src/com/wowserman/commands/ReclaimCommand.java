package com.wowserman.commands;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wowserman.TownyLegacy;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ReclaimCommand implements CommandExecutor {
	
	public TownyLegacy plugin;
	
	public TextComponent getTextComponent(Player player, int index) {

		TextComponent message = new TextComponent("- ");
		message.setColor(ChatColor.DARK_GRAY);

		TextComponent chest = new TextComponent("Chest " + (index + 1));
		chest.setColor(ChatColor.AQUA);
		chest.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND,
				"/reclaim " + plugin.getChestStorage().getChestKeys(player).get(index)));
		chest.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder("Click to Reclaim!").create()));

		TextComponent expires = new TextComponent(" (Expires "
				+ plugin.getChestStorage().getExpiration(plugin.getChestStorage().getChestKeys(player).get(index))
				+ ")");
		expires.setColor(ChatColor.GRAY);
		expires.setItalic(true);

		message.addExtra(chest);
		message.addExtra(expires);

		return message;
	}
	
	public void listReclaimsAvalible(Player player) {
		if (plugin.getChestStorage().hasChests(player)) {
			player.sendMessage("§aChests to Reclaim:");
			for (int i = 0; i < plugin.getChestStorage().getChestKeys(player).size(); i++) 
				player.spigot().sendMessage(getTextComponent(player, i));
				
			player.sendMessage("§7§oClick to Reclaim!");
		} else {
			player.sendMessage("§cYou have no Pending Chests to Reclaim!");
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		
		// Not Needed
		//if (sender.hasPermission("extras.reclaim") == false) {
		//	sender.sendMessage("§cYou're lacking Permisison to perform this!");
		//	return true;
		//}
		
		if (sender instanceof Player == false) {
			sender.sendMessage("§cOnly Players can perform this command!");
			return true;
		}
		
		final Player player = (Player) sender;
		
		if (args.length == 0) {
			listReclaimsAvalible(player);
			return true;
		}
		
		final String chest = String.join(" ", args);
				
		if (!plugin.getChestStorage().exists(chest)) {
			sender.sendMessage("§cThat Chest doesn't Exist!");
			return true;
		}
		
		if (plugin.getChestStorage().wasReclaimed(chest)) {
			sender.sendMessage("§cThat Chest has already been Reclaimed!");
			return true;
		}
		
		if (player.getInventory().firstEmpty()==-1) {
			sender.sendMessage("§7§oThere wasn't any space in your Inventory, so your Chest has been dropped at your feet.");
			player.getWorld().dropItem(player.getLocation(), getItemStackForStoredChest(chest));
		} else {
			player.getInventory().setItem(player.getInventory().firstEmpty(), getItemStackForStoredChest(chest));
		}
		
		return true;
	}
	
	public ItemStack getItemStackForStoredChest(String chest) {
		
		ItemStack item = new ItemStack(Material.CHEST, 1);
		
		if (!plugin.getChestStorage().exists(chest))
			return item;
		
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName("§bStored Chest");
		meta.setLore(Arrays.asList(new String[]{"§8ID:" + chest}));
		
		item.setItemMeta(meta);
		
		plugin.getChestStorage().reclaim(chest);
		
		return item;
	}
	
	public String getStoredChestFromItemStack(ItemStack item) {
		
		if (item==null) 
			return null;
		
		if (!item.hasItemMeta())
			return null;
		
		if (item.getItemMeta().getDisplayName() != "§bStored Chest")
			return null;
		
		if (item.getItemMeta().getLore().size() != 1)
			return null;
		
		return ChatColor.stripColor(item.getItemMeta().getLore().get(0)).split(":")[1];
	}
	
	public ReclaimCommand(TownyLegacy instance) {
		
		this.plugin = instance;
		
	}
}
