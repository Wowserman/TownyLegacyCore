package com.wowserman.listeners;

import java.math.RoundingMode;
import java.text.NumberFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.wowserman.TownyLegacy;

public class MobAttack implements Listener {

	private TownyLegacy plugin;
	
	private NumberFormat format = NumberFormat.getInstance();
	
	@EventHandler
	public void place(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player == false)
			return;
		
		if (e.getCause()!=DamageCause.ENTITY_ATTACK)
			return;
		
		Double distance = e.getDamager().getLocation().distance(e.getEntity().getLocation());
		
		
		String player1 = e.getEntity().getName();
		
		if (distance < 4.5)
			return;
		
		if (distance < 4.75) {
			plugin.actionAnnounce("§c§oPlayer Watcher §f" + player1 + " is hitting at §c" + format.format(distance) + " §fblocks away at " + plugin.getPlayerShopStorage().getStringFrom(e.getDamager().getLocation()) + ".", "extras.playerwatcher");
			return;
		}
		
		if (distance <= 5) {
			plugin.actionAnnounce("§c§oPlayer Watcher §f" + player1 + " is hitting at §4" + format.format(distance) + " §fblocks away at " + plugin.getPlayerShopStorage().getStringFrom(e.getDamager().getLocation()) + ".", "extras.playerwatcher");
			return;
		}
		
	}
	
	public MobAttack(TownyLegacy instance) {
		Bukkit.getPluginManager().registerEvents(this, this.plugin = instance);	
		
		format.setRoundingMode(RoundingMode.DOWN);
		format.setMaximumFractionDigits(3);
	}
}
