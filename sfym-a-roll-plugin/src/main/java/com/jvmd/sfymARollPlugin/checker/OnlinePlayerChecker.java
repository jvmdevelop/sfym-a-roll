package com.jvmd.sfymARollPlugin.checker;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class OnlinePlayerChecker implements Checker {
    @Override
    public boolean check(Player player) {
        return player != null
                && player.isOnline()
                && Bukkit.getPlayer(player.getUniqueId()) != null;
    }
}
