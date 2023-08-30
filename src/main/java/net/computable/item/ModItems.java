package net.computable.item;

import net.computable.ModMain;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ModMain.MODID);

    public static final RegistryObject<Item> TRANSISTOR = basicItem("transistor");
    public static final RegistryObject<Item> BASIC_CHIP = basicItem("basic_chip");
    public static final RegistryObject<Item> ANALYSER = ITEMS.register("analyser", () -> new AnalyserItem(properties(1)));
    public static final RegistryObject<Item> CLOCK = basicItem("clock");
    public static final RegistryObject<Item> ALU = basicItem("alu");
    public static final RegistryObject<Item> CPU = ITEMS.register("cpu", () -> new CPUItem(properties(1), 1));
    public static final RegistryObject<Item> RAM = ITEMS.register("ram", () -> new RAMItem(properties(1), 1024));
    public static final RegistryObject<Item> EEPROM = ITEMS.register("eeprom", () -> new EEPROMItem(properties(1), 1024));

    private static Item.Properties properties() {
        return new Item.Properties();
    }

    private static Item.Properties properties(int stacksize) {
        return new Item.Properties().stacksTo(stacksize);
    }

    private static RegistryObject<Item> basicItem(String name) {
        return ITEMS.register(name, () -> new Item(properties()));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
