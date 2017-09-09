package com.wowserman.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;

import com.wowserman.TownyLegacy;

public class PvPArena implements Listener {

	private TownyLegacy plugin;
	
	private String world;
	
	@EventHandler
	public void click(PlayerInteractEvent e) {
		if (e.getAction()!=Action.LEFT_CLICK_AIR)
			return;
		
		if (e.getPlayer().getWorld().getName().equalsIgnoreCase(world)==false)
			return;
		
		if (e.getItem()==null)
			return;

		if (e.getItem().getType()!=Material.TNT)
			return;
		
		ItemStack item = e.getItem();
		if (item.getAmount() > 1) item.setAmount(item.getAmount() - 1);
		else item = new ItemStack(Material.AIR);
		e.getPlayer().getInventory().setItemInMainHand(item);
		
		Entity tnt = e.getPlayer().getWorld().spawnEntity(e.getPlayer().getEyeLocation(), EntityType.PRIMED_TNT);
		tnt.setVelocity(e.getPlayer().getLocation().getDirection().multiply(2.5));
	}
	
	@EventHandler
	public void shoot(EntityShootBowEvent e) {
		if (e.getEntity() instanceof Player == false) {
			return;
		}
		
		if (e.getEntity().getWorld().getName().equalsIgnoreCase(world)==false) {
			return;
		}
		
		if (e.getEntity().getEquipment()==null) {
			return;
		}
		
		if (e.getEntity().getEquipment().getHelmet()==null ||
			e.getEntity().getEquipment().getChestplate()==null ||
			e.getEntity().getEquipment().getLeggings()==null || 
			e.getEntity().getEquipment().getBoots()==null)
			return;
		
		if (e.getEntity().getEquipment().getHelmet().getType()!=Material.LEATHER_HELMET &&
			e.getEntity().getEquipment().getChestplate().getType()!=Material.LEATHER_CHESTPLATE &&
			e.getEntity().getEquipment().getLeggings().getType()!=Material.LEATHER_LEGGINGS &&
			e.getEntity().getEquipment().getBoots().getType()!=Material.LEATHER_BOOTS) {		
			return;
		}
		
		e.getProjectile().setVelocity(e.getEntity().getLocation().getDirection().multiply(4));
	}
	
	
	@EventHandler
	public void tp(PlayerTeleportEvent e) {
		if (e.getTo().getWorld().getName().equalsIgnoreCase(world)) {
			e.getPlayer().setFlying(false);
		}
	}
	
	@EventHandler
	public void toggleFly(PlayerToggleFlightEvent e) {
		if (e.isFlying()==true && e.getPlayer().getWorld().getName().equalsIgnoreCase(world)) {
			e.setCancelled(true);
		}
	}
	
	public PvPArena(TownyLegacy instance) {
		Bukkit.getPluginManager().registerEvents(this, this.plugin=instance);
		this.world = this.plugin.getConfig().getString("PvP World", "pvp");
	}
	
	public void save() {
		this.plugin.getConfig().set("PvP World", world);
		this.plugin.saveConfig();
	}
}
