package com.wowserman;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;

import com.wowserman.commands.AnnouncerCommand;
import com.wowserman.commands.ChatColorCommand;
import com.wowserman.commands.DonatorChatCommand;
import com.wowserman.commands.DropPartyCommand;
import com.wowserman.commands.GlobalCommand;
import com.wowserman.commands.GlowCommand;
import com.wowserman.commands.InfoCommand;
import com.wowserman.commands.JobsBoosterCommand;
import com.wowserman.commands.MOTDCommand;
import com.wowserman.commands.PlayerShopCommand;
import com.wowserman.commands.PrintCommand;
import com.wowserman.commands.ReclaimCommand;
import com.wowserman.commands.ReferalCommand;
import com.wowserman.commands.RenameCommand;
import com.wowserman.commands.StaffChatCommand;
import com.wowserman.commands.StoreCommand;
import com.wowserman.commands.VoteCommand;
import com.wowserman.hooks.GroupManagerHook;
import com.wowserman.hooks.JobsHook;
import com.wowserman.hooks.TownyHook;
import com.wowserman.hooks.VaultHook;
import com.wowserman.hooks.WorldEditHook;
import com.wowserman.listeners.BlockBreak;
import com.wowserman.listeners.BlockPlace;
import com.wowserman.listeners.ColorChat;
import com.wowserman.listeners.DonatorChat;
import com.wowserman.listeners.InventoryClick;
import com.wowserman.listeners.JobsPayment;
import com.wowserman.listeners.MOTD;
import com.wowserman.listeners.MobAttack;
import com.wowserman.listeners.PlayerJoin;
import com.wowserman.listeners.PlayerMove;
import com.wowserman.listeners.PlayerQuit;
import com.wowserman.listeners.PlayerShop;
import com.wowserman.listeners.PrintingPress;
import com.wowserman.listeners.PvPArena;
import com.wowserman.listeners.StaffChat;
import com.wowserman.listeners.VoteWatcher;
import com.wowserman.storage.ChestStorage;
import com.wowserman.storage.PlayerCacheStorage;
import com.wowserman.storage.PlayerShopStorage;
import com.wowserman.storage.VoteStorage;
import com.wowserman.tasks.AnnouncerTask;
import com.wowserman.tasks.ClearExpiredChestsTask;
import com.wowserman.tasks.FlyRewardTask;
import com.wowserman.tasks.JobsBoosterTimeoutTask;
import com.wowserman.tasks.VaultServerTotalTask;
import com.wowserman.tools.ActionBar;
import com.wowserman.tools.BukkitTools;
import com.wowserman.tools.TextComponentUtil;
import com.wowserman.tools.TownyTools;
import com.wowserman.tools.scoreboard.ScoreboardLib;
import com.wowserman.tools.scoreboard.type.SimpleScoreboard;

import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;

public class TownyLegacy extends JavaPlugin {
		
	
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy @ h:mm a");
	
	public static boolean isBeforeYesterday(String date) {
		try {
			return (TownyLegacy.DATE_FORMAT.parse(date).before(DateUtils.addDays(new Date(), -1)));
		} catch (ParseException | NullPointerException e) {
			return true;	
		}
	}
	
	// Command Executors
	private JobsBoosterCommand jobsBoosterExecutor;
	private PrintCommand printExecutor;
	private RenameCommand renameExecutor;
	private GlowCommand glowExecutor;
	private StoreCommand storeExecutor;
	private ReclaimCommand reclaimExecutor;
	private DonatorChatCommand donatorChatExecutor;
	private ChatColorCommand chatColorExecutor;
	private MOTDCommand motdExecutor;
	private PlayerShopCommand playerShopExecutor;
	private AnnouncerCommand announcerExecutor;
	private DropPartyCommand dropPartyExecutor;
	private VoteCommand voteExecutor;
	private ReferalCommand referalExecutor;
	private InfoCommand infoExecutor;
	private StaffChatCommand staffChatExecutor;
	private GlobalCommand globalExecutor;
	
	public JobsBoosterCommand getJobsBoosterExecutor() {
		return jobsBoosterExecutor;
	}
	
