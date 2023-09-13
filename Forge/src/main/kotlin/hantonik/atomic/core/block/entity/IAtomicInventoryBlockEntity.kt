package hantonik.atomic.core.block.entity

import net.minecraftforge.items.ItemStackHandler

interface IAtomicInventoryBlockEntity {
    val inventory: ItemStackHandler
}