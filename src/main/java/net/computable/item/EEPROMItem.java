package net.computable.item;

import net.computable.ModMain;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public class EEPROMItem extends StorageItem {
    public EEPROMItem(Properties pProperties, int pCapacity) {
        super(pProperties, pCapacity);
    }

    @Override
    protected void onCreated(ItemStack stack, CompoundTag tag) {
        super.onCreated(stack, tag);
        try {
            File file = getPath(tag).toFile();
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (Exception e) {
            ModMain.LOGGER.debug(e.toString());
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("gui.computable.eeprom.description").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