	public PrintCommand getPrintExecutor() {
		return printExecutor;
	}
	
	public RenameCommand getRenameExecutor() {
		return renameExecutor;
	}
	
	public GlowCommand getGlowExecutor() {
		return glowExecutor;
	}
	
	public StoreCommand getStoreCommand() {
		return storeExecutor;
	}
	
	public ReclaimCommand getReclaimCommand() {
		return reclaimExecutor;
	}
	
	public DonatorChatCommand getDonatorChatCommand() {
		return donatorChatExecutor;
	}
	
	public ChatColorCommand getChatColorCommand() {
		return chatColorExecutor;
	}
	
	public MOTDCommand getMOTDCommand() {
		return motdExecutor;
	}
	
	public PlayerShopCommand getPlayerShopCommand() {
		return playerShopExecutor;
	} 
	
	public AnnouncerCommand getAnnouncerCommand() {
		return announcerExecutor;
	}
	
	public DropPartyCommand getDropPartyCommand() {
		return dropPartyExecutor;
	}
	
	public VoteCommand getVoteExecutor() {
		return voteExecutor;
	}
	
	public ReferalCommand getReferalExecutor() {
		return referalExecutor;
	}
	
	public InfoCommand getInfoExecutor() {
		return infoExecutor;
	}
	
	public StaffChatCommand getStaffChatExecutor() {
		return staffChatExecutor;
	}
	
	public GlobalCommand getGlobalExecutor() {
		return globalExecutor;
	}
	
	// Plugin Hooks
	private JobsHook jobsHookInstance;
	private TownyHook townyHookInstance;
	private VaultHook vaultHookInstance;
	private WorldEditHook worldEditHookInstance;
	private GroupManagerHook groupManagerHook;
	
	public JobsHook getJobsHook() {
		return jobsHookInstance;
	}
	
	public TownyHook getTownyHook() {
		return townyHookInstance;
	}
	
	public VaultHook getVaultHook() {
		return vaultHookInstance;
	}
	
	public WorldEditHook getWorldEditHook() {
		return worldEditHookInstance;
	}
	
	public GroupManagerHook getGroupManagerHook() {
		return groupManagerHook;
	}
	
	// Tasks
	private JobsBoosterTimeoutTask jobsBoosterTimeoutTask;
	private ClearExpiredChestsTask clearExpiredChestsTask;
	private VaultServerTotalTask vaultServerTotalTask;
	private AnnouncerTask announcerTask;
	private FlyRewardTask flyRewardTask;
	
	public JobsBoosterTimeoutTask getJobsBoosterTimeoutTask() {
		return jobsBoosterTimeoutTask;
	}
	
	public ClearExpiredChestsTask getClearedExpiredChestsTask() {
		return clearExpiredChestsTask;
	}
	
	public VaultServerTotalTask getVaultServerTotalTask() {
		return vaultServerTotalTask;
	}
	
	public AnnouncerTask getAnnouncerTask() {
		return announcerTask;
	}
	
	public void reRunAnnouncerTask() {
		announcerTask.cancel();
		announcerTask = new AnnouncerTask(this); announcerTask.runTaskTimer(this, this.getAnnouncerCommand().getInterval(), this.getAnnouncerCommand().getInterval());
	}
	
	public FlyRewardTask getFlyRewardTask() {
		return flyRewardTask;
	}
	
	// Listeners
	private InventoryClick inventoryClickListener;
	private PlayerJoin playerJoinListener;
	private PlayerQuit playerQuitListener;
	private BlockPlace blockPlaceListener;
	private DonatorChat donatorChatListener;
	private ColorChat colorChatListener;
	private MOTD motdListener;
	private BlockBreak blockBreakListener;
	private MobAttack mobAttackListener;
	private PlayerShop playerShopListener;
	private PrintingPress printingPressListener;
	private VoteWatcher voteWatcherListener;
	private PvPArena pvpArenaListener;
	private StaffChat staffChatListener;
	private JobsPayment jobsPaymentListener;
	
	public JobsPayment getJobsPaymentListener() {
		return jobsPaymentListener;
	}
	
