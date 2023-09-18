package hantonik.atomic.core.inventory.slot

import net.minecraft.world.item.ItemStack
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.SlotItemHandler

open class AtomicOutputSlot(inventory: IItemHandler, id: Int, x: Int, y: Int) : SlotItemHandler(inventory, id, x, y) {
    override fun mayPlace(stack: ItemStack) = false
}