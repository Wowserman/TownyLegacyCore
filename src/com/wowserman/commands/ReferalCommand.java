package com.wowserman.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wowserman.TownyLegacy;

public class ReferalCommand implements CommandExecutor {

	public TownyLegacy plugin;
	
	private String command = "crates key <player> Ancient 1";
	
	private Hashtable<String, List<String>> referals = new Hashtable<String, List<String>>();
	
	public void close() {
		referals.clear();
	}
	
	public void remeoveReferee(Player player) {
		this.referals.remove(player.getName());
	}
	
	public void removedRefered(Player player) {
		List<String> referees = Arrays.asList(referals.keySet().toArray(new String[referals.keySet().size()]));
		
		for (int i = 0; i < referals.size(); i++) {
			for (String refered:referals.get(referees.get(i))) {
				if (refered.equalsIgnoreCase(player.getName())) {
					referals.get(referees.get(i)).remove(i);
				}
			}
			
			if (referals.get(referees).size()==0) {
				referals.remove(referees.get(i));
				i--;
			}
		}
	}
	
	public List<String> getReferees(Player player) {
		List<String> list = new ArrayList<String>();
		
		for (String referee:referals.keySet()) {
			for (String refers:referals.get(referee)) {
				if (refers.equalsIgnoreCase(player.getName())) {
					list.add(referee);

				}
			}
		}
		
		return list;
	}
	
	public boolean isRefered(Player player) {
		for (String key:referals.keySet()) {
			if (referals.get(key).contains(player.getName()))
				return true;
		}
		
		return false;
	}
	
	public void check(Player player) {
		if (player.isOnline())
			return;
		
		if (this.isRefered(player)==false)
			return;
		
		for (String refereeName:this.getReferees(player)) {
			System.out.print("Checking " + refereeName + "'s Reference");
			final Player referee = Bukkit.getPlayer(refereeName);
			
			if (referee==null) {
				this.remeoveReferee(player);
				continue;
			}
			
			if (referee.isOnline()==false)
				continue;
			
			referee.sendMessage("§3" + player.getName() + " §bis on the Server! Thanks for helping bring them over here! As a reward, here is §31x Ancient Key");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.contains("<player>") ? command.replaceAll("<player>", refereeName):command);
			
			this.remeoveReferee(referee);
		}
		
		this.removedRefered(player);
	}
	
//	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
				
//		if (sender instanceof Player == false) {
//			sender.sendMessage("§cOnly Players can perform this command!");
//			return true;
//		}
//		
//		if (args.length==0) {
//			
//			sender.sendMessage("§cYou're missing your 1st Argument. Try adding a Player's Name.");
//			
//			return true;
//		}
//		
//		final Player player = (Player) sender;
//		
//		if (args.length>=2 && player.hasPermission("extras.refer")) {
//			
//			if (args[0].equalsIgnoreCase("set")) {
//				
//				this.command = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
//				
//				player.sendMessage("§aSet Command to '" + command + "'");
//			}
//			
//			else if (args[0].equalsIgnoreCase("fake")) {
//				
//				final Player user = Bukkit.getPlayer(args[1]);
//				
//				if (user==null) {
//					player.sendMessage("§c" + user + " isn't online!");
//				}
//				
//				this.check(player);
//			}
//			
//			else {
//				player.sendMessage("§cYou're missing your 1st Argument. Try adding a 'set' or 'fake'.");
//			}
//			
//			return true;
//		}
//		
//		final String username = args[0];
//		
//		if (Bukkit.getOfflinePlayer(username).hasPlayedBefore()) {
//			player.sendMessage("§c" + username + " has played before!");
//			// return true;
//		}
//		
//		if (this.referals.getOrDefault(player.getName(), new ArrayList<String>()).contains(username)) {
//			player.sendMessage("§c" + username + " is already in your Refered Player's List, and they haven't joined yet!");
//			return true;
//		}
//		
//		this.referals.getOrDefault(player.getName(), new ArrayList<String>()).add(username);
//		
//		player.sendMessage("§aAdded '" + player.getName() + "' to your refered Players. When He/She joins the Server you will be rewarded if you're online!");
		
		
		return true;
	}
	
	public ReferalCommand(TownyLegacy instance) {
		
		this.plugin = instance;
		
	}
}
