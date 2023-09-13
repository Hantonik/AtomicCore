package hantonik.atomic.core.block;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.DropExperienceBlock;

import java.util.function.Function;

public class AtomicDropExperienceBlock extends DropExperienceBlock {
    public AtomicDropExperienceBlock(Function<Properties, Properties> properties) {
        this(properties, ConstantInt.of(0));
    }

    public AtomicDropExperienceBlock(Function<Properties, Properties> properties, IntProvider xpRange) {
        super(properties.apply(Properties.of()), xpRange);
    }
}
