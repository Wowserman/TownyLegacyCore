package com.wowserman;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class EconomyHandler {
	
	private static boolean usingEconomy = false;
	
	public static boolean isUsingEconomy() {
		return usingEconomy;
	}
	
	private static Economy economy = null;
	
	public static Economy getEconomy() {
		return economy;
	}
	
	private TownyLegacy plugin;
	
	private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        
        return usingEconomy = economy != null;
    }
	
	public EconomyHandler(TownyLegacy instance) {
		this.setupEconomy();
	}
	
	public OfflinePlayer offline(Player player) {
		return Bukkit.getOfflinePlayer(player.getUniqueId());
	}
	
	@SuppressWarnings("deprecation")
	public double getBalance(Player player) {
		return EconomyHandler.isUsingEconomy() ? economy.getBalance(player.getName()):0;
	}
	
	@SuppressWarnings("deprecation")
	public boolean has(Player player, double amount) {
		return EconomyHandler.isUsingEconomy() ? economy.has(player.getName(), amount):false;
	}
	
	public boolean remove(Player player, double amount) {
		EconomyResponse response = economy.withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), amount);
	
		
		
		return true;
	}
}
