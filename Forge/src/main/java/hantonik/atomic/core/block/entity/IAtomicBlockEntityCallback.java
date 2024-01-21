package hantonik.atomic.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface IAtomicBlockEntityCallback {
    default int getAnalogOutputSignal() {
        return 0;
    }

    default void neighborChanged(Level level, BlockPos pos, BlockState state, Block block, BlockPos fromPos, boolean movedByPiston) {}

    default void callNeighborChangeState(Level level, BlockPos pos, BlockState state) {
        level.updateNeighborsAt(pos, state.getBlock());
    }

    default void onPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {}

    default void onReplaced(Level level, BlockPos pos, BlockState state, BlockState newState, boolean movedByPiston) {}
}
