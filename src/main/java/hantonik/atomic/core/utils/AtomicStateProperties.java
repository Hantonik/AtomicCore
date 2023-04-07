package hantonik.atomic.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AtomicStateProperties {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final DirectionProperty FACING_HORIZONTAL = BlockStateProperties.HORIZONTAL_FACING;
}
