package hantonik.atomic.core.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class AtomicBurnableItem extends AtomicItem implements IAtomicBurnableItem {
    private final int burnTime;

    public AtomicBurnableItem(Function<Properties, Properties> properties, int burnTime) {
        super(properties);

        this.burnTime = burnTime;
    }

    public AtomicBurnableItem(int burnTime) {
        this(properties -> properties, burnTime);
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> type) {
        return this.burnTime;
    }
}
