package hantonik.atomic.core.registration.object;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EnumObject<T extends Enum<T>, I extends IForgeRegistry<? super I>> {
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final EnumObject EMPTY = new EnumObject(Collections.emptyMap());

    private final Map<T, Supplier<? extends I>> map;

    @Nullable
    public Supplier<? extends I> getEntry(T value) {
        return this.map.get(value);
    }

    public I get(T value) {
        var entry = this.getEntry(value);

        if (entry == null)
            throw new NoSuchElementException("Missing key " + value);

        return Objects.requireNonNull(entry.get(), () -> "No enum object value for " + value);
    }

    @Nullable
    public I getOrNull(T value) {
        var entry = this.map.get(value);

        if (entry == null)
            return null;

        try {
            return entry.get();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public boolean contains(IForgeRegistry<? super I> value) {
        return this.map.values().stream().map(Supplier::get).anyMatch(value::equals);
    }

    public List<I> values() {
        return this.map.values().stream().map(Supplier::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void forEach(BiConsumer<T, ? super I> consumer) {
        this.map.forEach((k, v) -> {
            I value;

            try {
                value = v.get();
            } catch (NullPointerException e) {
                return;
            }

            if (value != null)
                consumer.accept(k, value);
        });
    }

    public void forEach(Consumer<? super I> consumer) {
        this.forEach((k, v) -> consumer.accept(v));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>, I extends IForgeRegistry<? super I>> EnumObject<T, I> empty() {
        return (EnumObject<T, I>) EMPTY;
    }

    public static class Builder<T extends Enum<T>, I extends IForgeRegistry<? super I>> {
        private final Map<T, Supplier<? extends I>> map;

        public Builder(Class<T> enumClass) {
            this.map = Maps.newEnumMap(enumClass);
        }

        public Builder<T, I> put(T key, Supplier<? extends I> value) {
            this.map.put(key, value);

            return this;
        }

        public Builder<T, I> putAll(Map<T, Supplier<? extends I>> map) {
            this.map.putAll(map);

            return this;
        }

        public Builder<T, I> putAll(EnumObject<T, ? extends I> object) {
            this.map.putAll(object.map);

            return this;
        }

        public EnumObject<T, I> build() {
            return new EnumObject<>(this.map);
        }
    }
}
