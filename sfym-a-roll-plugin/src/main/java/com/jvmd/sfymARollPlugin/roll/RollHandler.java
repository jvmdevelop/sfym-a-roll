package com.jvmd.sfymARollPlugin.roll;

import org.bukkit.entity.Player;

public final class RollHandler {
    private final RollProcessor rollProcessor;

    public RollHandler(RollProcessor rollProcessor) {
        this.rollProcessor = rollProcessor;
    }

    public boolean roll(Player player) {
        return rollProcessor.process(player);
    }
}
