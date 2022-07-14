package hantonik.atomiccore.item;

import net.minecraft.world.item.Item;

import java.util.function.Function;

public class BasicItem extends Item {
    public BasicItem(Function<Properties, Properties> properties) {
        super(properties.apply(new Properties()));
    }
}
