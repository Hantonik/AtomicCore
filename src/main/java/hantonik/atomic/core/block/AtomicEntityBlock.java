package hantonik.atomic.core.block;

import hantonik.atomic.core.block.entity.AtomicBlockEntity;
import hantonik.atomic.core.block.entity.IBlockEntityCallback;
import hantonik.atomic.core.block.entity.ITickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public class AtomicEntityBlock extends AtomicBlock implements EntityBlock {
    protected final Supplier<BlockEntityType<?>> blockEntityType;
    protected final Class<?> entityClass;

    public AtomicEntityBlock(Material material, Function<Properties, Properties> properties, Class<?> entityClass, Supplier<BlockEntityType<?>> blockEntityType) {
        super(material, properties);

        this.blockEntityType = blockEntityType;
        this.entityClass = entityClass;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return this.blockEntityType.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return ITickableBlockEntity.createTicker(level, type, this.blockEntityType.get(), this.entityClass);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.getBlockEntity(pos) instanceof AtomicBlockEntity atomicEntity)
            atomicEntity.neighborChanged(block, fromPos);

        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof AtomicBlockEntity atomicEntity)
                atomicEntity.onPlacedBy(level, pos, state, placer, stack);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof AtomicBlockEntity atomicEntity)
                atomicEntity.onReplaced(state, level, pos, newState);

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof IBlockEntityCallback callback)
            return callback.getComparatorInput();

        else
            return super.getAnalogOutputSignal(state, level, pos);
    }
}
