package hantonik.atomic.core.item;

import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class AtomicFoilItem extends AtomicItem {
    public AtomicFoilItem(Function<Properties, Properties> properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
