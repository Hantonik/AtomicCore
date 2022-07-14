package hantonik.atomiccore.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DirectionUtils {
    public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity) {
        return Direction.getNearest(
                (float) (entity.getX() - clickedBlock.getX()),
                (float) (entity.getY() - clickedBlock.getY()),
                (float) (entity.getZ() - clickedBlock.getZ()));
    }
}