	public InventoryClick getInventoryClickListener() {
		return inventoryClickListener;
	}

	public PlayerJoin getPlayerJoinListener() {
		return playerJoinListener;
	}

	public PlayerQuit getPlayerQuitListener() {
		return playerQuitListener;
	}
	
	public BlockPlace getBlockPlaceListener() {
		return blockPlaceListener;
	}
	
	public DonatorChat getDonatorChatListener() {
		return donatorChatListener;
	}
	
	public ColorChat getColorChatListener() {
		return colorChatListener;
	}
	
	public MOTD getMOTDListener() {
		return motdListener;
	}
	
	public BlockBreak getBlockBreakListener() {
		return blockBreakListener;
	}
	
	public MobAttack getMobAttackListener() {
		return mobAttackListener;
	}
	
	public PlayerShop getPlayerShopListener() {
		return playerShopListener;
	}
	
	public PrintingPress getPrintingPressListener() {
		return printingPressListener;
	}
	
	public VoteWatcher getVoteWatcherListener() {
		return voteWatcherListener;
	}
	
	public PvPArena getPvPArenaListener() {
		return pvpArenaListener;
	}
	
	public StaffChat getStaffChatListener() {
		return staffChatListener;
	}
	
	// Storage
	private ChestStorage chestStorage;
	private PlayerShopStorage playerShopStorage;
	private VoteStorage voteStorage;
	private PlayerCacheStorage playerCacheStorage;
	
	public ChestStorage getChestStorage() {
		return chestStorage;
	}
	
	public PlayerShopStorage getPlayerShopStorage() {
		return playerShopStorage;
	}
	
	public VoteStorage getVoteStorage() {
		return voteStorage;
	}
	
	public PlayerCacheStorage getPlayerCacheStorage() {
		return playerCacheStorage;
	}
	
	// Utils
	
	public void announce(String... string) {
		if (string.length > 0) {
			for (Player player:Bukkit.getOnlinePlayers()) {
				if (string.length > 1) {
					if (player.hasPermission(string[1]))
						player.sendMessage(string[0]);
					else continue;
				} else player.sendMessage(string[0]);
			}
		}
	}
	
	public void announce(TextComponent component, String permission) {
		for (Player player:Bukkit.getOnlinePlayers()) {
			if (permission == null || player.hasPermission(permission)) {
				TextComponentUtil.send(player, component);
			}
		}
	}
	
	public void actionAnnounce(String... string) {
		if (string.length > 0) {
			for (Player player:Bukkit.getOnlinePlayers()) {
				if (string.length > 1) {
					if (player.hasPermission(string[1]))
						ActionBar.send(player, string[0]);
					else continue;
				} else ActionBar.send(player, string[0]);
			}
		}
	}
	
	public void fullTitleAnnounce(String...string) {
		if (string.length > 1) {
			for (Player player:Bukkit.getOnlinePlayers()) {
				if (string.length > 2) {
					if (player.hasPermission(string[2]))
						player.sendTitle(string[0], string[1], 0, 100, 0);
					else continue;
				} else player.sendTitle(string[0], string[1], 0, 100, 0);
			}
		}
	}
	
	public void titleAnnounce(String... string) {
		if (string.length > 0) {
			for (Player player:Bukkit.getOnlinePlayers()) {
				if (string.length > 1) {
					if (player.hasPermission(string[1]))
						player.sendTitle(string[0], "", 0, 100, 0);
					else continue;
				} else player.sendTitle(string[0], "", 0, 100, 0);
			}
		}
	}
	
	public void subTitleAnnounce(String... string) {
		if (string.length > 0) {
			for (Player player:Bukkit.getOnlinePlayers()) {
				if (string.length > 1) {
					if (player.hasPermission(string[1]))
						player.sendTitle("", string[0], 0, 100, 0);
					else continue;
				} else player.sendTitle("", string[0], 0, 100, 0);
			}
		}
	}
	
	public String colify(String string) {
		return string.contains("&") ? string.replaceAll("&", "§") : string;
	}
	
