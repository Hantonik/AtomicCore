package hantonik.atomic.core.util.helper

import com.google.common.collect.ImmutableMap
import com.google.common.collect.Maps
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraftforge.client.event.RecipesUpdatedEvent
import net.minecraftforge.event.AddReloadListenerEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent

object ACRecipeHelper {
    private var MANAGER: RecipeManager? = null

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onAddReloadListeners(event: AddReloadListenerEvent) {
        MANAGER = event.serverResources.recipeManager
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onRecipesUpdated(event: RecipesUpdatedEvent) {
        MANAGER = event.recipeManager
    }

    fun getRecipeManager(): RecipeManager? {
        if (MANAGER?.recipes is ImmutableMap) {
            MANAGER?.recipes = Maps.newHashMap(MANAGER?.recipes!!)
            MANAGER?.recipes!!.replaceAll { t, _ -> Maps.newHashMap(MANAGER?.recipes!![t]!!) }
        }

        if (MANAGER?.byName is ImmutableMap)
            MANAGER?.byName = Maps.newHashMap(MANAGER?.byName!!)

        return MANAGER
    }

    fun getRecipes(): Map<RecipeType<*>, Map<ResourceLocation, Recipe<*>>> = this.getRecipeManager()?.recipes!!

    fun <C : Container> getRecipes(type: RecipeType<Recipe<C>>): Map<ResourceLocation, Recipe<C>> = this.getRecipeManager()?.byType(type)!!

    fun addRecipe(recipe: Recipe<*>) {
        this.getRecipeManager()?.recipes!!.computeIfAbsent(recipe.type) { Maps.newHashMap() }[recipe.id] = recipe
    }

    fun <C : Container> addRecipe(type: RecipeType<Recipe<C>>, recipe: Recipe<C>) {
        this.getRecipeManager()?.recipes!!.computeIfAbsent(type) { Maps.newHashMap() }[recipe.id] = recipe
        this.getRecipeManager()?.byName!![recipe.id] = recipe
    }
}