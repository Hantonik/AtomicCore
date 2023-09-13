package hantonik.atomic.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AtomicInventoryBlockEntity extends AtomicBlockEntity implements MenuProvider, IAtomicInventoryBlockEntity {
    public AtomicInventoryBlockEntity(BlockEntityType<? extends AtomicInventoryBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public abstract SimpleContainer getInventory();

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.getInventory().fromTag(tag.getList("Inventory", 0));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.put("Inventory", this.getInventory().createTag());
    }


}