	public List<String> colify(List<String> list) {
		List<String> ls = list;
		
		for (int i = 0; i < ls.size(); i++) {
			ls.set(i, this.colify(ls.get(i)));
		}
		
		return ls;
	}
	
	/**
	 * @param player
	 * @param material
	 * @return true if player has item.
	 */
	public boolean removeItem(Player player, Material material) {
		
		ItemStack[] items = player.getInventory().getContents().clone();
		
		for (int i = 0; i<items.length; i++) {
			if (items[i]==null)
				continue;
			ItemStack item = items[i].clone();
			if (item!=null && item.getType()==material) {
				if (item.getAmount() > 1)
					item.setAmount(item.getAmount() - 1);
				else item = new ItemStack(Material.AIR);
				
				items[i] = item;
				player.getInventory().setContents(items);
				player.updateInventory();
				return true;
			}
		}
		return false;
	}
	
	public void shuffleArray(ItemStack[] array) {
		Random rnd = ThreadLocalRandom.current();
		for (int i = array.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			ItemStack a = array[index];
			array[index] = array[i];
			array[i] = a;
		}
	}
	
	public ItemStack removeItem(Player player) {
		
		ItemStack[] items = player.getInventory().getContents().clone();
		
		this.shuffleArray(items);
		
		for (int i = 0; i<items.length; i++) {
			if (items[i]==null)
				continue;
			ItemStack item = items[i].clone();
			if (item!=null) {
				if (item.getAmount() > 1) {
					item.setAmount(item.getAmount() - 1);
					items[i] = item;
				}
				else items[i] = null;
				
				player.getInventory().setContents(items);
				player.updateInventory();
				
				ItemStack it = item.clone();
				it.setAmount(1);
				return it;
			}
		}
		return null;
	}
	
	public ItemStack removeItem(ItemStack[] items) {
		
		this.shuffleArray(items);
		
		for (int i = 0; i<items.length; i++) {
			if (items[i]==null)
				continue;
			ItemStack item = items[i].clone();
			if (item!=null) {
				if (item.getAmount() > 1) {
					item.setAmount(item.getAmount() - 1);
					items[i] = item;
				}
				else items[i] = null;
				
				
				ItemStack it = item.clone();
				it.setAmount(1);
				return it;
			}
		}
		return null;
	}
	
