package hantonik.atomic.core.block.entity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public interface IAtomicTickableBlockEntity {
    default void tick() {}

    @OnlyIn(Dist.DEDICATED_SERVER)
    default void serverTick() {}

    @OnlyIn(Dist.CLIENT)
    default void clientTick() {}

    @Nullable
    static <T extends BlockEntity> BlockEntityTicker<T> createTicker(BlockEntityType<T> type, BlockEntityType<?> expectedType, Class<?> entityClass) {
        if (type == expectedType) {
            if (IAtomicTickableBlockEntity.class.isAssignableFrom(entityClass)) {
                return (level, pos, state, entity) -> {
                    ((IAtomicTickableBlockEntity) entity).tick();

                    if (level.isClientSide)
                        ((IAtomicTickableBlockEntity) entity).clientTick();
                    else
                        ((IAtomicTickableBlockEntity) entity).serverTick();
                };
            }
        }

        return null;
    }
}
