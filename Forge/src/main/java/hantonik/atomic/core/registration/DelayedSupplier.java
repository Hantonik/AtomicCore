package hantonik.atomic.core.registration;

import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class DelayedSupplier<T> implements Supplier<T> {
    @Setter
    @Nullable
    private Supplier<T> supplier;

    @Override
    public T get() {
        if (this.supplier == null)
            throw new IllegalStateException("Attempted to call DelayedSupplier::get() before the supplier was set.");

        return this.supplier.get();
    }
}
