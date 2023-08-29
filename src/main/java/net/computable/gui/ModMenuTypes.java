package net.computable.gui;

import net.computable.ModMain;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ModMain.MODID);

    public static final RegistryObject<MenuType<CaseMenu>> CASE = MENUS.register("case",
            () -> IForgeMenuType.create(CaseMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}