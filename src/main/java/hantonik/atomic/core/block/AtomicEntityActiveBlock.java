package hantonik.atomic.core.block;

import hantonik.atomic.core.utils.AtomicStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;

import java.util.function.Function;
import java.util.function.Supplier;

public class AtomicEntityActiveBlock extends AtomicEntityBlock {
    public AtomicEntityActiveBlock(Material material, Function<Properties, Properties> properties, Class<?> entityClass, Supplier<BlockEntityType<?>> blockEntityType) {
        super(material, properties, entityClass, blockEntityType);

        this.registerDefaultState(this.getStateDefinition().any().setValue(AtomicStateProperties.ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);

        builder.add(AtomicStateProperties.ACTIVE);
    }
}
