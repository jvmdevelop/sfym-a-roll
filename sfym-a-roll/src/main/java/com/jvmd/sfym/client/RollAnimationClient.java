package com.jvmd.sfym.client;

import com.jvmd.sfym.SfymMod;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SfymMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class RollAnimationClient {
    private static final ResourceLocation LAYER_ID = new ResourceLocation(SfymMod.MODID, "roll_layer");
    private static final ResourceLocation ROLL_ANIMATION_ID = new ResourceLocation(SfymMod.MODID, "roll");

    private RollAnimationClient() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                LAYER_ID,
                1500,
                RollAnimationClient::createAnimationLayer
        ));
    }

    public static void playLocalRoll() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }

        Object layer = PlayerAnimationAccess.getPlayerAssociatedData(minecraft.player).get(LAYER_ID);
        if (layer instanceof ModifierLayer<?> modifierLayer) {
            @SuppressWarnings("unchecked")
            ModifierLayer<IAnimation> animationLayer = (ModifierLayer<IAnimation>) modifierLayer;
            animationLayer.setAnimation(new KeyframeAnimationPlayer(
                    PlayerAnimationRegistry.getAnimation(ROLL_ANIMATION_ID)
            ));
        }
    }

    private static IAnimation createAnimationLayer(AbstractClientPlayer player) {
        return new ModifierLayer<>();
    }
}
