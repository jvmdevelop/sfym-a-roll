package com.jvmd.sfymARollPlugin.roll;

import com.jvmd.sfymARollPlugin.checker.Checker;
import com.jvmd.sfymARollPlugin.checker.CooldownChecker;
import com.jvmd.sfymARollPlugin.player.ModdedPlayerRegistry;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.Plugin;

public final class ShiftRollListener implements Listener {
    private static final long DOUBLE_SHIFT_WINDOW_MILLIS = 350L;
    private static final long HANDSHAKE_WAIT_TICKS = 40L;

    private final Plugin plugin;
    private final RollHandler rollHandler;
    private final CooldownChecker cooldownChecker;
    private final ModdedPlayerRegistry moddedPlayerRegistry;
    private final List<Checker> checkers;
    private final Map<UUID, Long> lastSneakPresses = new ConcurrentHashMap<>();

    public ShiftRollListener(
            Plugin plugin,
            RollHandler rollHandler,
            CooldownChecker cooldownChecker,
            ModdedPlayerRegistry moddedPlayerRegistry,
            Checker... checkers
    ) {
        this.plugin = plugin;
        this.rollHandler = rollHandler;
        this.cooldownChecker = cooldownChecker;
        this.moddedPlayerRegistry = moddedPlayerRegistry;
        this.checkers = List.of(checkers);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline() && !moddedPlayerRegistry.isModded(player)) {
                moddedPlayerRegistry.markFallbackRoll(player);
            }
        }, HANDSHAKE_WAIT_TICKS);
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) {
            return;
        }

        Player player = event.getPlayer();
        if (!moddedPlayerRegistry.usesFallbackRoll(player)) {
            return;
        }

        long now = System.currentTimeMillis();
        Long lastSneakPress = lastSneakPresses.put(player.getUniqueId(), now);
        if (lastSneakPress == null || now - lastSneakPress > DOUBLE_SHIFT_WINDOW_MILLIS) {
            return;
        }

        lastSneakPresses.remove(player.getUniqueId());
        for (Checker checker : checkers) {
            if (!checker.check(player)) {
                return;
            }
        }

        if (rollHandler.roll(player)) {
            cooldownChecker.markUsed(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        lastSneakPresses.remove(player.getUniqueId());
        moddedPlayerRegistry.forget(player);
    }
}
