package net.computable.computer;

import net.computable.ModMain;
import net.computable.block.util.EnergyHandler;
import net.computable.item.DataItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class Computer {
    private final ItemStackHandler itemHandler;
    private final EnergyHandler energyHandler;
    public boolean indicator = false;
    public boolean halted = false;

    public Computer(ItemStackHandler pItemHandler, EnergyHandler pEnergyHandler) {
        ModMain.LOGGER.debug("computer created!");
        itemHandler = pItemHandler;
        energyHandler = pEnergyHandler;
    }

    public void tick() {
        checkItems();
        if (Math.random() < 0.02) indicator = true;
        else if (Math.random() < 0.1) indicator = false;
        float drain = 0.2f;
        float consumed = energyHandler.consumeEnergy(drain, false);
        if (consumed != drain) {
            ModMain.LOGGER.debug("computer ran out of energy");
            halted = true;
        }
    }

    public void kill() {
        ModMain.LOGGER.debug("computer killed!");
    }

    private void checkItems() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                DataItem item = (DataItem) stack.getItem();
                item.assertTag(stack);
            }
        }
    }
}
