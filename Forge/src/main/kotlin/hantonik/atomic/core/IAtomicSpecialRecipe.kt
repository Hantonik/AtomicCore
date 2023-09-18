package hantonik.atomic.core

import net.minecraft.core.RegistryAccess
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.wrapper.InvWrapper

interface IAtomicSpecialRecipe : Recipe<Container> {
    fun assemble(inventory: IItemHandler, access: RegistryAccess): ItemStack

    override fun assemble(container: Container, access: RegistryAccess): ItemStack {
        return this.assemble(InvWrapper(container), access)
    }
}