	/**
	 * @param range The Range of Blocks we're going to Look through.
	 * @return The Block the Player is Looking at.
	 */
	public final Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }
	
	@Override
	public void onEnable() {
		
		PlayerCache.setPlugin(this);
		
		// Configuration
		this.getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
			
		// Storage
		chestStorage = new ChestStorage(this);
		playerShopStorage = new PlayerShopStorage(this);
		voteStorage = new VoteStorage(this);
		playerCacheStorage = new PlayerCacheStorage(this);
		
		// Plugin Hooks
		jobsHookInstance = new JobsHook(this);
		townyHookInstance = new TownyHook(this);
		vaultHookInstance = new VaultHook(this);
		worldEditHookInstance = new WorldEditHook(this);
		groupManagerHook = new GroupManagerHook(this);
		
		// Tools
		TownyTools.setPlugin(this);
		ScoreboardLib.setPluginInstance(this);
		SimpleScoreboard.setPlugin(this);
		
		// Initialize Hooks
		jobsHookInstance.setEnabled(jobsHookInstance.initialize());
		townyHookInstance.setEnabled(townyHookInstance.initialize());
		vaultHookInstance.setEnabled(vaultHookInstance.initialize());	
		worldEditHookInstance.setEnabled(worldEditHookInstance.initialize());
		groupManagerHook.setEnabled(groupManagerHook.initialize());
		
		// Listeners
		Bukkit.getPluginManager().registerEvents(new PlayerMove(), this);
		inventoryClickListener = new InventoryClick(this);
		playerJoinListener = new PlayerJoin(this);
		playerQuitListener = new PlayerQuit(this);
		blockPlaceListener = new BlockPlace(this);
		donatorChatListener = new DonatorChat(this);
		colorChatListener = new ColorChat(this);
		motdListener = new MOTD(this);
		blockBreakListener = new BlockBreak(this);
		mobAttackListener = new MobAttack(this);
		playerShopListener = new PlayerShop(this);
		printingPressListener = new PrintingPress(this);
		voteWatcherListener = new VoteWatcher(this);
		pvpArenaListener = new PvPArena(this);
		staffChatListener = new StaffChat(this);
		jobsPaymentListener = new JobsPayment();
		
		// Command Executors
		this.getCommand("jobs-booster").setExecutor(jobsBoosterExecutor = new JobsBoosterCommand(this));
		this.getCommand("print").setExecutor(printExecutor = new PrintCommand(this));
		this.getCommand("rename").setExecutor(renameExecutor = new RenameCommand(this));
		this.getCommand("glow").setExecutor(glowExecutor = new GlowCommand(this));
		this.getCommand("store").setExecutor(storeExecutor = new StoreCommand(this));
		this.getCommand("reclaim").setExecutor(reclaimExecutor = new ReclaimCommand(this));
		this.getCommand("donator-chat").setExecutor(donatorChatExecutor = new DonatorChatCommand(this));
		this.getCommand("chat-color").setExecutor(chatColorExecutor = new ChatColorCommand(this));
		this.getCommand("server-motd").setExecutor(motdExecutor = new MOTDCommand(this));
		this.getCommand("player-shop").setExecutor(playerShopExecutor = new PlayerShopCommand(this));
		this.getCommand("announcer").setExecutor(announcerExecutor = new AnnouncerCommand(this));
		this.getCommand("drop-party").setExecutor(dropPartyExecutor = new DropPartyCommand(this));
		this.getCommand("vote").setExecutor(voteExecutor = new VoteCommand(this));
		this.getCommand("referal").setExecutor(referalExecutor = new ReferalCommand(this));
		this.getCommand("info").setExecutor(infoExecutor = new InfoCommand(this));
		this.getCommand("staff-chat").setExecutor(staffChatExecutor = new StaffChatCommand(this));
		this.getCommand("global").setExecutor(globalExecutor = new GlobalCommand(this));
		
		// Start Tasks
		jobsBoosterTimeoutTask = new JobsBoosterTimeoutTask(this); jobsBoosterTimeoutTask.runTaskTimer(this, 0, 1200);
		clearExpiredChestsTask = new ClearExpiredChestsTask(this); clearExpiredChestsTask.runTaskTimerAsynchronously(this, 0, 12000);
		vaultServerTotalTask = new VaultServerTotalTask(this); vaultServerTotalTask.runTaskTimerAsynchronously(this, 0, 12000);
		announcerTask = new AnnouncerTask(this); announcerTask.runTaskTimer(this, this.getAnnouncerCommand().getInterval(), this.getAnnouncerCommand().getInterval());
		flyRewardTask = new FlyRewardTask(this); flyRewardTask.runTaskTimer(this, 1200, 1200); // 1200 1200
		
		PlayerCache.convertAllVoteData();
		
		for (Player player:Bukkit.getOnlinePlayers())
			PlayerCache.get(player).open();
	}
	
	@Override
	public void onDisable() {
		
		// Close Extras Executors
		jobsBoosterExecutor.close();
		announcerExecutor.close();
		infoExecutor.close();
		
		// Cancel Tasks
		jobsBoosterTimeoutTask.cancel();
		clearExpiredChestsTask.cancel();
		vaultServerTotalTask.cancel();
		
		// Save Listeners
		motdListener.save();
		pvpArenaListener.save();
		
		// Close Storage
		voteStorage.close();
		PlayerCache.closeAll();
		
		this.getCommand("jobs-booster").setExecutor(null);
		this.getCommand("print").setExecutor(null);
		this.getCommand("rename").setExecutor(null);
		this.getCommand("glow").setExecutor(null);
		this.getCommand("store").setExecutor(null);
		this.getCommand("reclaim").setExecutor(null);
		this.getCommand("donator-chat").setExecutor(null);
		this.getCommand("chat-color").setExecutor(null);
		this.getCommand("server-motd").setExecutor(null);
		
		ClearExpiredChestsTask.runAll(this);
	}
	
}
