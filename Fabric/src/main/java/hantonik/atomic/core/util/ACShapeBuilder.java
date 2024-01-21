package hantonik.atomic.core.util;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ACShapeBuilder {
    private static final Map<BlockState, VoxelShape> SHAPES = Maps.newHashMap();

    private VoxelShape leftShape;
    private VoxelShape lastOrShape;

    public static VoxelShape getOrCreate(BlockState state, Function<BlockState, VoxelShape> shapeFactory) {
        return SHAPES.computeIfAbsent(state, shapeFactory);
    }

    public static ACShapeBuilder fromShapes(VoxelShape... shapes) {
        var builder = new ACShapeBuilder();

        for (var shape : shapes)
            builder.shape(shape);

        return builder;
    }

    public ACShapeBuilder shape(VoxelShape shape) {
        if (this.leftShape == null)
            this.leftShape = shape;
        else {
            var newShape = Shapes.or(this.leftShape, shape);

            if (this.lastOrShape != null)
                this.lastOrShape = Shapes.or(this.lastOrShape, newShape);
            else
                this.lastOrShape = newShape;

            this.leftShape = null;
        }

        return this;
    }

    public VoxelShape build() {
        return this.lastOrShape;
    }
}
