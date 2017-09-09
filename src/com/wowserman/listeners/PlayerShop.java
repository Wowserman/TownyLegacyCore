package com.wowserman.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.wowserman.TownyLegacy;
import com.wowserman.hooks.WorldEditHook.Selection;
import com.wowserman.storage.PlayerShopStorage.ShopData;

public class PlayerShop implements Listener {

	private TownyLegacy plugin;
	
	private String world;
	
	private double x, X, z, Z;
	
	public PlayerShop(TownyLegacy instance) {
		Bukkit.getPluginManager().registerEvents(this, this.plugin = instance);

		x = this.plugin.getConfig().getDouble("Player Shop x", 0.0);
		X = this.plugin.getConfig().getDouble("Player Shop X", 0.0);
		z = this.plugin.getConfig().getDouble("Player Shop z", 0.0);
		Z = this.plugin.getConfig().getDouble("Player Shop Z", 0.0);

		world = this.plugin.getConfig().getString("Player Shop World", "world");
		
		this.updateBorders();
	}
	
	public String getBorders() {
		return "Corner 1: (x: " + x + ", z:" + z + "), Corner 2: (x:" + X + ", z:" + Z + "), World: " + world;
	}
	
	public void setBorders(Selection selection) {
		x = selection.getCorner1().getX();
		X = selection.getCorner2().getX();
		z = selection.getCorner1().getZ();
		Z = selection.getCorner2().getZ();
		
		world = selection.getWorld();
		
		this.updateBorders();
	}
	
	private void updateBorders() {
		
		// Organize by Value
		
		final double x1 = x; final double x2 = X; final double z1 = z; final double z2 = Z;
		
		x = x1 < x2 ? x1 : x2;
		X = x1 > x2 ? x1 : x2;
		z = z1 < z2 ? z1 : z2;
		Z = z1 > z2 ? z1 : z2;
		
		this.plugin.getConfig().set("Player Shop x", x);
		this.plugin.getConfig().set("Player Shop z", z);
		this.plugin.getConfig().set("Player Shop X", X);
		this.plugin.getConfig().set("Player Shop Z", Z);
		
		this.plugin.getConfig().set("Player Shop World", world);
		
		this.plugin.saveConfig();
	}
	
	public boolean isInShop(Player player) {
		return (x <= player.getLocation().getX() && 
			X >= player.getLocation().getX() &&
			z <= player.getLocation().getZ() &&
			Z >= player.getLocation().getZ() &&
			world.equalsIgnoreCase(player.getWorld().getName()));
	}
	
	public boolean isInShop(Location location) {
		return (x <= location.getX() && 
				X >= location.getX() &&
				z <= location.getZ() &&
				Z >= location.getZ() &&
				world.equalsIgnoreCase(location.getWorld().getName()));
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void bucket(PlayerBucketEmptyEvent e) {
		Player player = e.getPlayer();
		
		if (this.isInShop(player) == false && this.isInShop(e.getBlockClicked().getLocation())==false) return;
		
		if (!player.hasPermission("extras.shopbypass"))
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void light(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		if (this.isInShop(player) == false) return;
		
		if (e.getItem()==null)
			return;
		
		if (e.getItem().getType() == Material.FLINT_AND_STEEL || e.getItem().getType() == Material.FIREBALL)
			if (!player.hasPermission("extras.shopbypass"))
				e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void destroy(BlockBreakEvent e) {
		Player player = e.getPlayer();
				
		if (this.isInShop(e.getBlock().getLocation()) == false) return;
		
		if (e.getBlock().getType() == Material.CHEST || e.getBlock().getType() == Material.SIGN || e.getBlock().getType() == Material.SIGN_POST || e.getBlock().getType() == Material.WALL_SIGN) {
			
			if (e.getBlock().getType() != Material.CHEST)
				return;
			
			ShopData data = plugin.getPlayerShopStorage().destroyShop(e.getBlock().getLocation());
			
			if (data==null)
				return;
			
			if (data.getUUIDFromData().equals(player.getUniqueId().toString()))
				player.sendMessage("§aYou have removed one of your ChestShops. You can now have " + (plugin.getPlayerShopStorage().getMax()-plugin.getPlayerShopStorage().getShopCount(player)) + " new ChestShops in Shop.");
			
			plugin.announce("§c§oPlayer Watcher §f" + e.getPlayer().getName() + " destroyed a ChestShop of " + data.getPlayerNameFromData() + "'s in Shop.", "extras.playerwatcher");
			return;
		} else {
			if (!player.hasPermission("extras.shopbypass"))
				e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void place(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		
		if (this.isInShop(player) == false) return;
		
		
		if ((e.getBlockPlaced().getType() == Material.SIGN || e.getBlockPlaced().getType() == Material.SIGN_POST || e.getBlockPlaced().getType() == Material.WALL_SIGN) && (e.getBlock().getRelative(0, -1, 0).getType() == Material.SIGN || e.getBlock().getRelative(0, -1, 0).getType() == Material.SIGN_POST || e.getBlock().getRelative(0, -1, 0).getType() == Material.WALL_SIGN || e.getBlock().getRelative(0, -1, 0).getType() == Material.CHEST)) {
			return; 
		} else if (e.getBlockAgainst().getType()!=Material.CLAY) { 
			if (!player.hasPermission("extras.shopbypass"))
				e.setCancelled(true);
			
			return;
		}
		
		if (e.getBlockPlaced().getType() != Material.CHEST && e.getBlockPlaced().getType() != Material.SIGN && e.getBlockPlaced().getType() != Material.SIGN_POST && e.getBlockPlaced().getType() != Material.WALL_SIGN) {
			if (!player.hasPermission("extras.shopbypass"))
				e.setCancelled(true);
			return;
		}
		
		if (e.getBlockPlaced().getType() == Material.CHEST) {
			final boolean successful = plugin.getPlayerShopStorage().createShop(e.getBlock().getLocation(), player);
			
			if (successful==false) {
				player.sendMessage("§cYou have exceeded the Max ChestShops in Shop (" + plugin.getPlayerShopStorage().getMax() + "), and cannot create any more until you remove some of your ChestShops.");
				e.setCancelled(true);
			} else {
				player.sendMessage("§aYou can create " +(plugin.getPlayerShopStorage().getMax()-plugin.getPlayerShopStorage().getShopCount(player)) + " more ChestShops in Shop.");
				plugin.actionAnnounce("§c§oPlayer Watcher §f" + player.getName() + " created a ChestShop in Shop at " + plugin.getPlayerShopStorage().getStringFrom(e.getBlock().getLocation()), "extras.playerwatcher");
			}
			
		}
	}
	
	@EventHandler
	public void tp(PlayerTeleportEvent e) {
		if (this.isInShop(e.getTo())) {
			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100000, 1));
			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 1));
		}
		
		else if (this.isInShop(e.getFrom())) {
			e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
			e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
		}
	}
	
}
