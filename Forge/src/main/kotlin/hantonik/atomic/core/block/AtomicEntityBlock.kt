package hantonik.atomic.core.block

import hantonik.atomic.core.block.entity.IAtomicBlockEntityCallback
import hantonik.atomic.core.block.entity.IAtomicTickableBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

open class AtomicEntityBlock(properties: (Properties) -> Properties, protected val entityClass: Class<*>, protected val blockEntityType: () -> BlockEntityType<*>) : AtomicBlock(properties), EntityBlock {
    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? = this.blockEntityType.invoke().create(pos, state)

    override fun <T : BlockEntity> getTicker(level: Level, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? = IAtomicTickableBlockEntity.createTicker(type, this.blockEntityType.invoke(), this.entityClass)

    override fun neighborChanged(state: BlockState, level: Level, pos: BlockPos, neighborBlock: Block, neighborPos: BlockPos, movedByPiston: Boolean) {
        val entity = level.getBlockEntity(pos)

        if (entity is IAtomicBlockEntityCallback)
            entity.neighborChanged(neighborBlock, neighborPos, movedByPiston)

        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston)
    }

    override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        if (!level.isClientSide) {
            val entity = level.getBlockEntity(pos)

            if (entity is IAtomicBlockEntityCallback)
                entity.onPlacedBy(level, pos, state, placer, stack)
        }
    }

    override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, movedByPiston: Boolean) {
        if (state.block != newState.block) {
            val entity = level.getBlockEntity(pos)

            if (entity is IAtomicBlockEntityCallback)
                entity.onReplaced(state, level, pos, newState)

            super.onRemove(state, level, pos, newState, movedByPiston)
        }
    }

    override fun getAnalogOutputSignal(state: BlockState, level: Level, pos: BlockPos): Int {
        val entity = level.getBlockEntity(pos)

        return if (entity is IAtomicBlockEntityCallback)
            entity.getComparatorInput()
        else
            super.getAnalogOutputSignal(state, level, pos)
    }
}