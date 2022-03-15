package hantonik.atomiccore.utils.helpers;

import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class EnergyHelper {
    @Nullable
    public static <T> T get(@Nonnull LazyOptional<T> lazyOptional) {
        if (lazyOptional.isPresent())
            return lazyOptional.orElseThrow(IllegalStateException::new);

        return null;
    }
}
