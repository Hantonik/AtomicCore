package hantonik.atomic.core.item;

import net.minecraft.world.item.Item;

import java.util.function.Function;

public class AtomicItem extends Item {
    public AtomicItem(Function<Properties, Properties> properties) {
        super(properties.apply(new Properties()));
    }

    public AtomicItem() {
        this(properties -> properties);
    }
}
