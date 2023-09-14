package hantonik.atomic.core.event

import hantonik.atomic.core.util.helper.ACRecipeHelper
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraftforge.eventbus.api.Event

open class RegisterRecipesEvent(val recipeManager: RecipeManager) : Event() {
    open fun register(recipe: Recipe<*>) {
        ACRecipeHelper.addRecipe(recipe)
    }
}