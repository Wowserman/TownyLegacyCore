package com.wowserman.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wowserman.TownyLegacy;
import com.wowserman.tasks.DropPartyTask;

public class DropPartyCommand implements CommandExecutor {

	public TownyLegacy plugin;
	
	private List<DropPartyTask> dps = new ArrayList<DropPartyTask>();
	
	public void remove(Player player) {
		for (int i = 0; i < dps.size(); i++) {
			if (dps.get(i).getStarter().equals(player.getUniqueId().toString())) {
				dps.remove(i);
				i--;
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		
		if (sender.hasPermission("extras.dropparty") == false) {
			sender.sendMessage("§cYou're lacking Permisison to perform this!");
			return true;
		}
		
		if (sender instanceof Player == false) {
			sender.sendMessage("§cOnly Players can perform this command!");
			return true;
		}
		
		Player player = (Player) sender;
		
		for (DropPartyTask task:dps) 
			if (task.getStarter().equals(player.getName())) {
				player.sendMessage("§cYou already have a Drop Party Running!");
				return true;
			}
		
		DropPartyTask task = new DropPartyTask(plugin, player);
		
		task.runTaskTimer(plugin, 0, 4);
		
		dps.add(task);
		
		return true;
	}
	
	public DropPartyCommand(TownyLegacy instance) {
		
		this.plugin = instance;
		
	}
}
