package hantonik.atomic.core.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;

import java.util.function.Function;

public class AtomicBlock extends Block {
    public AtomicBlock(Material material, Function<Properties, Properties> properties) {
        super(properties.apply(Properties.of(material)));
    }
}
