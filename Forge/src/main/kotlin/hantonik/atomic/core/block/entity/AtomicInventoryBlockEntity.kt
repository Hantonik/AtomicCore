package hantonik.atomic.core.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.MenuProvider
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class AtomicInventoryBlockEntity(type: BlockEntityType<out AtomicInventoryBlockEntity>, pos: BlockPos, state: BlockState) : AtomicBlockEntity(type, pos, state), IAtomicInventoryBlockEntity, MenuProvider {
    override fun load(tag: CompoundTag) {
        super.load(tag)

        this.inventory.deserializeNBT(tag.getCompound("Inventory"))
    }

    override fun saveAdditional(tag: CompoundTag) {
        super.saveAdditional(tag)

        tag.put("Inventory", this.inventory.serializeNBT())
    }
}