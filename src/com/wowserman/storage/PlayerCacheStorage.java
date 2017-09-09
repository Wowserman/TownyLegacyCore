package com.wowserman.storage;

import com.wowserman.TownyLegacy;

public class PlayerCacheStorage extends StorageFile {

	public PlayerCacheStorage(TownyLegacy instance) {
		super(instance);
	}
	
	@Override
	public String getName() {
		return "cache-data";
	}

}
