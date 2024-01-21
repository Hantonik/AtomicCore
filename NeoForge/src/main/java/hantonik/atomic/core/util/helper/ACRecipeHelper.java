package hantonik.atomic.core.util.helper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ACRecipeHelper {
    public static final ACRecipeHelper INSTANCE = new ACRecipeHelper();

    private static RecipeManager MANAGER;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onAddReloadListeners(final AddReloadListenerEvent event) {
        MANAGER = event.getServerResources().getRecipeManager();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRecipesUpdated(final RecipesUpdatedEvent event) {
        MANAGER = event.getRecipeManager();
    }

    public static RecipeManager getRecipeManager() {
        if (MANAGER.recipes instanceof ImmutableMap<?,?>) {
            MANAGER.recipes = Maps.newHashMap(MANAGER.recipes);
            MANAGER.recipes.replaceAll((type, recipes) -> Maps.newHashMap(MANAGER.recipes.get(type)));
        }

        if (MANAGER.byName instanceof ImmutableMap<?, ?>)
            MANAGER.byName = Maps.newHashMap(MANAGER.byName);

        return MANAGER;
    }

    public static Map<RecipeType<?>, Map<ResourceLocation, RecipeHolder<?>>> getRecipes() {
        return getRecipeManager().recipes;
    }

    public static <C extends Container, T extends Recipe<C>> Map<ResourceLocation, RecipeHolder<T>> getRecipes(RecipeType<T> type) {
        return getRecipeManager().byType(type);
    }

    public static void addRecipe(RecipeHolder<?> recipe) {
        getRecipeManager().recipes.computeIfAbsent(recipe.value().getType(), t -> Maps.newHashMap()).put(recipe.id(), recipe);
    }

    public static <C extends Container, T extends Recipe<C>> void addRecipe(RecipeType<T> type, RecipeHolder<T> recipe) {
        getRecipeManager().recipes.computeIfAbsent(type, t -> Maps.newHashMap()).put(recipe.id(), recipe);
        getRecipeManager().byName.put(recipe.id(), recipe);
    }
}
