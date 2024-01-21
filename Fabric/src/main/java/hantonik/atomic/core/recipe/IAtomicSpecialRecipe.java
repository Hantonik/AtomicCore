package hantonik.atomic.core.recipe;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public interface IAtomicSpecialRecipe extends Recipe<Container> {
    ItemStack assemble(InventoryStorage handler, RegistryAccess access);

    boolean matches(InventoryStorage handler);

    default NonNullList<ItemStack> getRemainingItems(InventoryStorage handler) {
        var remaining = NonNullList.withSize(handler.getSlotCount(), ItemStack.EMPTY);

        for (var index = 0; index < remaining.size(); index++) {
            var slot = handler.getSlot(index);
            var stack = slot.getResource().toStack((int) slot.getAmount());

            if (stack.getItem().hasCraftingRemainingItem())
                remaining.set(index, stack.getRecipeRemainder());
        }

        return remaining;
    }

    @Override
    default ItemStack assemble(Container container, RegistryAccess access) {
        return this.assemble(InventoryStorage.of(container, null), access);
    }

    @Override
    default boolean matches(Container container, Level level) {
        return this.matches(InventoryStorage.of(container, null));
    }

    @Override
    default NonNullList<ItemStack> getRemainingItems(Container container) {
        return this.getRemainingItems(InventoryStorage.of(container, null));
    }
}
