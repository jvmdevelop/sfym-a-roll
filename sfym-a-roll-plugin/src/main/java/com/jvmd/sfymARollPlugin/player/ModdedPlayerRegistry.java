package com.jvmd.sfymARollPlugin.player;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;

public final class ModdedPlayerRegistry {
    private final Set<UUID> moddedPlayers = ConcurrentHashMap.newKeySet();
    private final Set<UUID> fallbackRollPlayers = ConcurrentHashMap.newKeySet();

    public void markModded(Player player) {
        UUID playerId = player.getUniqueId();
        moddedPlayers.add(playerId);
        fallbackRollPlayers.remove(playerId);
    }

    public void markFallbackRoll(Player player) {
        UUID playerId = player.getUniqueId();
        if (!moddedPlayers.contains(playerId)) {
            fallbackRollPlayers.add(playerId);
        }
    }

    public boolean isModded(Player player) {
        return moddedPlayers.contains(player.getUniqueId());
    }

    public boolean usesFallbackRoll(Player player) {
        return fallbackRollPlayers.contains(player.getUniqueId());
    }

    public void forget(Player player) {
        UUID playerId = player.getUniqueId();
        moddedPlayers.remove(playerId);
        fallbackRollPlayers.remove(playerId);
    }
}
