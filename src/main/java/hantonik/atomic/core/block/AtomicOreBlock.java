package hantonik.atomic.core.block;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

import java.util.function.Function;

public class AtomicOreBlock extends AtomicBlock {
    public AtomicOreBlock(Function<Properties, Properties> properties) {
        super(Material.STONE, p -> properties.apply(Properties.of(Material.STONE).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    }
}
