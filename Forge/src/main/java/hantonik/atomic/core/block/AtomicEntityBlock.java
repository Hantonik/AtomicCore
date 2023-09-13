package hantonik.atomic.core.block;

import hantonik.atomic.core.block.entity.IAtomicBlockEntityCallback;
import hantonik.atomic.core.block.entity.IAtomicTickableBlockEntity;
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
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public class AtomicEntityBlock extends AtomicBlock implements EntityBlock {
    protected final Supplier<BlockEntityType<?>> blockEntityType;
    protected final Class<?> entityClass;

    public AtomicEntityBlock(Function<Properties, Properties> properties, Class<?> entityClass, Supplier<BlockEntityType<?>> blockEntityType) {
        super(properties);

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
        return IAtomicTickableBlockEntity.createTicker(type, this.blockEntityType.get(), this.entityClass);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level.getBlockEntity(pos) instanceof IAtomicBlockEntityCallback callback)
            callback.neighborChanged(neighborBlock, neighborPos, movedByPiston);

        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof IAtomicBlockEntityCallback callback)
                callback.onPlacedBy(level, pos, state, placer, stack);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof IAtomicBlockEntityCallback callback)
                callback.onReplaced(state, level, pos, newState);

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof IAtomicBlockEntityCallback callback)
            return callback.getComparatorInput();

        else
            return super.getAnalogOutputSignal(state, level, pos);
    }
}
