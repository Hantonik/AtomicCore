package hantonik.atomic.core.utils.helpers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Some helpful code for recipes. <p>
 *
 * @author Hantonik
 */
public final class RecipeHelper {
    private static RecipeManager MANAGER;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        MANAGER = event.getServerResources().getRecipeManager();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRecipesUpdated(RecipesUpdatedEvent event) {
        MANAGER = event.getRecipeManager();
    }

    public static RecipeManager getRecipeManager() {
        if (MANAGER.recipes instanceof ImmutableMap) {
            MANAGER.recipes = Maps.newHashMap(MANAGER.recipes);
            MANAGER.recipes.replaceAll((t, v) -> new HashMap<>(MANAGER.recipes.get(t)));
        }

        if (MANAGER.byName instanceof ImmutableMap)
            MANAGER.byName = new HashMap<>(MANAGER.byName);

        return MANAGER;
    }

    public static Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> getRecipes() {
        return getRecipeManager().recipes;
    }

    public static <C extends Container> Map<ResourceLocation, Recipe<C>> getRecipes(RecipeType<Recipe<C>> type) {
        return getRecipeManager().byType(type);
    }

    public static void addRecipe(Recipe<?> recipe) {
        getRecipeManager().recipes.computeIfAbsent(recipe.getType(), t -> Maps.newHashMap()).put(recipe.getId(), recipe);
    }

    public static void addRecipe(RecipeType<?> recipeType, Recipe<?> recipe) {
        getRecipeManager().recipes.computeIfAbsent(recipeType, t -> Maps.newHashMap()).put(recipe.getId(), recipe);
        getRecipeManager().byName.put(recipe.getId(), recipe);
    }
}
