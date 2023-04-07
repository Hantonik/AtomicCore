package hantonik.atomic.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AtomicInventoryBlockEntity extends AtomicBlockEntity implements MenuProvider, IInventoryBlockEntity {
    public AtomicInventoryBlockEntity(BlockEntityType<? extends AtomicInventoryBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public abstract ItemStackHandler getInventory();

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.getInventory().deserializeNBT(tag.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.put("Inventory", this.getInventory().serializeNBT());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.isRemoved() && cap == ForgeCapabilities.ITEM_HANDLER)
            return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, LazyOptional.of(this::getInventory));

        return super.getCapability(cap, side);
    }
}
