package hantonik.atomic.core.item

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType

open class AtomicBurnableItem(properties: (Properties) -> Properties, private val burnTime: Int) : AtomicItem(properties), IBurnableItem {
    constructor(burnTime: Int) : this({ it }, burnTime)

    override fun getBurnTime(itemStack: ItemStack, recipeType: RecipeType<*>?) = this.burnTime
}