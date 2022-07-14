package hantonik.atomiccore.api.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface IBlockEntityLocation {
    Block block();

    BlockState state();

    BlockPos pos();

    Level level();
}
