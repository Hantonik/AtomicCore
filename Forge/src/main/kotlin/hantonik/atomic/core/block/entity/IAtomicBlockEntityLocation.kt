package hantonik.atomic.core.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

interface IAtomicBlockEntityLocation {
    fun state(): BlockState

    fun pos(): BlockPos

    fun level(): Level?
}