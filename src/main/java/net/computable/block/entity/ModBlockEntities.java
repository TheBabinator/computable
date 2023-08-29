package net.computable.block.entity;

import net.computable.ModMain;
import net.computable.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ModMain.MODID);

    public static final RegistryObject<BlockEntityType<CaseBlockEntity>> CASE = BLOCK_ENTITIES.register("case",
                () -> BlockEntityType.Builder.of(CaseBlockEntity::new, ModBlocks.CASE.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
