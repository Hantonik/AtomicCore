package hantonik.atomiccore.utils;

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public final class Criteria {
    public static InventoryChangeTrigger.TriggerInstance hasItem(ItemLike itemIn) {
        return hasItem(ItemPredicate.Builder.item().of(itemIn).build());
    }

    public static InventoryChangeTrigger.TriggerInstance hasItem(TagKey<Item> tagIn) {
        return hasItem(ItemPredicate.Builder.item().of(tagIn).build());
    }

    public static InventoryChangeTrigger.TriggerInstance hasItem(ItemPredicate... predicates) {
        return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, predicates);
    }
}
