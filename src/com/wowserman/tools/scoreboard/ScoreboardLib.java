package com.wowserman.tools.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.wowserman.tools.scoreboard.type.Scoreboard;
import com.wowserman.tools.scoreboard.type.SimpleScoreboard;

public final class ScoreboardLib {

    private static Plugin instance;

    public static Plugin getPluginInstance() {
        return instance;
    }

    public static void setPluginInstance(Plugin instance) {
        if (ScoreboardLib.instance != null) return;
        ScoreboardLib.instance = instance;
    }

    public static Scoreboard createScoreboard(Player holder) {
        return new SimpleScoreboard(holder);
    }
}