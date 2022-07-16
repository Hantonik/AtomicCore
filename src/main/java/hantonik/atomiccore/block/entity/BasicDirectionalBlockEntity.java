package hantonik.atomiccore.block.entity;

import hantonik.atomiccore.utils.BlockDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public abstract class BasicDirectionalBlockEntity extends BasicBlockEntity {
    public BasicDirectionalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Nonnull
    protected Direction getDirection() {
        var state = this.getBlockState();

        if (!state.hasProperty(BlockDirection.HORIZONTAL.getProperty()))
            throw new IllegalStateException("Directional BlockEntity is not directional");

        return state.getValue(BlockDirection.HORIZONTAL.getProperty());
    }

    private Direction calculateDirection(Direction directionIn) {
        if (this.getDirection().getAxis() == Direction.Axis.Y || directionIn.getAxis() == Direction.Axis.Y)
            throw new IllegalStateException("Horizontal directional BlockEntity cannot be rotated in Y axis");

        int direction = BlockDirection.HORIZONTAL_DIRECTION_MAP.get(directionIn);

        direction += BlockDirection.HORIZONTAL_DIRECTION_MAP.get(this.getDirection());

        if (direction >= BlockDirection.HORIZONTAL_DIRECTION_MAP.size())
            direction -= 4;

        return BlockDirection.HORIZONTAL_DIRECTION_MAP.inverse().get(direction);
    }

    @NotNull
    @SuppressWarnings("unused")
    public <T> LazyOptional<T> getCapabilityDirectional(@NotNull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.empty();
    }

    @NotNull
    @Override
    public final <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        var capability = this.getCapabilityDirectional(cap, this.calculateDirection(side));

        return capability.isPresent() ? capability : super.getCapability(cap, side);
    }
}
