package com.jvmd.sfymARollPlugin;

import com.jvmd.sfymARollPlugin.checker.CooldownChecker;
import com.jvmd.sfymARollPlugin.checker.OnlinePlayerChecker;
import com.jvmd.sfymARollPlugin.net.RollPluginMessageListener;
import com.jvmd.sfymARollPlugin.roll.RollHandler;
import com.jvmd.sfymARollPlugin.roll.RollProcessor;
import org.bukkit.plugin.java.JavaPlugin;

public final class SfymARollPlugin extends JavaPlugin {
    public static final String ROLL_CHANNEL = "sfym_a_roll:roll";

    private RollPluginMessageListener rollPluginMessageListener;

    @Override
    public void onEnable() {
        CooldownChecker cooldownChecker = new CooldownChecker(1200L);
        RollHandler rollHandler = new RollHandler(new RollProcessor());

        rollPluginMessageListener = new RollPluginMessageListener(
                rollHandler,
                cooldownChecker,
                new OnlinePlayerChecker(),
                cooldownChecker
        );

        getServer().getMessenger().registerIncomingPluginChannel(this, ROLL_CHANNEL, rollPluginMessageListener);
        getServer().getMessenger().registerOutgoingPluginChannel(this, ROLL_CHANNEL);
    }

    @Override
    public void onDisable() {
        if (rollPluginMessageListener != null) {
            getServer().getMessenger().unregisterIncomingPluginChannel(this, ROLL_CHANNEL, rollPluginMessageListener);
        }
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, ROLL_CHANNEL);
    }
}
