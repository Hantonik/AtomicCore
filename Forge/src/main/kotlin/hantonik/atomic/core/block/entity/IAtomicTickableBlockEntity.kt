package hantonik.atomic.core.block.entity

import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType

interface IAtomicTickableBlockEntity {
    fun tick()

    companion object {
        fun <A : BlockEntity> createTicker(type: BlockEntityType<A>, expectedType: BlockEntityType<*>, clazz: Class<*>): BlockEntityTicker<A>? {
            if (type == expectedType)
                if (IAtomicTickableBlockEntity::class.java.isAssignableFrom(clazz))
                    return BlockEntityTicker { _, _, _, entity -> (entity as IAtomicTickableBlockEntity).tick() }

            return null
        }
    }
}