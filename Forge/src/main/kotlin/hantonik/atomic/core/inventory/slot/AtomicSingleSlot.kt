package hantonik.atomic.core.inventory.slot

import net.minecraft.world.item.ItemStack
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.SlotItemHandler

open class AtomicSingleSlot(inventory: IItemHandler, id: Int, x: Int, y: Int) : SlotItemHandler(inventory, id, x, y) {
    override fun getMaxStackSize(stack: ItemStack) = 1

    override fun getMaxStackSize() = 1
}