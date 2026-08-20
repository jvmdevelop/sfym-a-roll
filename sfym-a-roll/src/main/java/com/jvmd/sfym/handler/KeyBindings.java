package com.jvmd.sfym.handler;

import com.jvmd.sfym.SfymMod;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = SfymMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class KeyBindings {
    public static final KeyMapping ROLL = new KeyMapping(
            "key." + SfymMod.MODID + ".roll",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "key.categories." + SfymMod.MODID
    );

    private KeyBindings() {
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ROLL);
    }
}
