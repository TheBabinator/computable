package net.computable.item;

import net.computable.block.util.IAnalyserBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AnalyserItem extends Item {
    public AnalyserItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("gui.computable.analyser.description").withStyle(ChatFormatting.GRAY));
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
