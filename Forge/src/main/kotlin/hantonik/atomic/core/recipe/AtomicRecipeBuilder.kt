package hantonik.atomic.core.recipe

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.CriterionTriggerInstance
import net.minecraft.advancements.RequirementsStrategy
import net.minecraft.advancements.critereon.ContextAwarePredicate
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.common.crafting.conditions.ICondition
import net.minecraftforge.registries.ForgeRegistries

abstract class AtomicRecipeBuilder<T : AtomicRecipeBuilder<T>>(protected val serializerName: ResourceLocation) {
    protected val conditions = mutableListOf<ICondition>()
    protected val advancementBuilder: Advancement.Builder = Advancement.Builder.advancement()

    @Suppress("UNCHECKED_CAST")
    fun addCriterion(name: String, criterion: CriterionTriggerInstance): T {
        this.advancementBuilder.addCriterion(name, criterion)

        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    fun addCriterion(condition: ICondition): T {
        this.conditions.add(condition)

        return this as T
    }

    protected abstract fun getResult(id: ResourceLocation): RecipeResult

    fun build(builder: (FinishedRecipe) -> Unit, id: ResourceLocation) {
        if (this.advancementBuilder.criteria.isEmpty())
            throw IllegalStateException("No way of obtaining recipe $id")

        this.advancementBuilder.parent(ResourceLocation("recipes/root"))
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.TriggerInstance(ContextAwarePredicate.ANY, id))
            .rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR)

        builder.invoke(this.getResult(id))
    }

    protected abstract inner class RecipeResult(private val id: ResourceLocation) : FinishedRecipe {
        private val advancementId: ResourceLocation = ResourceLocation(this.id.namespace, "recipes/${this.id.path}")

        override fun serializeRecipe(): JsonObject {
            val json = JsonObject()

            json.addProperty("type", serializerName.toString())

            if (conditions.isNotEmpty()) {
                val conditionsArray = JsonArray()

                for (condition in conditions)
                    conditionsArray.add(CraftingHelper.serialize(condition))

                json.add("conditions", conditionsArray)
            }

            this.serializeRecipeData(json)

            return json
        }

        override fun getAdvancementId() = this.advancementId

        override fun getType() = ForgeRegistries.RECIPE_SERIALIZERS.getValue(serializerName)!!

        override fun serializeAdvancement(): JsonObject = advancementBuilder.serializeToJson()
    }
}