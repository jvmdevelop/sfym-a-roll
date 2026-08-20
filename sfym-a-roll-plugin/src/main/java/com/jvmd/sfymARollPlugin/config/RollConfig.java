package com.jvmd.sfymARollPlugin.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public final class RollConfig {
    private final long cooldownMillis;
    private final double strength;
    private final int iframeTicks;
    private final long doubleShiftWindowMillis;
    private final long handshakeWaitTicks;

    private RollConfig(
            long cooldownMillis,
            double strength,
            int iframeTicks,
            long doubleShiftWindowMillis,
            long handshakeWaitTicks
    ) {
        this.cooldownMillis = cooldownMillis;
        this.strength = strength;
        this.iframeTicks = iframeTicks;
        this.doubleShiftWindowMillis = doubleShiftWindowMillis;
        this.handshakeWaitTicks = handshakeWaitTicks;
    }

    public static RollConfig load(Plugin plugin) {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();
        return new RollConfig(
                config.getLong("roll.cooldown-millis", 1200L),
                config.getDouble("roll.strength", 1.15D),
                config.getInt("roll.iframe-ticks", 12),
                config.getLong("fallback.double-shift-window-millis", 350L),
                config.getLong("fallback.handshake-wait-ticks", 40L)
        );
    }

    public long cooldownMillis() {
        return cooldownMillis;
    }

    public double strength() {
        return strength;
    }

    public int iframeTicks() {
        return iframeTicks;
    }

    public long doubleShiftWindowMillis() {
        return doubleShiftWindowMillis;
    }

    public long handshakeWaitTicks() {
        return handshakeWaitTicks;
    }
}
