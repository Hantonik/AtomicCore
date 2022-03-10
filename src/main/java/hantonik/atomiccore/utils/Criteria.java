package hantonik.atomiccore.utils;

import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class Criteria {
    public static InventoryChangeTrigger.TriggerInstance hasItem(ItemLike itemIn) {
        return hasItem(ItemPredicate.Builder.item().of(itemIn).build());
    }

    public static InventoryChangeTrigger.TriggerInstance hasItem(TagKey<Item> tagIn) {
        return hasItem(ItemPredicate.Builder.item().of(tagIn).build());
    }

    public static InventoryChangeTrigger.TriggerInstance hasItem(ItemPredicate... predicates) {
        return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, predicates);
    }

    public static RecipeCriterion has(ItemLike provider) {
        return RecipeCriterion.of(provider.asItem().getRegistryName().getPath(), hasItem(provider.asItem()));
    }

    public static RecipeCriterion has(Ingredient ingredient) {
        Item item = ingredient.getItems()[0].getItem();
        return RecipeCriterion.of(item.getRegistryName().getPath(), hasItem(item));
    }

    public static class RecipeCriterion {
        public final String name;
        public final CriterionTriggerInstance criterion;

        private RecipeCriterion(String name, CriterionTriggerInstance criterion) {
            this.name = name;
            this.criterion = criterion;
        }

        public static RecipeCriterion of(String name, CriterionTriggerInstance criterion) {
            return new RecipeCriterion(name, criterion);
        }
    }
}
