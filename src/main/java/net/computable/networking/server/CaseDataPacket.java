package net.computable.networking.server;

import net.computable.block.entity.CaseBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CaseDataPacket {
    private final BlockPos pos;
    private final boolean powered;
    private final boolean indicator;

    public CaseDataPacket(CaseBlockEntity pEntity) {
        pos = pEntity.getBlockPos();
        powered = pEntity.powered;
        indicator = pEntity.indicator;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(powered);
        buf.writeBoolean(indicator);
    }

    public CaseDataPacket(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        powered = buf.readBoolean();
        indicator = buf.readBoolean();
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof CaseBlockEntity caseBlockEntity) {
                caseBlockEntity.powered = powered;
                caseBlockEntity.indicator = indicator;
            }
        });
    }
}
