package net.computable.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CPUItem extends DataItem {
    public int clockspeed;

    public CPUItem(Properties pProperties, int pClockspeed) {
        super(pProperties);
        clockspeed = pClockspeed;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("gui.computable.cpu.description").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("gui.computable.cpu.clockspeed", clockspeed * 20).withStyle(ChatFormatting.DARK_GRAY));
    }
}
