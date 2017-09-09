package com.wowserman.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBar {

	private static String nmsVersion;
	
	public static void send(Player player, String message) {
        if (!player.isOnline()) {
            return; 
        }
        
        if (nmsVersion==null) {
        	nmsVersion = Bukkit.getServer().getClass().getPackage().getName();
        	nmsVersion = nmsVersion.substring(nmsVersion.lastIndexOf(".") + 1);
        }

        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object ppoc;
            Class<?> c4 = Class.forName("net.minecraft.server." + nmsVersion + ".PacketPlayOutChat");
            Class<?> c5 = Class.forName("net.minecraft.server." + nmsVersion + ".Packet");
            Class<?> c2 = Class.forName("net.minecraft.server." + nmsVersion + ".ChatComponentText");
            Class<?> c3 = Class.forName("net.minecraft.server." + nmsVersion + ".IChatBaseComponent");
            Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + nmsVersion + ".ChatMessageType");
            Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
            Object chatMessageType = null;
            for (Object obj : chatMessageTypes) {
                if (obj.toString().equals("GAME_INFO")) {
                    chatMessageType = obj;
                }
            }
            Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(message);
            ppoc = c4.getConstructor(new Class<?>[]{c3, chatMessageTypeClass}).newInstance(o, chatMessageType);
            Method m1 = craftPlayerClass.getDeclaredMethod("getHandle");
            Object h = m1.invoke(craftPlayer);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
            m5.invoke(pc, ppoc);
        } catch (Exception ex) {
        	
        }
    }
}
