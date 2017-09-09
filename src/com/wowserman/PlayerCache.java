package com.wowserman;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.inventivetalent.glow.GlowAPI;
import org.inventivetalent.glow.GlowAPI.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.wowserman.storage.VoteStorage.VoteSite;
import com.wowserman.tools.scoreboard.ScoreboardLib;
import com.wowserman.tools.scoreboard.common.EntryBuilder;
import com.wowserman.tools.scoreboard.common.Strings;
import com.wowserman.tools.scoreboard.common.animate.HighlightedString;
import com.wowserman.tools.scoreboard.common.animate.ScrollableString;
import com.wowserman.tools.scoreboard.type.Entry;
import com.wowserman.tools.scoreboard.type.Scoreboard;
import com.wowserman.unlockables.Unlockable;
import com.wowserman.unlockables.UnlockablesManager;

public class PlayerCache {

	private static TownyLegacy plugin;
	
	public static void setPlugin(TownyLegacy instance) {
		PlayerCache.plugin = instance;
	}
	
	private String uuid, town, nation;
	private long firstJoin, lastSeen;
	private int legacyLevel, prestige, votes, minutesLeft;
	private ChatColor chatColor;
	private Color glowColor;
	private Date streak;
	private Hashtable<Unlockable, Integer> unlockables = new Hashtable<Unlockable, Integer>();
	private Hashtable<VoteSite, String> lastVotes = new Hashtable<VoteSite, String>();
	private Scoreboard scoreboard = null;
	
	public void setTown(String town) {
		this.town = town;
	}
	
	public void setNation(String nation) {
		this.nation = nation;
	}
	
	public String getTown() {
		return town;
	}
	
	public String getNation() {
		return nation;
	}
	
	public Scoreboard getScoreboard() {
		return scoreboard;
	}
	
	public boolean hasScoreboard() {
		return scoreboard!=null;
	}
	
	public void setScoreboard(Scoreboard value) {
		this.scoreboard = value;
	}
	
	public int getUnlockableLevel(Unlockable unlockable) {
		return unlockables.getOrDefault(unlockable, 0);
	}
	
	public void disableScoreboard() {
		if (this.hasScoreboard() && this.getScoreboard().isActivated())
			scoreboard.deactivate();
	}
	
	public void enableScoreboard() {
		if (scoreboard!=null) 
			scoreboard.deactivate();
		scoreboard = ScoreboardLib.createScoreboard(this.getBase())
		        .setHandler(new com.wowserman.tools.scoreboard.type.ScoreboardHandler() {

		            private final HighlightedString title = new HighlightedString("Towny Legacy", "&d", "&f");
		            
		            @Override
		            public String getTitle(Player player) {
		            	
		                return title.next();
		            }

		            
		            @Override
		            public List<Entry> getEntries(Player player) {
		            	
		            	/*
		            	 * Towny Legacy
		            	 * 
		            	 *  Wowserman
		            	 *  Level 5 IIV (+10%)
		            	 *  Geneva, Switzerland
		            	 *  
		            	 *  $250,000
		            	 * 
		            	 */
		            	 
		            	
		            	return new EntryBuilder()
		            			.blank()
		            			.next("  " + player.getName())
		            			.next("  &dLevel " + getLegacyLevel() + (getPrestige() > 0 ? " &5" + getPrestigeRomanNumeral():"") + (getJobsBooster() != 1 ? " &a(+" + getJobsBoosterPercentage() + "%)":""))
		            			.blank()
		            			.next("  " + plugin.getTownyHook().getFormatedTownAndNation(getBase()))
		            			.next("  §a" + plugin.getVaultHook().getFormattedPlayerTotal(getBase()))
		                        .build();
		            }

		       })
		       .setUpdateInterval(5l);
		    scoreboard.activate();
	}
	
