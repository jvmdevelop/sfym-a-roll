package com.jvmd.sfymARollPlugin.roll;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public final class RollProcessor {
    private final Plugin plugin;
    private final RollState rollState;
    private final double strength;
    private final int iframeTicks;

    public RollProcessor(Plugin plugin, RollState rollState, double strength, int iframeTicks) {
        this.plugin = plugin;
        this.rollState = rollState;
        this.strength = strength;
        this.iframeTicks = iframeTicks;
    }

    public boolean process(Player player) {
        if (!player.isOnGround() || player.isFlying() || player.getGameMode() == GameMode.SPECTATOR) {
            return false;
        }

        Vector direction = player.getLocation().getDirection();
        direction.setY(0.0D);
        if (direction.lengthSquared() < 0.0001D) {
            return false;
        }

        Vector velocity = direction.normalize().multiply(strength);
        velocity.setY(Math.min(player.getVelocity().getY(), 0.0D));
        player.setVelocity(velocity);
        rollState.begin(player, plugin, iframeTicks);
        return true;
    }
}
