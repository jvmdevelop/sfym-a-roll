package com.jvmd.sfym.network;

import com.jvmd.sfym.SfymMod;
import com.jvmd.sfym.client.RollAnimationClient;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.event.EventNetworkChannel;

public final class RollNetwork {
    public static final ResourceLocation CHANNEL_ID = new ResourceLocation(SfymMod.MODID, "roll");
    private static final byte PAYLOAD_HANDSHAKE = 0;
    private static final byte PAYLOAD_ROLL = 1;
    private static final String PROTOCOL_VERSION = "1";
    private static EventNetworkChannel channel;

    private RollNetwork() {
    }

    public static void register() {
        channel = NetworkRegistry.ChannelBuilder
                .named(CHANNEL_ID)
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(version -> true)
                .serverAcceptedVersions(version -> true)
                .eventNetworkChannel();

        channel.addListener(RollNetwork::onServerPayload);
    }

    public static void sendRollRequest() {
        sendClientPayload(PAYLOAD_ROLL);
    }

    public static void sendHandshake() {
        sendClientPayload(PAYLOAD_HANDSHAKE);
    }

    public static void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        sendHandshake();
    }

    private static void sendClientPayload(byte payloadType) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getConnection() == null || minecraft.player == null) {
            return;
        }

        FriendlyByteBuf payload = new FriendlyByteBuf(Unpooled.buffer());
        payload.writeByte(payloadType);
        minecraft.getConnection().send(new ServerboundCustomPayloadPacket(CHANNEL_ID, payload));
    }

    private static void onServerPayload(NetworkEvent.ServerCustomPayloadEvent event) {
        FriendlyByteBuf payload = event.getPayload();
        boolean accepted = payload != null && payload.isReadable() && payload.readBoolean();
        event.getSource().get().enqueueWork(() -> {
            if (accepted) {
                RollAnimationClient.playLocalRoll();
            }
        });
        event.getSource().get().setPacketHandled(true);
    }
}
