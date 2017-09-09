package com.wowserman.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class PlayerMove implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void move(PlayerMoveEvent e) {
		if (e.getPlayer().isFlying()) 
			return;

		if (e.getTo().getBlock().getRelative(0, -1, 0).getTypeId()==36 || e.getTo().getBlock().getTypeId()==36) {
			Vector v = e.getPlayer().getLocation().getDirection();
			v.setY(0.5);
			v.multiply(1.1);
			e.getPlayer().setVelocity(v);
		}
			
//			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 10, 1, true, false, Color.WHITE));
	}
}
