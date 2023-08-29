package net.computable.item;

import net.computable.block.util.IAnalyserBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AnalyserItem extends Item {
    public AnalyserItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        if (!level.isClientSide()) {
            Player player = pContext.getPlayer();
            BlockPos pos = pContext.getClickedPos();
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof IAnalyserBlockEntity analyser) {
                player.sendSystemMessage(
                        Component.translatable("gui.computable.analyser.a")
                                .append("\n")
                                .append(analyser.getAnalyserText())
                                .append("\n")
                                .append(Component.translatable("gui.computable.analyser.b"))
                );
            }
        }

        return super.useOn(pContext);
    }
}
