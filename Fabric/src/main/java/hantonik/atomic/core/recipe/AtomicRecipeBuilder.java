package hantonik.atomic.core.recipe;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Map;

@RequiredArgsConstructor
public class AtomicRecipeBuilder<T extends AtomicRecipeBuilder<T> & Recipe<?>> {
    protected final ResourceLocation serializerName;
    protected final Map<String, Criterion<?>> criteria = Maps.newLinkedHashMap();

    @SuppressWarnings("unchecked")
    public T addCriterion(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);

        return (T) this;
    }

    public void build(RecipeOutput output, ResourceLocation id) {
        if (this.criteria.isEmpty())
            throw new IllegalStateException("No way of obtaining recipe " + id);

        var advancementBuilder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
        output.accept(id, (Recipe<?>) this, advancementBuilder.build(id.withPrefix("recipes/")));
    }
}
