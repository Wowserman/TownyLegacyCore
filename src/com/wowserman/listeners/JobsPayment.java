package com.wowserman.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gamingmesh.jobs.api.JobsPaymentEvent;
import com.wowserman.PlayerCache;

public class JobsPayment implements Listener {

	@EventHandler
	public void pay(JobsPaymentEvent e) {
		PlayerCache cache = PlayerCache.get(e.getPlayer().getUniqueId().toString());
		
		e.setAmount(e.getAmount() * cache.getJobsBooster());
	}
}
