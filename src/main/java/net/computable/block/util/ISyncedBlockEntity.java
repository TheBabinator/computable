package net.computable.block.util;

import net.minecraft.server.level.ServerPlayer;

public interface ISyncedBlockEntity {
    void sync();

    void sync(ServerPlayer player);

    void requestSync();
}
