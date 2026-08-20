package com.jvmd.sfym.handler;

import com.jvmd.sfym.SfymMod;
import com.jvmd.sfym.network.RollNetwork;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SfymMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InputHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        while (KeyBindings.ROLL.consumeClick()) {
            RollNetwork.sendRollRequest();
        }
    }
}
