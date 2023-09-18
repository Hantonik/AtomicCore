package hantonik.atomic.core.item

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraftforge.common.extensions.IForgeItem

interface IBurnableItem : IForgeItem {
    override fun getBurnTime(itemStack: ItemStack, recipeType: RecipeType<*>?): Int
}