package com.wowserman.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.object.Town;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TextComponentUtil {
	
	/**
	 * Get a formated List of TextComponents for a Town's Warps.
	 * 
	 * '  &3/town warp &bGoldFarm Geneva &7(75.0, 52.25, -400.0, World_nether)'
	 * 
	 * 
	 * @param town
	 * @param townSpecific
	 * @return Output
	 */
	public static List<TextComponent> formatWarps(Town town, boolean townSpecific) {
		List<TextComponent> list = new ArrayList<TextComponent>();
		
		for (int i = 0; i < town.getWarpCount(); i++) {
			
			String warp = (String) town.getWarps().keySet().toArray()[i];
			Location loc = town.getWarp(warp);
			
			TextComponent command = new TextComponent("  /town warp");
			command.setColor(ChatColor.DARK_AQUA);
			command.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/town warp " + warp + " " + (townSpecific ? town.getName():"")));

			TextComponent command2 = new TextComponent(" " + warp + " " + (townSpecific ? town.getName():""));
			command2.setColor(ChatColor.AQUA);
			command2.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/town warp " + warp + " " + (townSpecific ? town.getName():"")));
			command2.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder("Click to Teleport to " + warp + "!").create()));
			
			TextComponent info = new TextComponent("(" + loc.getBlockX() + ", "  + loc.getBlockY() + ", " + loc.getBlockZ() + ", " + loc.getWorld().getName() + ")");
			info.setColor(ChatColor.GRAY);
			info.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "(" + loc.getBlockX() + ", "  + loc.getBlockY() + ", " + loc.getBlockZ() + ", " + loc.getWorld().getName() + ")"));
			
			

			command.addExtra(command2);
			command.addExtra(info);
			
			list.add(command);
		}

		return list;
	}
	
	private static boolean isColor(ChatColor color) {
		return !isFormat(color);
	}
	
	private static boolean isFormat(ChatColor color) {
		return color==ChatColor.BOLD || color==ChatColor.UNDERLINE || color==ChatColor.STRIKETHROUGH || color==ChatColor.ITALIC || color==ChatColor.MAGIC;
	}
	
	/**
	 * Returns Length of Variable (Example: Fish->4)
	 * 
	 * @param string
	 * @return -1 if no Variable.
	 */
	private static int containsVariable(String string) {
		
		for (Player player:Bukkit.getOnlinePlayers())
			if (string.equalsIgnoreCase(player.getName()))
				return player.getName().length();
		
		return -1;
	}
	
	private static String getVariableName(String string) {
		
		for (Player player:Bukkit.getOnlinePlayers()) 
			if (string.length() != player.getName().length() && string.toLowerCase().contains(player.getName().toLowerCase()))
				return player.getName();
		
		return null;
	}
	
	private static List<String> getValueOfVariable(String variable) {
		
		if (variable==null)
			return null;
		
		List<String> value = new ArrayList<String>();
		
		for (Player player:Bukkit.getOnlinePlayers()) 
			// This is how you should properly compare the variable to whatever.
			if (variable.toLowerCase().contains(player.getName().toLowerCase())) {

				value.add(player.getName());
				value.add(player.getFoodLevel() + " Food.");
				value.add(player.getExp() + " Exp.");
				
				return value;
			}
		
		// Return Null when the variable of that name doesn't Exist.
		return null;
	}
	
	public static List<TextComponent> splitMessageOfFormats(String message, Player player) {
		List<TextComponent> list = new ArrayList<TextComponent>();
				
		List<String> splits = new ArrayList<String>();
		
		splits.addAll(Arrays.asList(message.split(String.valueOf(ChatColor.COLOR_CHAR))));
		
		for (int index = 0; index < splits.size(); index++)
			splits.set(index, String.valueOf(ChatColor.COLOR_CHAR) + splits.get(index));
		
		System.out.print(splits);
		
		ChatColor lastColor = null;
		ChatColor lastFormat = null;
		
		int size = splits.size();
		
		for (int i = 0; i < size; i++) {
			String split = splits.get(i);
			
			if ((split.length()==1 && split.charAt(0)==ChatColor.COLOR_CHAR) || split.length()==0) {
				System.out.print("Is Color Char only or Empty, skipping and removing.");
				splits.remove(i);
				size = splits.size();
				i--;
				continue;
			}
			
			if (split.length()>=2 && split.startsWith(String.valueOf(ChatColor.COLOR_CHAR))) {
				ChatColor color = ChatColor.getByChar(split.charAt(1));
				System.out.print("Starts with Color Char and a Code.");
				
				if (TextComponentUtil.isColor(color)) {
					lastColor = color;
					lastFormat = null;
				} else lastFormat = color;
				
				if (split.length()==2) {
					System.out.print("^- Only a Color Char and a Code, Removing it and Skipping it.");
					splits.remove(i);
					size = splits.size();
					i--;
					continue;
				} else {
					System.out.print("^- Has more after Color Char and Code, setting Sub '" + split.substring(2, split.length()) + "'.");
					split = split.substring(2, split.length());
				}
				
			}
			
			System.out.print("Starting with #" + i + ": " + split);
			
			if (TextComponentUtil.getVariableName(split) != null && TextComponentUtil.isURL(split) == false) {
				String variable = TextComponentUtil.getVariableName(split);
				
				int index = split.toLowerCase().indexOf(variable.toLowerCase());
				
				String first = split.toLowerCase().contains(variable.toLowerCase()) ? split.substring(0, variable.length()):split.substring(0, index);
				String remainder = split.replace(first, "");
				
				List<String> newSplits = new ArrayList<String>();
				
				newSplits.addAll(splits.subList(0, i));
				
				if (first.length()!=0)
					newSplits.add(first);
				if (remainder.length()!=0)
					newSplits.add(remainder);
				
				newSplits.addAll(splits.subList(i + 1, splits.size()));
				
				size = newSplits.size();
				
				System.out.print("New List: " + newSplits);
				
				splits = newSplits;
				
				split = first;
			}
			
//			if (split.contains(player.getName()) && split.length()!=player.getName().length()) {
//				
//				String first = split.startsWith(player.getName()) ? split.substring(0, player.getName().length()):split.substring(0, split.indexOf(player.getName()));
//				
//				System.out.print("First: '" + first + "'");
//				
//				System.out.print("Index of " + player.getName() + " in '" + split + "': " + split.indexOf(player.getName()));
//				
//				// split.indexOf(player.getName())==0 ? split.substring(split.indexOf(player.getName()) + player.getName().length(), split.length()):
//				String remainder = split.replace(first, "");
//				
//				System.out.print("Remainder: '" + remainder + "'");
//				
//				List<String> newSplits = new ArrayList<String>();
//				newSplits.addAll(splits.subList(0, i));
//				if (first.length()!=0)
//					newSplits.add(first);
//				if (remainder.length()!=0)
//					newSplits.add(remainder);
//				newSplits.addAll(splits.subList(i + 1, splits.size()));
//				
//				size = newSplits.size();
//				
//				System.out.print("New List: " + newSplits);
//				
//				splits = newSplits;
//				
//				split = first;
//			}
			
			if (split.length()==0)
				continue;
			
			System.out.print("Building component for '" + split + "'");
			TextComponent component = new TextComponent(split);
			
			if (lastFormat != null) {
				if (lastFormat==ChatColor.BOLD)
					component.setBold(true);
				else if (lastFormat==ChatColor.ITALIC)
					component.setItalic(true);
				else if (lastFormat==ChatColor.STRIKETHROUGH)
					component.setItalic(true);
				else if (lastFormat==ChatColor.MAGIC)
					component.setObfuscated(true);
				else if (lastFormat==ChatColor.UNDERLINE)
					component.setUnderlined(true);
				else component.setColor(lastFormat);
			}
			
			if (lastColor != null) {
				if (lastColor==ChatColor.BOLD)
					component.setBold(true);
				else if (lastColor==ChatColor.ITALIC)
					component.setItalic(true);
				else if (lastColor==ChatColor.STRIKETHROUGH)
					component.setItalic(true);
				else if (lastColor==ChatColor.MAGIC)
					component.setObfuscated(true);
				else if (lastColor==ChatColor.UNDERLINE)
					component.setUnderlined(true);
				else component.setColor(lastColor);
			}
			
			list.add(component);
			
			System.out.print("Finished with: " + split);
		}
		
		return list;
	}
	
	private static boolean isURL(String split) {
		return split.toLowerCase().startsWith("https://");
	}

	public static TextComponent getFormattedMessage(String message, Player player) {
		List<TextComponent> list = splitMessageOfFormats(message, player);
		
		TextComponent component = new TextComponent("");
		
		for (TextComponent subComponent:list) {
			
			List<String> value = TextComponentUtil.getValueOfVariable(subComponent.getText());
			
			System.out.print("'" + subComponent.getText() + "' contains Variable: " + TextComponentUtil.containsVariable(subComponent.getText()));
			System.out.print("Value of Variable: '" + value + "'");
			
			if (value != null)
				subComponent.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder(String.join("\n", value)).create()));
			component.addExtra(subComponent);
		}
		
//		for (TextComponent c:list) {
//			if (c.getText().equals(player.getName()))
//				c.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
//						new ComponentBuilder("TEST\nTEST").create()));
//			
//			component.addExtra(c);
//		}
//		
		return component;
	}
	
	public static TextComponent getTextComponent(String text, ChatColor color, String hoverMessage, String clickCommand) {
		TextComponent message = new TextComponent(text);
		message.setColor(color);
		message.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMessage).create()));
		message.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, clickCommand));
		return message;
	}

	public static void send(Player player, TextComponent component) {
		player.spigot().sendMessage(component);
	}
}
