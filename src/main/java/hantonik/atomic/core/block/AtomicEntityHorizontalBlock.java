package hantonik.atomic.core.block;

import hantonik.atomic.core.utils.AtomicStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

public class AtomicEntityHorizontalBlock extends AtomicEntityBlock {
    public AtomicEntityHorizontalBlock(Material material, Function<Properties, Properties> properties, Class<?> entityClass, Supplier<BlockEntityType<?>> blockEntityType) {
        super(material, properties, entityClass, blockEntityType);

        this.registerDefaultState(this.stateDefinition.any().setValue(AtomicStateProperties.FACING_HORIZONTAL, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);

        builder.add(AtomicStateProperties.FACING_HORIZONTAL);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(AtomicStateProperties.FACING_HORIZONTAL, context.getPlayer() != null ? context.getPlayer().getMotionDirection().getOpposite() : Direction.NORTH);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(AtomicStateProperties.FACING_HORIZONTAL, rotation.rotate(state.getValue(AtomicStateProperties.FACING_HORIZONTAL)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(AtomicStateProperties.FACING_HORIZONTAL)));
    }
}
