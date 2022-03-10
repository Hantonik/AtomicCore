package hantonik.atomiccore.items;

import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class BasicShinyItem extends BasicItem {
    public BasicShinyItem(Function<Properties, Properties> properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
