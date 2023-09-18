package hantonik.atomic.core.item

import net.minecraft.world.item.ItemStack

open class AtomicFoilItem(properties: (Properties) -> Properties) : AtomicItem(properties) {
    constructor() : this({ it })

    override fun isFoil(stack: ItemStack) = true
}