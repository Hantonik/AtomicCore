package hantonik.atomic.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IAtomicBlockEntityLocation {
    BlockState state();

    BlockPos pos();

    Level level();
}
