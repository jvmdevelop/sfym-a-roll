package com.jvmd.sfymARollPlugin.checker;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;

public final class CooldownChecker implements Checker {
    private final Map<UUID, Long> lastRolls = new ConcurrentHashMap<>();
    private final long cooldownMillis;

    public CooldownChecker(long cooldownMillis) {
        this.cooldownMillis = cooldownMillis;
    }

    @Override
    public boolean check(Player player) {
        Long lastRoll = lastRolls.get(player.getUniqueId());
        return lastRoll == null || System.currentTimeMillis() - lastRoll >= cooldownMillis;
    }

    public void markUsed(Player player) {
        lastRolls.put(player.getUniqueId(), System.currentTimeMillis());
    }
}
