package com.wowserman.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.wowserman.TownyLegacy;
import com.wowserman.tasks.DropPartyTask;

public class VoteStorage extends StorageFile {
	
	// TODO: Clear stuff
	public void close() {
		
		this.getConfig().set("Vote Party Count", votePartyCount);
		this.getConfig().set("Reward Commands", commands);
		this.getConfig().set("Announcement", announcement);
		this.getConfig().set("Player Message", message);
		this.getConfig().set("Fly Time", flyTime);
		this.getConfig().set("Player Fly Expires", expiredMessage);
		this.getConfig().set("Vote Party Location", this.toString(party));
		this.getConfig().set("Player still has Time Message", "&aYou have &d<minutes> &aleft of Fly!");

		
		for (int i = 0; i < this.rewards.size(); i++) {
			this.getConfig().set("Reward Items." + (i + 1), rewards.get(i));
		}
		
		this.saveFile();
		
	}
	
	public static class VoteSite {
		
		private static List<VoteSite> sites = new ArrayList<VoteSite>();
		
		public static void clear() {
			sites.clear();
		}
		
		public static List<VoteSite> getSites() {
			return sites;
		}
		
		public static void add(VoteSite site) {
			if (VoteSite.get(site.getName()) != null)
				return;
			sites.add(site);
		}
		
		public static VoteSite get(String name) {
			for (VoteSite site:sites)
				if (site.getName().equals(name))
					return site;
			return null;
		}
		
		private String name;
		private String url;
		
		public String getName() {
			return name;
		}
		
		public String getUrl() {
			return url;
		}
		
		public VoteSite(String name, String url) {
			this.name = name;
			this.url = url;
			sites.add(this);
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public void updateVotePartyCount() {
		votePartyCount = votePartyCount + 1;
		
		if (votePartyCount>=50) {
			votePartyCount = 0;
			
			new BukkitRunnable() {
				int timer = 15;
				
				@Override
				public void run() {
					if (timer < 1) {
						votePartyCount = 0;
						VoteStorage.this.getPlugin().fullTitleAnnounce("§bVote Party", "§dStarted at /warp DropParty");
						new DropPartyTask(VoteStorage.this.getPlugin(), "Server", VoteStorage.this.getPlugin().getVoteStorage().getPartyLocation()).runTaskTimer(VoteStorage.this.getPlugin(), 0, 4);
						this.cancel();
						return;
					}
					
					VoteStorage.this.getPlugin().fullTitleAnnounce("§bVote Party in " + timer + "s", "§dat /warp DropParty");
					timer--;
				}
			}.runTaskTimer(this.getPlugin(), 0, 20);
			
		} else {
			this.getPlugin().fullTitleAnnounce("§b" + (50-votePartyCount) + " More Votes", "§funtil we start a §dVote Party§f");
		}
	}
	
	// TODO: VOTE PARTY
	private int votePartyCount = 0;
	
	private int flyTime = 30;
	
	public int getFlyTime() {
		return flyTime;	
	}
	
	private List<String> commands;
	private String announcement;
	private String message;
	private String expiredMessage;
	private String stillHasTimeMessage;
	
	private Location party;
	
	private String toString(Location location) {
		return location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() + "," + location.getWorld().getName();
	}
	
	private Location fromString(String string) {
		return new Location(Bukkit.getWorld(string.split(",")[3]), Integer.parseInt(string.split(",")[0]), Integer.parseInt(string.split(",")[1]), Integer.parseInt(string.split(",")[2]));
	}
	
	public static final String REWARDS_TITLE = "Vote Party Rewards:";
	
	private List<ItemStack> rewards = new ArrayList<ItemStack>();
	
	public ItemStack[] getRewards() {
		return rewards.toArray(new ItemStack[rewards.size()]);
	}
	
	public Inventory getRewardsMenu() {
		Inventory inventory = Bukkit.createInventory(null, 54, REWARDS_TITLE);
		inventory.setContents(rewards.toArray(new ItemStack[rewards.size()]));
		return inventory;
	}
	
	public void setRewards(Inventory menu) {
		rewards = Arrays.asList(menu.getContents());
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getAnnouncement() {
		return announcement;
	}
	
	public List<String> getCommands() {
		return commands;
	}
	
	public Location getPartyLocation() {
		return party;
	}
	
	public int getVotePartyCount() {
		return votePartyCount;
	}
	
	public void setPartyLocation(Location value) {
		this.party = value;
	}
	
	// TODO: do stuff
	public String getExpiredMessage() {
		return expiredMessage;
	}
	
	public String getPlayerStillHasTimeMessage() {
		return stillHasTimeMessage;
	}
	
	public VoteStorage(TownyLegacy instance) {
		super(instance);
		
		this.votePartyCount = this.getConfig().getInt("Vote Party Count", 0);
		this.flyTime = this.getConfig().getInt("Fly Time", 30);
		this.commands = this.getConfig().getStringList("Reward Commands")!=null ? this.getConfig().getStringList("Reward Commands") : new ArrayList<String>(Arrays.asList("eco give <player> 250", "crate key <player> Voting 1"));
		this.announcement = this.getConfig().getString("Announcement", "§3<player> §bvoted at §3<service> §band received 1x Vote Key + $250.0!");
		this.message = this.getConfig().getString("Player Message", "§bYou currently have <votes> Votes!");
		this.expiredMessage = this.getConfig().getString("Player Fly Expires", "&cYour Fly has Expired! Vote for More Time /vote!");
		this.party = this.fromString(this.getConfig().getString("Vote Party Location", "0,0,0,world"));
		this.stillHasTimeMessage = this.getConfig().getString("Player still has Time Message", "&bYou have &d<minutes> minutes &bleft of Fly!");
		
		VoteSite.clear();
		
		for (String string:this.getConfig().getStringList("Vote Sites")) {
			VoteSite.add(new VoteSite(string.split(";")[0], string.split(";")[1]));
		}
		
		for (VoteSite site:VoteSite.getSites())
			this.getPlugin().getLogger().info("Registered Vote Site: " + site.getName() + ".");
		
		if (this.getConfig().get("Reward Items")!=null)
			for (String itemPath:this.getConfig().getConfigurationSection("Reward Items").getKeys(false)) {
				rewards.add(this.getConfig().getItemStack("Reward Items." + itemPath));
			}
		
	}

	
	@Override
	public String getName() {
		return "vote-data";
	}
}
