package net.computable.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class StorageItem extends DataItem {
    public int capacity;

    public StorageItem(Properties pProperties, int pCapacity) {
        super(pProperties);
        capacity = pCapacity;
    }

    @Override
    protected void initTag(CompoundTag tag, boolean preview) {
        super.initTag(tag, preview);
        tag.putInt("usage", 0);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (getTag(pStack).get("usage") instanceof IntTag usageIntTag) {
            int usage = usageIntTag.getAsInt();
            String string = String.format("%d/%d B", usage, capacity);
            pTooltipComponents.add(Component.translatable("gui.computable.storage", string).withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
