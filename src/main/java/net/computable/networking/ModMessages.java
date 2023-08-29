package net.computable.networking;

import net.computable.ModMain;
import net.computable.networking.client.CaseButtonPacket;
import net.computable.networking.client.RequestBlockEntitySyncPacket;
import net.computable.networking.server.CaseDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ModMain.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE.messageBuilder(RequestBlockEntitySyncPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RequestBlockEntitySyncPacket::new)
                .encoder(RequestBlockEntitySyncPacket::toBytes)
                .consumerMainThread(RequestBlockEntitySyncPacket::handle)
                .add();

        INSTANCE.messageBuilder(CaseButtonPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CaseButtonPacket::new)
                .encoder(CaseButtonPacket::toBytes)
                .consumerMainThread(CaseButtonPacket::handle)
                .add();

        INSTANCE.messageBuilder(CaseDataPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(CaseDataPacket::new)
                .encoder(CaseDataPacket::toBytes)
                .consumerMainThread(CaseDataPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToPlayers(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <MSG> void sendToPlayersInLevel(MSG message, Level level) {
        level.players().forEach(player -> {
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
        });
    }
}
