package hantonik.atomic.core.block.entity;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface ITickableBlockEntity {
    void tick();

    static <A extends BlockEntity> BlockEntityTicker<A> createTicker(Level level, BlockEntityType<A> type, BlockEntityType<?> expectedType, Class<?> clazz) {
        if (type == expectedType) {
            if (ITickableBlockEntity.class.isAssignableFrom(clazz))
                return (level1, pos, state, instance) -> ((ITickableBlockEntity) instance).tick();

            if (level.isClientSide && IClientTickable.class.isAssignableFrom(clazz))
                return (level1, pos, state, instance) -> ((IClientTickable) instance).tickClient();

            if (!level.isClientSide && IServerTickable.class.isAssignableFrom(clazz))
                return (level1, pos, state, instance) -> ((IServerTickable) instance).tickServer();
        }

        return null;
    }

    interface IServerTickable {
        void tickServer();
    }

    interface IClientTickable {
        void tickClient();
    }
}
