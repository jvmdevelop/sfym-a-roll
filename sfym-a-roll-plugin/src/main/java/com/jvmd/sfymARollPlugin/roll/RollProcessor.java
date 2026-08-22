package com.jvmd.sfymARollPlugin.roll;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public final class RollProcessor {
    private final Plugin plugin;
    private final RollState rollState;
    private final double strength;
    private final int iframeTicks;
    private final double manaCost;
    private final Method playerDataGetMethod;
    private final Method getManaMethod;
    private final Method giveManaMethod;

    public RollProcessor(Plugin plugin, RollState rollState, double strength, int iframeTicks, double manaCost) {
        this.plugin = plugin;
        this.rollState = rollState;
        this.strength = strength;
        this.iframeTicks = iframeTicks;
        this.manaCost = manaCost;
        try {
            Class<?> playerDataClass = Class.forName("net.Indyuce.mmocore.api.player.PlayerData");
            this.playerDataGetMethod = playerDataClass.getMethod("get", OfflinePlayer.class);
            this.getManaMethod = playerDataClass.getMethod("getMana");
            this.giveManaMethod = playerDataClass.getMethod("giveMana", double.class);
        } catch (ClassNotFoundException | NoSuchMethodException exception) {
            throw new IllegalStateException("MMOCore PlayerData API is not available", exception);
        }
    }

    public boolean process(Player player) {
        if (!player.isOnGround() || player.isFlying() || player.getGameMode() == GameMode.SPECTATOR) {
            return false;
        }

        Object playerData = null;
        if (manaCost > 0.0D) {
            playerData = getPlayerData(player);
            if (getMana(playerData) < manaCost) {
                return false;
            }
        }

        Vector direction = player.getLocation().getDirection();
        direction.setY(0.0D);
        if (direction.lengthSquared() < 0.0001D) {
            return false;
        }

        if (manaCost > 0.0D) {
            giveMana(playerData, -manaCost);
        }

        Vector velocity = direction.normalize().multiply(strength);
        velocity.setY(Math.min(player.getVelocity().getY(), 0.0D));
        player.setVelocity(velocity);
        rollState.begin(player, plugin, iframeTicks);
        return true;
    }

    private Object getPlayerData(Player player) {
        try {
            return playerDataGetMethod.invoke(null, player);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            throw new IllegalStateException("Could not read MMOCore player data for " + player.getName(), exception);
        }
    }

    private double getMana(Object playerData) {
        try {
            return (double) getManaMethod.invoke(playerData);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            throw new IllegalStateException("Could not read MMOCore mana", exception);
        }
    }

    private void giveMana(Object playerData, double amount) {
        try {
            giveManaMethod.invoke(playerData, amount);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            throw new IllegalStateException("Could not update MMOCore mana", exception);
        }
    }
}
