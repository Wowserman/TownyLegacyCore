package com.wowserman.hooks;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.wowserman.TownyLegacy;

import net.milkbowl.vault.economy.Economy;

public class VaultHook implements PluginHook {
	
	private Economy economy;
	
	public Economy getEconomy() {
		return economy;
	}
	
	private static BigDecimal serverTotal = BigDecimal.ZERO;
	
	private BigDecimal getServerTotal() {
		return serverTotal;
	}
	
	private static final DecimalFormat format = new DecimalFormat("#,###.00");
	
	public String getFormattedServerTotal() {
		return (format.format(getServerTotal().movePointLeft(6)) + "m").startsWith(".") ? "0" + format.format(getServerTotal().movePointLeft(6)) + "m": format.format(getServerTotal().movePointLeft(6)) + "m";
	}
	
	public String getFormattedPlayerTotal(Player player) {
		
		double amount = economy.getBalance(Bukkit.getOfflinePlayer(player.getUniqueId()));
		
		String format = String.format("%,.2f", amount);
		
		return "$" + format;
	}
	
	public void setServerTotal(BigDecimal value) {
		serverTotal = value;
	}
	
	@Override
	public String getName() {
		return "Vault";
	}

	@Override
	public boolean initialize() {
		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
	}
	
	public VaultHook(TownyLegacy instance) {

	}

	private boolean enabled;
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean value) {
		enabled = value;
	}
}
