package com.wowserman.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wowserman.PlayerCache;
import com.wowserman.TownyLegacy;
import com.wowserman.storage.VoteStorage.VoteSite;
import com.wowserman.tasks.DropPartyTask;

public class VoteCommand implements CommandExecutor {

	public TownyLegacy plugin;
	
	public static final String VOTE_TITLE = "Vote for Rewards";
	
	public Inventory getMenu(Player player) {
		
		PlayerCache cache = PlayerCache.get(player);
		
		Inventory inventory = Bukkit.createInventory(null, 9, VoteCommand.VOTE_TITLE);
		
		ItemStack info = new ItemStack(Material.BOOK, 1);
		ItemMeta infoMeta = info.getItemMeta();
		infoMeta.setDisplayName("§a" + player.getName() + " Statistics");
		List<String> lores = new ArrayList<String>();
		lores.add("§a" + cache.getVotes() + " Votes All Time");
		int streak = cache.getStreak();
		streak = streak < 0 ? 0:streak;
		lores.add("§a" + streak + " Day Streak");
		lores.add("§a" + cache.getMinutesLeft() + " Minutes of Flight!");
		infoMeta.setLore(lores);
		info.setItemMeta(infoMeta);
		
		inventory.setItem(0, info);

		int[] slots = {2,3,4,5,6,7,8};
		
		for (int i = 0; i<VoteSite.getSites().size(); i++) {
			if (slots.length <= i + 1)
				break;
			
			VoteSite site = VoteSite.getSites().get(i);
			boolean voted = !cache.isExpired(site);
						
			ItemStack item = new ItemStack(Material.EMERALD, 1);
			ItemMeta meta = item.getItemMeta();
			if (voted) {
				meta.setDisplayName("§c" + site.getName());
				meta.setLore(new ArrayList<String>(Arrays.asList("§7You Already Voted Today!")));
				item.setType(Material.REDSTONE);
			} else {
				meta.setDisplayName("§a" + site.getName());
				meta.setLore(new ArrayList<String>(Arrays.asList("§7Click to Vote")));
			}
			item.setItemMeta(meta);
			inventory.setItem(slots[i], item);
		}
		
		return inventory;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		
		if (args.length==1 && sender.isOp() && args[0].equalsIgnoreCase("start")) {
			new DropPartyTask(plugin, (sender instanceof Player==false) ? "Server":((Player) sender).getName(), plugin.getVoteStorage().getPartyLocation()).runTaskTimer(this.plugin, 0, 4);
			return true;
		}
		
		if (sender instanceof Player == false) {
			sender.sendMessage("§cOnly Players can perform this command!");
			return true;
		}
		
		final Player player = (Player) sender;
		
		if (args.length==1 && player.isOp() && args[0].equalsIgnoreCase("fake")) {
			PlayerCache.get(player).updateVote(VoteSite.getSites().get(0));
			return true;
		}
		
		if (args.length==1 && player.isOp() && args[0].equalsIgnoreCase("rewards")) {
			player.openInventory(this.plugin.getVoteStorage().getRewardsMenu());
			return true;
		}
		
		if (args.length==1 && player.isOp() && args[0].equalsIgnoreCase("center")) {
			this.plugin.getVoteStorage().setPartyLocation(player.getLocation());
			player.sendMessage("§aUpdated Vote Party Center.");
			return true;
		}
		
		if (args.length == 1 && player.isOp() && args[0].equalsIgnoreCase("party")) {
			for (int i = 0; i < 50; i++)
				plugin.getVoteStorage().updateVotePartyCount();
		}
		
		if (args.length==1) {
			
			if (args[0].equalsIgnoreCase("check") == false) {
				player.sendMessage("§cYour 1st Argument isn't a valid sub-command. Try 'check'");
				return true;
			}
			
			String message = plugin.getVoteStorage().getPlayerStillHasTimeMessage();
			
			message = message.contains("&") ? message.replaceAll("&", "§"):message;
			message = message.contains("<player>") ? message.replaceAll("<player>", player.getName()):message;
			message = message.contains("<votes>") ? message.replaceAll("<votes>", "" + PlayerCache.get(player).getVotes()):message;
			message = message.contains("<minutes>") ? message.replaceAll("<minutes>", PlayerCache.get(player).getMinutesLeft() + ""):message;
			
			player.sendMessage(message);
			player.sendTitle("§b" + (50-plugin.getVoteStorage().getVotePartyCount()) + " More Votes", "§funtil we start a §dVote Party§f", 0, 100, 0);
			
			return true;
		}
		
		if (args.length==2 && player.isOp()) {
			Player target = Bukkit.getPlayer(args[0]);
			
			if (target==null) {
				sender.sendMessage("§cTarget isn't online.");
				return true;
			}
			
			try {
				Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				
				sender.sendMessage("§cYour 2nd Argument is not a Integer.");
				
				return true;
			}
						
			sender.sendMessage("§aAdded " + Integer.parseInt(args[1]) + " minutes to " + args[0]);
			
			return true;
		}
		
		player.openInventory(this.getMenu(player));
		
		return true;
	}
	
	public VoteCommand(TownyLegacy instance) {
		
		this.plugin = instance;
		
	}

}
