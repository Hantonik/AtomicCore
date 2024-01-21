package hantonik.atomic.core.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.Nullable;

public interface IAtomicBurnableItem extends IForgeItem {
    @Override
    int getBurnTime(ItemStack stack, @Nullable RecipeType<?> type);
}
