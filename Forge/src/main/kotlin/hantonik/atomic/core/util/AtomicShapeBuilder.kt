package hantonik.atomic.core.util

import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class AtomicShapeBuilder {
    private var leftShape: VoxelShape? = null
    private var lastOrShape: VoxelShape? = null

    companion object {
        private val SHAPES: MutableMap<BlockState, VoxelShape> = hashMapOf()

        fun getOrCreate(state: BlockState, shapeFactory: (BlockState) -> VoxelShape): VoxelShape = SHAPES.computeIfAbsent(state, shapeFactory)

        fun fromShapes(vararg shapes: VoxelShape): AtomicShapeBuilder {
            val builder = AtomicShapeBuilder()

            for (shape in shapes)
                builder.shape(shape)

            return builder
        }
    }

    fun shape(shape: VoxelShape): AtomicShapeBuilder {
        if (this.leftShape == null)
            this.leftShape = shape
        else {
            val newShape = Shapes.or(this.leftShape!!, shape)

            if (this.lastOrShape != null)
                this.lastOrShape = Shapes.or(this.lastOrShape!!, newShape)
            else
                this.lastOrShape = newShape

            this.leftShape = null
        }

        return this
    }

    fun build(): VoxelShape = this.lastOrShape!!
}