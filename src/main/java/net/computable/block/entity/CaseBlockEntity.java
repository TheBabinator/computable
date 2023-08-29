package net.computable.block.entity;

import net.computable.block.util.EnergyHandler;
import net.computable.block.util.IAnalyserBlockEntity;
import net.computable.block.util.ISyncedBlockEntity;
import net.computable.computer.Computer;
import net.computable.gui.CaseMenu;
import net.computable.item.ModItems;
import net.computable.networking.ModMessages;
import net.computable.networking.client.RequestBlockEntitySyncPacket;
import net.computable.networking.server.CaseDataPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CaseBlockEntity extends BlockEntity implements MenuProvider, IAnalyserBlockEntity, ISyncedBlockEntity {
    private final ItemStackHandler itemHandler = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.getItem() == ModItems.EEPROM.get();
                case 3 -> stack.getItem() == ModItems.CPU.get();
                case 4, 5 -> stack.getItem() == ModItems.RAM.get();
                default -> false;
            };
        }
    };
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    protected final ContainerData containerData;
    private final EnergyHandler energyHandler = new EnergyHandler(16, 16, 0) {
        @Override
        public void onEnergyChanged() {
            setChanged();
        }
    };
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    public boolean powered = false;
    public boolean indicator = false;
    public Computer computer;

    public CaseBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CASE.get(), pPos, pBlockState);
        containerData = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> powered ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> powered = pValue == 1;
                }
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        } else if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> energyHandler);
        if (level.isClientSide()) {
            requestSync();
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.put("energy", energyHandler.serializeNBT());
        pTag.putBoolean("powered", powered);
        pTag.putBoolean("indicator", indicator);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        energyHandler.deserializeNBT(pTag.get("energy"));
        powered = pTag.getBoolean("powered");
        indicator = pTag.getBoolean("indicator");
        super.load(pTag);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(level, worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("blockentity.computable.case");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CaseMenu(pContainerId, pPlayerInventory, this, containerData);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, CaseBlockEntity pEntity) {
        if (pLevel.isClientSide()) return;
        if (pEntity.powered && pEntity.computer != null) {
            if (pEntity.computer.halted) {
                pEntity.powerOff();
            } else {
                pEntity.computer.tick();
                if (pEntity.computer.indicator != pEntity.indicator) {
                    pEntity.indicator = pEntity.computer.indicator;
                    pEntity.sync();
                }
            }
            pEntity.setChanged();
        }
    }

    @Override
    public void sync() {
        ModMessages.sendToPlayersInLevel(new CaseDataPacket(this), level);
    }

    @Override
    public void sync(ServerPlayer player) {
        ModMessages.sendToPlayer(new CaseDataPacket(this), player);
    }

    @Override
    public void requestSync() {
        ModMessages.sendToServer(new RequestBlockEntitySyncPacket(getBlockPos()));
    }

    public void powerOn() {
        if (energyHandler.getEnergyStored() == 0) return;
        powered = true;
        indicator = false;
        computer = new Computer(energyHandler);
        sync();
        setChanged();
    }

    public void powerOff() {
        powered = false;
        indicator = false;
        computer.kill();
        sync();
        setChanged();
    }

    public void onPowerButton() {
        if (powered) {
            powerOff();
        } else {
            powerOn();
        }
    }

    @Override
    public Component getAnalyserText() {
        return Component.translatable("blockentity.computable.case.analyser",
                powered,
                indicator,
                String.format("%.1f/%.1f FE", energyHandler.getActualEnergyStored(), energyHandler.getActualMaxEnergyStored())
        );
    }
}
