package hantonik.atomic.core.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

interface IAtomicBlockEntityCallback : IAtomicBlockEntityLocation {
    fun getComparatorInput(): Int = 0

    fun neighborChanged(block: Block, fromPos: BlockPos, movedByPiston: Boolean) {}

    fun callNeighborStateChange() {
        this.level() ?: return

        this.level()!!.updateNeighborsAt(this.pos(), this.state().block)
    }

    fun onPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {}

    fun onReplaced(state: BlockState, level: Level, pos: BlockPos, newState: BlockState) {}
}