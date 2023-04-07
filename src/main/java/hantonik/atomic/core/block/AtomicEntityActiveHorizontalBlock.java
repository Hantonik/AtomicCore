package hantonik.atomic.core.block;

import hantonik.atomic.core.utils.AtomicStateProperties;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;

import java.util.function.Function;
import java.util.function.Supplier;

public class AtomicEntityActiveHorizontalBlock extends AtomicEntityHorizontalBlock {
    public AtomicEntityActiveHorizontalBlock(Material material, Function<Properties, Properties> properties, Class<?> entityClass, Supplier<BlockEntityType<?>> blockEntityType) {
        super(material, properties, entityClass, blockEntityType);

        this.registerDefaultState(this.stateDefinition.any().setValue(AtomicStateProperties.ACTIVE, false));
    }
}
