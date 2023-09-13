package hantonik.atomic.core.block.entity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface IAtomicTickableBlockEntity {
    void tick();

    static <A extends BlockEntity> BlockEntityTicker<A> createTicker(BlockEntityType<A> type, BlockEntityType<?> expectedType, Class<?> clazz) {
        if (type == expectedType)
            if (IAtomicTickableBlockEntity.class.isAssignableFrom(clazz))
                return (level, pos, state, entity) -> ((IAtomicTickableBlockEntity) entity).tick();

        return null;
    }
}
