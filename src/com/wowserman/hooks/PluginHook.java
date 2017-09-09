package com.wowserman.hooks;

public interface PluginHook {
	
	public void setEnabled(boolean value);
	
	public boolean isEnabled();
	
	public String getName();
	
	public boolean initialize();
}
