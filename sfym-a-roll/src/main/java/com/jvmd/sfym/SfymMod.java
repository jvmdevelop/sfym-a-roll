package com.jvmd.sfym;

import com.jvmd.sfym.network.RollNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SfymMod.MODID)
public class SfymMod {
    public static final String MODID = "sfym_a_roll";

    public SfymMod(FMLJavaModLoadingContext context) {
        RollNetwork.register();
        MinecraftForge.EVENT_BUS.register(this);
    }
}
