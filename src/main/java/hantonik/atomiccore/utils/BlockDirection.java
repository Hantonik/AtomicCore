package hantonik.atomiccore.utils;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import java.util.Arrays;

public enum BlockDirection {
    NONE(),
    ANY(Direction.values()),
    ANY_FACE_PLAYER(Direction.values()),
    HORIZONTAL(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

    private final DirectionProperty property;

    public static final ImmutableBiMap<Direction, Integer> HORIZONTAL_DIRECTION_MAP = ImmutableBiMap.copyOf(Util.make(HashBiMap.create(), p -> {
        p.put(Direction.NORTH, 0);
        p.put(Direction.EAST, 1);
        p.put(Direction.SOUTH, 2);
        p.put(Direction.WEST, 3);
    }));

    BlockDirection(Direction... allowed) {
        this.property = DirectionProperty.create("direction", Arrays.asList(allowed));
    }

    public DirectionProperty getProperty() {
        return this.property;
    }

    public Direction getFrom(Direction facing, BlockPos pos, LivingEntity entity) {
        return switch (this) {
            case ANY -> facing.getOpposite();
            case ANY_FACE_PLAYER -> DirectionUtils.getFacingFromEntity(pos, entity);
            case HORIZONTAL -> entity.getDirection().getOpposite();

            default -> throw new IllegalStateException("Unknown direction type");
        };
    }

    public Direction getNext(Direction previous) {
        return switch (this) {
            case ANY, ANY_FACE_PLAYER -> previous.ordinal() + 1 >= Direction.values().length ? Direction.values()[0] : Direction.values()[previous.ordinal() + 1];
            case HORIZONTAL -> previous.getCounterClockWise();

            default -> throw new IllegalStateException("Unknown direction type");
        };
    }
}
