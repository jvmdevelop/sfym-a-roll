package com.jvmd.sfymARollPlugin.roll;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public final class RollProcessor {
    private static final double ROLL_STRENGTH = 1.15D;
    private static final int ROLL_IFRAME_TICKS = 12;

    private final Plugin plugin;
    private final RollState rollState;

    public RollProcessor(Plugin plugin, RollState rollState) {
        this.plugin = plugin;
        this.rollState = rollState;
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

        Vector velocity = direction.normalize().multiply(ROLL_STRENGTH);
        velocity.setY(Math.min(player.getVelocity().getY(), 0.0D));
        player.setVelocity(velocity);
        rollState.begin(player, plugin, ROLL_IFRAME_TICKS);
        return true;
    }
}
