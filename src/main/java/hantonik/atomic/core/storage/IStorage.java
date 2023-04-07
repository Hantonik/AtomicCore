package hantonik.atomic.core.storage;

import com.google.errorprone.annotations.CanIgnoreReturnValue;

public interface IStorage<T extends IStorage<T>> {
    default boolean isFull() {
        return this.getSpace() <= 0;
    }

    default int getSpace() {
        return this.getCapacity() - this.getStored();
    }

    void clear();

    @CanIgnoreReturnValue
    T setCapacity(int capacity);

    boolean isEmpty();

    int getCapacity();

    int getStored();

    StorageUnit getUnit();
}
