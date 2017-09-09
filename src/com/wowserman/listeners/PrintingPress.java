package com.wowserman.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import com.wowserman.TownyLegacy;

public class PrintingPress implements Listener {

	private final String PRINTINGPRESS = "#_PRINTING_PRESS_#";
	
	private TownyLegacy plugin;
	
	public PrintingPress(TownyLegacy instance) {
		Bukkit.getPluginManager().registerEvents(this, this.plugin = instance);
	}
	
	public boolean isPrintingPressArmorStand(Entity entity) {
		if (entity instanceof ArmorStand == false)
			return false;
		
		if (entity.getCustomName()==null || entity.getCustomName().equals(PRINTINGPRESS)==false)
			return false;
		
		return true;
	}
	
	
	public ArmorStand getStand(Location location, boolean spawn) {
		for (Entity entity:location.getWorld().getNearbyEntities(location, 1, 1, 1)) {
			if (isPrintingPressArmorStand(entity)==false)
				continue;
			else return (ArmorStand) entity;
		}
		
		// summon armor_stand ~0.0 ~-4.61477 ~-0.71875 {Pose:{Head:[90f,0f,0f]},DisabledSlots:4096,Invisible:1,NoGravity:1,ArmorItems:[{},{},{},{Count:1,id:book}]}
		
		if (spawn==false)
			return null;
		
		ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location.add(0.5, -0.71477, -0.3), EntityType.ARMOR_STAND);
		stand.setHeadPose(new EulerAngle(Math.toRadians(90), 0f, 0f));
		stand.setGravity(false);
		stand.setVisible(false);
		stand.setCustomName(PRINTINGPRESS);
		stand.setCustomNameVisible(false);
		return stand;
	}
	
	public ItemStack getBook(Location location) {
		for (Entity entity:location.getWorld().getNearbyEntities(location, 1, 1, 1)) {
			
			if (entity.getCustomName().equals(PRINTINGPRESS)==false)
				continue;
			
			if (entity instanceof ArmorStand == false)
				continue;
			
			ArmorStand stand = (ArmorStand) entity;
			
			if (stand.getHelmet()==null)
				continue;
			
			return stand.getHelmet();
		}
		
		return new ItemStack(Material.AIR, 1);
	}
	
	public void kill(Location location) {
		ArmorStand stand = this.getStand(location, false);
		
		if (stand==null || stand.isDead())
			return;
		
		this.drop(stand, location);
		
		stand.remove();
	}
	
	public void drop(ArmorStand stand, Location location) {
		if (stand.getHelmet()!=null && stand.getHelmet().getType()!=Material.AIR)
			stand.getWorld().dropItem(location.add(0, 1.5, 0), stand.getHelmet().clone());
	}
	
	/*
	 * 1. Check if Block we're clicking is a iron block
	 * 2. Check if there is a stand
	 *   -> If not Spawn one with the book in the player's hand
	 *   -> If there is continue
	 * 3. Check if it has a book
	 *   -> IF it does, take it.
	 *   -> If it doesn't, give it one.
	 * 4. 
	 */
	
	@EventHandler
	public void click(PlayerInteractEvent e) {
		if (e.getAction()!=Action.RIGHT_CLICK_BLOCK)
			return;
		
		if (e.getClickedBlock().getType()!=Material.IRON_BLOCK)
			return;
		
		if (e.getPlayer().hasPermission("extas.printingpress")==false)
			return;
		
		if (e.getItem()==null || e.getItem().getType() != Material.WRITTEN_BOOK) {
			this.kill(e.getClickedBlock().getLocation());
		} else {
			ArmorStand stand = this.getStand(e.getClickedBlock().getLocation(), true);
			
			this.drop(stand, e.getClickedBlock().getLocation());
			
			ItemStack book = e.getItem();
			book.setAmount(1);
			
			stand.setHelmet(book);
			
			ItemStack item = e.getItem();
			if (item.getAmount() > 1) item.setAmount(item.getAmount() - 1);
			else item = new ItemStack(Material.AIR);
			e.getPlayer().getInventory().setItemInMainHand(item);
			
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void click(BlockBreakEvent e) {
		
		if (e.getBlock().getType()!=Material.IRON_BLOCK)
			return;
		
		this.kill(e.getBlock().getLocation());
	}
	
	@EventHandler
	public void retract(BlockPistonRetractEvent e) {
		
		if (e.isSticky()) {
			return;
		}
		
		if (e.getDirection()!=BlockFace.DOWN) {
			return;
		}
		
		if (e.getBlock().getRelative(0, -2, 0).getType()!=Material.IRON_BLOCK) {
			return;
		}
		
		ArmorStand stand = this.getStand(e.getBlock().getLocation().subtract(0, 2, 0), false);
		
		if (stand==null) {
			return;
		}
		
		Player player = null;
		
		for (Entity entity:e.getBlock().getWorld().getNearbyEntities(e.getBlock().getLocation(), 5, 5, 5)) {
			if (entity instanceof Player == false)
				continue;
			
			if (player==null) {
				player = (Player) entity;
				continue;
			}
			
			if (player.getLocation().distanceSquared(e.getBlock().getLocation()) > entity.getLocation().distanceSquared(e.getBlock().getLocation()))
				entity = (Player) entity;
		}
		
		if (player==null) {
			return;
		}
		
		if (player.hasPermission("extas.printingpress")==false)
			return;
		
		if (this.plugin.removeItem(player, Material.BOOK)==false) {
			player.sendMessage("§cYou're missing a empty Book!");
			return;
		}
		
		if (this.plugin.removeItem(player, Material.INK_SACK)==false) {
			player.sendMessage("§cYou're missing a empty Ink Sack!");
			return;
		}
		
		this.drop(stand, e.getBlock().getLocation().subtract(0, -2, 0));
		
		player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 3f, 3f);
		
	}
	
}
