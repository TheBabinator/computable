package net.computable.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class DataItem extends Item {
    public DataItem(Properties pProperties) {
        super(pProperties);
    }

    protected void initTag(CompoundTag tag, boolean preview) {
        if (!preview) {
            tag.putUUID("address", UUID.randomUUID());
        }
    }

    public CompoundTag getTag(ItemStack stack) {
        if (!stack.hasTag()) {
            CompoundTag tag = new CompoundTag();
            initTag(tag, true);
            return tag;
        }
        return stack.getTag();
    }

    public void assertTag(ItemStack stack) {
        if (!stack.hasTag()) {
            CompoundTag tag = new CompoundTag();
            initTag(tag, false);
            stack.setTag(tag);
            onCreated(stack, tag);
        }
    }

    protected void onCreated(ItemStack stack, CompoundTag tag) {

    }

    public Path getPath(CompoundTag tag) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        Path root = server.getWorldPath(LevelResource.ROOT); // AAAH
        return Paths.get(root.toString(), "computable", tag.getUUID("address").toString());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide()) {
            assertTag(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (getTag(pStack).get("address") instanceof IntArrayTag tag) {
            UUID address = NbtUtils.loadUUID(tag);
            pTooltipComponents.add(Component.translatable("gui.computable.address", address).withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
