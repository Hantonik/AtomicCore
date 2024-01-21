package hantonik.atomic.core.block;

import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public class AtomicBlock extends Block {
    public AtomicBlock(Function<Properties, Properties> properties) {
        super(properties.apply(Properties.of()));
    }
}
