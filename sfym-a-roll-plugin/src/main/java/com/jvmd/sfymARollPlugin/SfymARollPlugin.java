package com.jvmd.sfymARollPlugin;

import com.jvmd.sfymARollPlugin.checker.CooldownChecker;
import com.jvmd.sfymARollPlugin.checker.OnlinePlayerChecker;
import com.jvmd.sfymARollPlugin.config.RollConfig;
import com.jvmd.sfymARollPlugin.net.RollPluginMessageListener;
import com.jvmd.sfymARollPlugin.player.ModdedPlayerRegistry;
import com.jvmd.sfymARollPlugin.roll.RollHandler;
import com.jvmd.sfymARollPlugin.roll.RollProcessor;
import com.jvmd.sfymARollPlugin.roll.RollProtectionListener;
import com.jvmd.sfymARollPlugin.roll.RollState;
import com.jvmd.sfymARollPlugin.roll.ShiftRollListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class SfymARollPlugin extends JavaPlugin {
    public static final String ROLL_CHANNEL = "sfym_a_roll:roll";

    private RollPluginMessageListener rollPluginMessageListener;

    @Override
    public void onEnable() {
        RollConfig rollConfig = RollConfig.load(this);
        CooldownChecker cooldownChecker = new CooldownChecker(rollConfig.cooldownMillis());
        OnlinePlayerChecker onlinePlayerChecker = new OnlinePlayerChecker();
        ModdedPlayerRegistry moddedPlayerRegistry = new ModdedPlayerRegistry();
        RollState rollState = new RollState();
        RollHandler rollHandler = new RollHandler(new RollProcessor(
                this,
                rollState,
                rollConfig.strength(),
                rollConfig.iframeTicks()
        ));

        rollPluginMessageListener = new RollPluginMessageListener(
                rollHandler,
                cooldownChecker,
                moddedPlayerRegistry,
                onlinePlayerChecker,
                cooldownChecker
        );

        getServer().getMessenger().registerIncomingPluginChannel(this, ROLL_CHANNEL, rollPluginMessageListener);
        getServer().getMessenger().registerOutgoingPluginChannel(this, ROLL_CHANNEL);
        getServer().getPluginManager().registerEvents(new RollProtectionListener(rollState), this);
        getServer().getPluginManager().registerEvents(new ShiftRollListener(
                this,
                rollHandler,
                cooldownChecker,
                moddedPlayerRegistry,
                rollConfig.doubleShiftWindowMillis(),
                rollConfig.handshakeWaitTicks(),
                onlinePlayerChecker,
                cooldownChecker
        ), this);
    }

    @Override
    public void onDisable() {
        if (rollPluginMessageListener != null) {
            getServer().getMessenger().unregisterIncomingPluginChannel(this, ROLL_CHANNEL, rollPluginMessageListener);
        }
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, ROLL_CHANNEL);
    }
}
