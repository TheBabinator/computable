package net.computable.networking.client;

import net.computable.block.entity.CaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CaseButtonPacket {
    private final BlockPos pos;

    public CaseButtonPacket(BlockPos pPos) {
        pos = pPos;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public CaseButtonPacket(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof CaseBlockEntity caseBlockEntity) {
                caseBlockEntity.onPowerButton();
            }
        });
    }
}
