package hantonik.atomic.core.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.jetbrains.annotations.Nullable;

public interface IAtomicBurnableItem extends IItemExtension {
    @Override
    int getBurnTime(ItemStack stack, @Nullable RecipeType<?> type);
}
