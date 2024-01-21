package hantonik.atomic.core.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public interface IAtomicSpecialRecipe extends Recipe<Container> {
    ItemStack assemble(IItemHandler handler, RegistryAccess access);

    default boolean matches(IItemHandler handler) {
        return this.matches(handler, 0, handler.getSlots());
    }

    default boolean matches(IItemHandler handler, int startIndex, int endIndex) {
        NonNullList<ItemStack> inputs = NonNullList.create();

        for (var index = startIndex; index < endIndex; index++)
            inputs.add(handler.getStackInSlot(index));

        return RecipeMatcher.findMatches(inputs, this.getIngredients()) != null;
    }

    default NonNullList<ItemStack> getRemainingItems(IItemHandler handler) {
        var remaining = NonNullList.withSize(handler.getSlots(), ItemStack.EMPTY);

        for (var index = 0; index < remaining.size(); index++) {
            var stack = handler.getStackInSlot(index);

            if (stack.hasCraftingRemainingItem())
                remaining.set(index, stack.getCraftingRemainingItem());
        }

        return remaining;
    }

    @Override
    default ItemStack assemble(Container container, RegistryAccess access) {
        return this.assemble(new InvWrapper(container), access);
    }

    @Override
    default boolean matches(Container container, Level level) {
        return this.matches(new InvWrapper(container));
    }

    @Override
    default NonNullList<ItemStack> getRemainingItems(Container container) {
        return this.getRemainingItems(new InvWrapper(container));
    }
}
