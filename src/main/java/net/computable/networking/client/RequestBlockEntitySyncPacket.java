package net.computable.networking.client;

import net.computable.block.util.ISyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestBlockEntitySyncPacket {
    private final BlockPos pos;

    public RequestBlockEntitySyncPacket(BlockPos pPos) {
        pos = pPos;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public RequestBlockEntitySyncPacket(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof ISyncedBlockEntity syncedBlockEntity) {
                syncedBlockEntity.sync(player);
            }
        });
    }
}
