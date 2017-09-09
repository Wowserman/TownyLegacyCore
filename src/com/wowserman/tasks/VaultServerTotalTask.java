package com.wowserman.tasks;

import java.math.BigDecimal;

import org.bukkit.scheduler.BukkitRunnable;

import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.wowserman.TownyLegacy;

public class VaultServerTotalTask extends BukkitRunnable {

	private TownyLegacy plugin;
	
	private BigDecimal total = BigDecimal.ZERO;
	
	public VaultServerTotalTask(TownyLegacy instance) {
		this.plugin = instance;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		total = BigDecimal.ZERO;
		
		for (
				Resident resident:
					plugin.
					getTownyHook().
					getPlugin().
					getTownyUniverse().
					getResidents()
					) {
			try {
				total = total.add(BigDecimal.valueOf(resident.getHoldingBalance()));
			} catch (EconomyException e) {
				
			}
		}
		
		plugin.getVaultHook().setServerTotal(total);
		
		plugin.getLogger().info("Updated Server Account Total: " + plugin.getVaultHook().getFormattedServerTotal());
	}
	
	

}
