package hantonik.atomic.core.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AtomicRecipeBuilder<T extends AtomicRecipeBuilder<T>> {
    protected final ResourceLocation serializerName;

    protected final List<ICondition> conditions = new ArrayList<>();
    protected final Advancement.Builder advancementBuilder = Advancement.Builder.advancement();

    @SuppressWarnings("unchecked")
    public T addCriterion(String name, CriterionTriggerInstance criterion) {
        this.advancementBuilder.addCriterion(name, criterion);

        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T addCondition(ICondition condition) {
        this.conditions.add(condition);

        return (T) this;
    }

    protected abstract RecipeResult getResult(ResourceLocation id);

    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        if (this.advancementBuilder.getCriteria().isEmpty())
            throw new IllegalStateException("No way of obtaining recipe " + id);

        this.advancementBuilder.parent(new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", new RecipeUnlockedTrigger.TriggerInstance(EntityPredicate.Composite.ANY, id))
                .rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
        consumer.accept(getResult(id));
    }

    protected abstract class RecipeResult implements FinishedRecipe {
        @Getter
        private final ResourceLocation id;
        @Getter
        private final ResourceLocation advancementId;

        protected RecipeResult(ResourceLocation id) {
            this.id = id;
            this.advancementId = new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath());
        }

        @Override
        public JsonObject serializeRecipe() {
            var jsonObject = new JsonObject();

            jsonObject.addProperty("type", serializerName.toString());

            if (!conditions.isEmpty()) {
                var conditionsArray = new JsonArray();

                for (var condition : conditions)
                    conditionsArray.add(CraftingHelper.serialize(condition));

                jsonObject.add("conditions", conditionsArray);
            }

            this.serializeRecipeData(jsonObject);

            return jsonObject;
        }

        @Nonnull
        @Override
        public RecipeSerializer<?> getType() {
            return Objects.requireNonNull(ForgeRegistries.RECIPE_SERIALIZERS.getValue(serializerName));
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return advancementBuilder.serializeToJson();
        }
    }
}
