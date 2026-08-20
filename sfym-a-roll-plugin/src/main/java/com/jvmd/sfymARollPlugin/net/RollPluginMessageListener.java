package com.jvmd.sfymARollPlugin.net;

import com.jvmd.sfymARollPlugin.SfymARollPlugin;
import com.jvmd.sfymARollPlugin.checker.Checker;
import com.jvmd.sfymARollPlugin.checker.CooldownChecker;
import com.jvmd.sfymARollPlugin.player.ModdedPlayerRegistry;
import com.jvmd.sfymARollPlugin.roll.RollHandler;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public final class RollPluginMessageListener implements PluginMessageListener {
    private static final byte PAYLOAD_HANDSHAKE = 0;
    private static final byte PAYLOAD_ROLL = 1;

    private final RollHandler rollHandler;
    private final CooldownChecker cooldownChecker;
    private final ModdedPlayerRegistry moddedPlayerRegistry;
    private final List<Checker> checkers;

    public RollPluginMessageListener(
            RollHandler rollHandler,
            CooldownChecker cooldownChecker,
            ModdedPlayerRegistry moddedPlayerRegistry,
            Checker... checkers
    ) {
        this.rollHandler = rollHandler;
        this.cooldownChecker = cooldownChecker;
        this.moddedPlayerRegistry = moddedPlayerRegistry;
        this.checkers = List.of(checkers);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!SfymARollPlugin.ROLL_CHANNEL.equals(channel)) {
            return;
        }

        if (message.length > 0 && message[0] == PAYLOAD_HANDSHAKE) {
            moddedPlayerRegistry.markModded(player);
            return;
        }

        if (message.length > 0 && message[0] != PAYLOAD_ROLL) {
            return;
        }

        moddedPlayerRegistry.markModded(player);
        for (Checker checker : checkers) {
            if (!checker.check(player)) {
                sendRollResponse(player, false);
                return;
            }
        }

        boolean rolled = rollHandler.roll(player);
        if (rolled) {
            cooldownChecker.markUsed(player);
        }
        sendRollResponse(player, rolled);
    }

    private void sendRollResponse(Player player, boolean accepted) {
        player.sendPluginMessage(SfymARollPlugin.getPlugin(SfymARollPlugin.class), SfymARollPlugin.ROLL_CHANNEL, new byte[] {
                (byte) (accepted ? 1 : 0)
        });
    }
}
