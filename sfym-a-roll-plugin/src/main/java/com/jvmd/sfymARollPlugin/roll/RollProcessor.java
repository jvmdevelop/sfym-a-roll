package com.jvmd.sfymARollPlugin.roll;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public final class RollProcessor {
    private static final double ROLL_STRENGTH = 1.15D;

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
        return true;
    }
}
