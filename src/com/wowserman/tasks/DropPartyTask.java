package com.wowserman.tasks;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.wowserman.TownyLegacy;

public class DropPartyTask extends BukkitRunnable {
	
	private TownyLegacy plugin;
	
	private String starter;
	
	private Location center;
	
	private ItemStack[] rewards;
	
	private boolean console = true;
	
	int count = 0;
	
	private Player player;
	
	public DropPartyTask(TownyLegacy instance, Player player) {
		this.plugin = instance;
		this.starter = player.getName();
		this.center = player.getLocation();
		this.rewards = player.getInventory().getContents();
		this.console = false;
	}
	
	public DropPartyTask(TownyLegacy instance, String starter, Location center) {
		this.plugin = instance;
		this.starter = starter;
		this.center = center;
		this.rewards = plugin.getVoteStorage().getRewards();
		this.console = true;
	}
	
	public String getStarter() {
		return starter;
	}
	
	public Location getRandomLocation() {
		double radius = 10;
		double diameter = radius*2;
		double x = center.getX() + (ThreadLocalRandom.current().nextDouble()*diameter - radius);
		double z = center.getZ() + (ThreadLocalRandom.current().nextDouble()*diameter - radius);
	    
	    return new Location(center.getWorld(), x, center.getY(), z);
	}
	
	public Particle getRandomParticle() {
		int random = ThreadLocalRandom.current().nextInt(10);
		
		if (random==0)
			return Particle.HEART;
		if (random==1)
			return Particle.NOTE;
		if (random==2)
			return Particle.DRIP_LAVA;
		if (random==3)
			return Particle.DRIP_WATER;
		if (random==4)
			return Particle.PORTAL;
		if (random==5)
			return Particle.REDSTONE;
		if (random==6)
			return Particle.SLIME;
		if (random==7)
			return Particle.VILLAGER_HAPPY;
		if (random==8)
			return Particle.VILLAGER_ANGRY;
		if (random==9)
			return Particle.SNOWBALL;
		
		return Particle.ENCHANTMENT_TABLE;
	}
	
	public void spawnFirework(Location location) {
		
		Builder fb = FireworkEffect.builder();
		FireworkEffect f = null;
		fb.withColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255)));
		fb.with(Type.BURST);
		if (ThreadLocalRandom.current().nextBoolean()==true) fb.withFlicker();
		f = fb.build();
		
		Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
 		FireworkMeta fwm = firework.getFireworkMeta();
 		fwm.clearEffects();
 		fwm.addEffect(f);
 		fwm.setPower(ThreadLocalRandom.current().nextInt(3) + 2);
 		firework.setFireworkMeta(fwm);
 		
	}
	
	@Override
	public void run() {

		count = count + 1;
		
		if (count==1) {
			plugin.announce("§b" + starter + " has Started a §5Drop Party§b!");
			if (!console)
				player.sendMessage("§7§oThe Drop Party you started will end when you have no more Items.");
		}
		
		if (!console && player!=null && player.isOnline()==false) {
			plugin.getDropPartyCommand().remove(player);
			this.cancel();
			return;
		}
		
		if (ThreadLocalRandom.current().nextBoolean()) {
			ItemStack item = null;
			
			if (console)
				item = plugin.removeItem(rewards);
			else if (player==null)
				item = plugin.removeItem(player);
			
			if (item==null || item.getType()==Material.AIR) {
				plugin.announce("§c" + starter + "'s Drop Party has Ended.");
				if (!console)
					plugin.getDropPartyCommand().remove(player);
				this.cancel();
				return;
			}
			Location location = this.getRandomLocation();
			center.getWorld().dropItem(location, item);
			this.spawnFirework(location);
			
		} else {
			Location location = this.getRandomLocation();
			center.getWorld().spawnParticle(this.getRandomParticle(), location, 20, 3, 3, 3);
			center.getWorld().spawnEntity(location, EntityType.THROWN_EXP_BOTTLE);
		}
	}

}
