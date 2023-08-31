package net.computable.item;

import net.computable.block.ModBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTab {
    public static final CreativeModeTab TAB = new CreativeModeTab("computable") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.CPU.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> pItems) {
            addItem(pItems, ModItems.TRANSISTOR);
            addItem(pItems, ModItems.BASIC_CHIP);
            addItem(pItems, ModItems.ANALYSER);
            addItem(pItems, ModItems.CLOCK);
            addItem(pItems, ModItems.ALU);
            addItem(pItems, ModItems.CPU);
            addItem(pItems, ModItems.RAM);
            addItem(pItems, ModItems.EEPROM);
            addItem(pItems, ModItems.DRIVE);
            addBlock(pItems, ModBlocks.CASE);
        }
    };

    private static void addItem(NonNullList<ItemStack> items, RegistryObject<Item> item) {
        items.add(new ItemStack(item.get()));
    }

    private static void addBlock(NonNullList<ItemStack> items, RegistryObject<Block> block) {
        items.add(new ItemStack(block.get().asItem()));
    }
}
