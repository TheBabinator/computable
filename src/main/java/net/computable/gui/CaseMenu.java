package net.computable.gui;

import net.computable.block.entity.CaseBlockEntity;
import net.computable.block.ModBlocks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class CaseMenu extends AbstractContainerMenu {
    public final CaseBlockEntity entity;
    private final Level level;
    public final ContainerData data;

    public CaseMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf pExtraData) {
        this(pContainerId, pPlayerInventory, pPlayerInventory.player.level.getBlockEntity(pExtraData.readBlockPos()), new SimpleContainerData(1));
    }

    public CaseMenu(int pContainerId, Inventory pPlayerInventory, BlockEntity pEntity, ContainerData pData) {
        super(ModMenuTypes.CASE.get(), pContainerId);
        entity = (CaseBlockEntity) pEntity;
        level = pPlayerInventory.player.level;
        data = pData;

        addPlayerInventory(pPlayerInventory);
        addPlayerHotbar(pPlayerInventory);

        entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            addSlot(new SlotItemHandler(handler, 0, 80, 17));
            addSlot(new SlotItemHandler(handler, 1, 80, 35));
            addSlot(new SlotItemHandler(handler, 2, 80, 53));
            addSlot(new SlotItemHandler(handler, 3, 98, 17));
            addSlot(new SlotItemHandler(handler, 4, 98, 35));
            addSlot(new SlotItemHandler(handler, 5, 98, 53));
            addSlot(new SlotItemHandler(handler, 6, 116, 17));
            addSlot(new SlotItemHandler(handler, 7, 116, 35));
            addSlot(new SlotItemHandler(handler, 8, 116, 53));
        });

        addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()), pPlayer, ModBlocks.CASE.get());
    }

    private void addPlayerInventory(Inventory pPlayerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 9; l++) {
                addSlot(new Slot(pPlayerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory pPlayerInventory) {
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(pPlayerInventory, i, 8 + i * 18, 142));
        }
    }
}
