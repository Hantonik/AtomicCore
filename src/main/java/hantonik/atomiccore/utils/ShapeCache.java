package hantonik.atomiccore.utils;

import com.google.common.collect.Maps;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;
import java.util.function.Function;

public final class ShapeCache {
    private static final Map<BlockState, VoxelShape> CACHE = Maps.newHashMap();

    private VoxelShape leftShape;
    private VoxelShape lastOrShape;

    public static VoxelShape getOrCreate(BlockState state, Function<BlockState, VoxelShape> shapeFactory) {
        return CACHE.computeIfAbsent(state, shapeFactory);
    }

    public static ShapeCache fromShapes(VoxelShape... shapes) {
        var builder = new ShapeCache();

        for (var shape : shapes)
            builder.shape(shape);

        return builder;
    }

    public ShapeCache shape(VoxelShape shape) {
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

    public ShapeCache cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
        var shape = Block.box(x1, y1, z1, x2, y2, z2);

        return this.shape(shape);
    }

    public VoxelShape build() {
        return this.lastOrShape;
    }
}