	private static void convertVoteCache(String uuid) {
		PlayerCache cache = PlayerCache.get(uuid);
		
		ConfigurationSection section = plugin.getVoteStorage().getConfig().getConfigurationSection("players").getConfigurationSection(uuid);
		
		cache.votes = section.getInt("players." + cache.getUUID() + ".votes", 0);
		
		List<String> list = section.getStringList("site-data");
		list = list == null ? new ArrayList<String>():list;
		for (String line:list) {
			if (VoteSite.get(line.split(",")[0])!=null)
				cache.setLastVotes(VoteSite.get(line.split(",")[0]), line.split(",")[1]);
		}

		try {
			cache.streak = TownyLegacy.DATE_FORMAT.parse(section.getString("streak"));
		} catch (NullPointerException | java.text.ParseException e) {
			cache.streak = new Date();
		}
		
		cache.minutesLeft = section.getInt("minutes", 0);
		
		plugin.getLogger().info("Converted Vote Cache to Player Cache " + cache.toString());
		
		cache.close();

	}
	
	public static void convertAllVoteData() {
		if (plugin.getVoteStorage().getConfig().getConfigurationSection("players")==null)
			return;
		for (String uuid:plugin.getVoteStorage().getConfig().getConfigurationSection("players").getKeys(false)) 
			convertVoteCache(uuid);
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject getJSON() {
		JSONObject json = new JSONObject();
		
		JSONArray unlockablesJSON = new JSONArray();
		
		JSONArray sitesJSON = new JSONArray();
		
		json.put("votes", votes);
		
		json.put("legacyLevel", legacyLevel);
		
		json.put("prestige", prestige);
		
		json.put("minutesLeft", minutesLeft);
		
		json.put("firstJoin", firstJoin < 0 ? System.currentTimeMillis():firstJoin);
		
		json.put("lastSeen", lastSeen < 0 ? System.currentTimeMillis():lastSeen);
		
		json.put("glowColor", glowColor.toString());
		
		json.put("chatColor", chatColor.getChar());
		
		if (streak!=null)
			json.put("streak", streak);
		
		for (Unlockable key:unlockables.keySet()) { 
			JSONObject unlockableJSON = new JSONObject();
			
			unlockableJSON.put("type", key.toString());
			unlockableJSON.put("level", unlockables.get(key));
			
			unlockablesJSON.add(unlockableJSON);
		}
		
		json.put("unlockables", unlockablesJSON);
		
		for (VoteSite key:lastVotes.keySet()) {
			JSONObject siteJSON = new JSONObject();
			
			siteJSON.put("site", key.toString());
			siteJSON.put("last-vote", lastVotes.get(key));
			
			sitesJSON.add(siteJSON);
		}		
		
		json.put("votes-sites", sitesJSON);
		
		return json;
	}
	
	public void save() {
		plugin.getPlayerCacheStorage().getConfig().set(uuid, this.getJSON().toJSONString());
		plugin.getPlayerCacheStorage().saveFile();
	}
	
	public boolean isOnline() {
		return Bukkit.getPlayer(UUID.fromString(uuid))!=null;
	}
	
	public void open() {
		if (this.isOnline()) {
			this.enableScoreboard();
			this.updateGlow();
		}
	}
	
	public void close() {
		this.lastSeen = System.currentTimeMillis();
		this.save();
		if (this.isOnline()) {
			this.disableScoreboard();
		}
		PlayerCache.caches.remove(this);
	}
	
	public static void closeAll() {
		for (PlayerCache cache:new ArrayList<PlayerCache>(caches))
			cache.close();
		caches.clear();
		
	}
	
	private static PlayerCache read(String uuid) {
		
		PlayerCache cache = new PlayerCache(uuid);
		
		final String data = plugin.getPlayerCacheStorage().getConfig().getString(uuid);
		
		if (data != null) {
						
			try {
				JSONObject json = (JSONObject) new JSONParser().parse(data);
				
				cache.votes = ((Long) json.get("votes")).intValue();
				
				cache.legacyLevel = ((Long) json.get("legacyLevel")).intValue();
								
				cache.prestige = ((Long) json.get("prestige")).intValue();
								
				cache.minutesLeft = ((Long) json.get("minutesLeft")).intValue();
				
				cache.chatColor = ChatColor.getByChar((String) json.get("chatColor"));
				
				
				cache.glowColor = Color.valueOf((String) json.get("glowColor"));

				if (json.get("streak") != null)
					cache.streak = TownyLegacy.DATE_FORMAT.parse((String) json.get("streak"));
				
				if (json.get("unlockables") instanceof JSONArray) {
					
					JSONArray unlockables = (JSONArray) json.get("unlockables");
					
					for (int i = 0; i < unlockables.size(); i++) {
						JSONObject unlockable = (JSONObject) unlockables.get(i);
						
						Unlockable type = UnlockablesManager.getUnlockable((String) unlockable.get("type"));
						Integer level = (Integer) unlockable.get("level");
						
						cache.setUnlocked(type, level);
					}
				}
				
				if (json.get("vote-sites") instanceof JSONArray) {
					
					JSONArray sites = (JSONArray) json.get("vote-sites");
					
					for (int i = 0; i < sites.size(); i++) {
						JSONObject site = (JSONObject) sites.get(i);
						
						VoteSite syte = VoteSite.get((String) site.get("site"));
						String lastVote = (String) site.get("votes");
						
						cache.setLastVotes(syte, lastVote);
					}
				}
				
				
			} catch (ParseException | java.text.ParseException e) {
				plugin.getLogger().warning("Problems loading " + uuid + "'s data.");
			}
			
		} else {
			cache.firstJoin = System.currentTimeMillis();
			cache.lastSeen = cache.firstJoin;
		}
				
		return cache;
	}
	
	private void setLastVotes(VoteSite site, String lastVote) {
		if (site==null || lastVote==null)
			return;
		
		lastVotes.put(site, lastVote);
	}
	
	public boolean isExpired(VoteSite site) {
		return TownyLegacy.isBeforeYesterday(lastVotes.get(site));
	}
	
	public PlayerCache(String uuid) {
		this.uuid = uuid;
		this.legacyLevel = 1;
		this.prestige = 0;
		this.votes = 0;
		this.minutesLeft = 0;
		this.lastSeen = -1;
		this.firstJoin = -1;
		this.chatColor = ChatColor.WHITE;
		this.glowColor = Color.NONE;
		
		caches.add(this);
	}
	
	private static List<PlayerCache> caches = new ArrayList<PlayerCache>();
	
	public static PlayerCache get(UUID uuid) {
		return PlayerCache.get(uuid.toString());
	}
	
	public static PlayerCache get(Player player) {
		return PlayerCache.get(player.getUniqueId().toString());
	}
	
	public static PlayerCache get(String uuid) {
		for (PlayerCache cache:PlayerCache.caches)
			if (cache.getUUID().equals(uuid))
				return cache;
		return PlayerCache.read(uuid);
	}
	
	public boolean isUnlocked(Unlockable unlockable) {
		return unlockables.getOrDefault(unlockable, -1) <= 0;
	}
	
	public void setUnlocked(Unlockable unlockable, int value) {
		if (value <= 0)
			unlockables.remove(unlockable);
		else unlockables.put(unlockable, value);
	}
	
	public String getUUID() {
		return uuid;
	}
	
	public int getVotes() {
		return votes;
	}
	
	public int getLegacyLevel() {
		return legacyLevel;
	}
	
	public Player getBase() {
		return Bukkit.getPlayer(UUID.fromString(uuid));
	}
	
	/**
	 * @return Booster + 1
	 */
	public double getJobsBooster() {
		return 1 + legacyLevel * 0.01;
	}
	
	private double getJobsBoosterPercentage() {
		return legacyLevel;
	}
	
	public boolean canRankUp() {
		return legacyLevel < 100;
	}
	
	public double getRankUpCost() {
		return (legacyLevel * 1000) * (prestige == 0 ? 1: prestige / 10 + 1);
	}

	public int getPrestige() {
		return prestige;
	}
	
	public String getPrestigeRomanNumeral() {
		if (prestige==1)
			return "I";
		if (prestige==2)
			return "II";
		if (prestige==3)
			return "III";
		if (prestige==4)
			return "IV";
		if (prestige==5)
			return "V";
		if (prestige==6)
			return "VI";
		if (prestige==7)
			return "VII";
		if (prestige==8)
			return "VIII";
		if (prestige==9)
			return "IX";
		if (prestige==10)
			return "X";
		
		return "";
	}
	
	public int getMinutesLeft() {
		return minutesLeft;
	}
	
	public void setMinutesLeft(int minutes) {
		this.minutesLeft = minutes;
	}
	
	public ChatColor getChatColor() {
		return chatColor;
	}
	
	public Color getGlowColor() {
		return glowColor;
	}
	
	public Integer getStreak() {
		return streak == null ? 0:(int) TimeUnit.DAYS.convert(new Date().getTime() - streak.getTime(), TimeUnit.MILLISECONDS);
	}
	
	public void updateGlow() {
		if (this.getBase()==null) 
			return;
		
		if (glowColor != Color.NONE) 
			GlowAPI.setGlowing(this.getBase(), glowColor, Bukkit.getOnlinePlayers());
		else GlowAPI.setGlowing(this.getBase(), false, Bukkit.getOnlinePlayers());
	}
	
	public void setChatColor(ChatColor color) {
		this.chatColor = color;
	}
	
	public void setGlow(Color color) {
		this.glowColor = color;
		this.updateGlow();
	}
	
	/**
	 * Call when a Player Votes for a Site.
	 * 
	 * @param site
	 */
	public void updateVote(VoteSite site) {
		streak = (streak==null || DateUtils.addDays(new Date(), 1).before(new Date())) ? new Date():streak;
		
		lastVotes.put(site, TownyLegacy.DATE_FORMAT.format(new Date()));
		
		votes++;
		
		minutesLeft = + minutesLeft + plugin.getVoteStorage().getFlyTime();
		
		String command = "";
		
		Player player = Bukkit.getPlayer(UUID.fromString(this.getUUID()));
		
		for (String c:plugin.getVoteStorage().getCommands()) {
			command = c;
			command = command.contains("&") ? command.replaceAll("&", "§"):command;
			command = command.contains("<player>") ? command.replaceAll("<player>", Bukkit.getPlayer(UUID.fromString(this.getUUID())).getName()):command;
			command = command.contains("<votes>") ? command.replaceAll("<votes>", "" + this.getVotes()):command;
			command = command.contains("<service>") ? command.replaceAll("<service>",site.getName()):command;
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
		}

		String announcement = plugin.getVoteStorage().getAnnouncement();
		announcement = announcement.contains("&") ? announcement.replaceAll("&", "§"):announcement;
		announcement = announcement.contains("<player>") ? announcement.replaceAll("<player>", Bukkit.getPlayer(UUID.fromString(this.getUUID())).getName()):announcement;
		announcement = announcement.contains("<votes>") ? announcement.replaceAll("<votes>", "" + this.getVotes()):announcement;
		announcement = announcement.contains("<service>") ? announcement.replaceAll("<service>",site.getName()):announcement;
		
		plugin.announce(announcement);
		
		String message = plugin.getVoteStorage().getMessage();
		message = message.contains("&") ? message.replaceAll("&", "§"):message;
		message = message.contains("<player>") ? message.replaceAll("<player>", Bukkit.getPlayer(UUID.fromString(this.getUUID())).getName()):message;
		message = message.contains("<votes>") ? message.replaceAll("<votes>", "" + this.getVotes()):message;
		message = message.contains("<service>") ? message.replaceAll("<service>",site.getName()):message;
		message = message.contains("<minutes>") ? message.replaceAll("<minutes>", this.minutesLeft + ""):message;
				
		player.sendMessage(message);
		
		plugin.getLogger().info(this.getUUID() + " voted at " + site.getName() + ".");
		plugin.getLogger().info(this.getUUID() + " last voted on " + site.getName() + " at " + lastVotes.get(site));
		
		plugin.getVoteStorage().updateVotePartyCount();
	}
	
	@Override
	public String toString() {
		return uuid + " | First Join " + this.firstJoin + " | Last Seen " + this.lastSeen + " | Legacy Level " + this.legacyLevel + " | Prestige " + this.prestige + " | Votes " + this.votes;
	}
}
