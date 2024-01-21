package hantonik.atomic.core.block;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.DropExperienceBlock;

import java.util.function.Function;

public class AtomicDropExperienceBlock extends DropExperienceBlock {
    public AtomicDropExperienceBlock(Function<Properties, Properties> properties, IntProvider xpRange) {
        super(xpRange, properties.apply(Properties.of()));
    }
}
