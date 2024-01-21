package hantonik.atomic.core.item.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public class AtomicBlockItem extends BlockItem {
    public AtomicBlockItem(Block block, Function<Properties, Properties> properties) {
        super(block, properties.apply(new Properties()));
    }

    public AtomicBlockItem(Block block) {
        this(block, properties -> properties);
    }
}
