package hantonik.atomic.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface IAtomicBlockEntityCallback extends IAtomicBlockEntityLocation {
    default int getComparatorInput() {
        return 0;
    }

    default void neighborChanged(Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {

    }

    default void callNeighborStateChange() {
        if (this.level() == null)
            return;

        this.level().updateNeighborsAt(this.pos(), this.state().getBlock());
    }

    default void onPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {

    }

    default void onReplaced(BlockState state, Level level, BlockPos pos, BlockState newState) {

    }
}
