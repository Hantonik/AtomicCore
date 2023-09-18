package hantonik.atomic.core.recipe

import net.minecraft.core.NonNullList
import net.minecraft.core.RegistryAccess
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.Level
import net.minecraftforge.common.util.RecipeMatcher
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.wrapper.InvWrapper

interface IAtomicSpecialRecipe : Recipe<Container> {
    override fun assemble(container: Container, access: RegistryAccess) = this.assemble(InvWrapper(container), access)

    fun assemble(inventory: IItemHandler, access: RegistryAccess): ItemStack

    override fun matches(container: Container, level: Level) = this.matches(InvWrapper(container))

    fun matches(inventory: IItemHandler) = this.matches(inventory, 0, inventory.slots)

    fun matches(inventory: IItemHandler, startIndex: Int, endIndex: Int): Boolean {
        val inputs = NonNullList.create<ItemStack>()

        for (index in startIndex until endIndex)
            inputs.add(inventory.getStackInSlot(index))

        return RecipeMatcher.findMatches(inputs, this.ingredients) != null
    }

    override fun getRemainingItems(container: Container) = this.getRemainingItems(InvWrapper(container))

    fun getRemainingItems(inventory: IItemHandler): NonNullList<ItemStack> {
        val remaining = NonNullList.withSize(inventory.slots, ItemStack.EMPTY)

        for (index in 0 until remaining.size) {
            val stack = inventory.getStackInSlot(index)

            if (stack.hasCraftingRemainingItem())
                remaining[index] = stack.craftingRemainingItem
        }

        return remaining
    }
}