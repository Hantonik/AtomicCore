package hantonik.atomic.core.registration.deferred;

import hantonik.atomic.core.registration.object.EnumObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public abstract class DeferredRegisterWrapper<T> {
    protected final DeferredRegister<T> register;
    protected final String modId;

    protected DeferredRegisterWrapper(IForgeRegistry<T> registry, String modId) {
        this.register = DeferredRegister.create(registry, modId);
        this.modId = modId;
    }

    public void register(IEventBus bus) {
        this.register.register(bus);
    }

    protected ResourceLocation resource(String name) {
        return new ResourceLocation(this.modId, name);
    }

    protected String resourceName(String name) {
        return this.modId + ":" + name;
    }

    protected static <E extends Enum<E> & StringRepresentable, V extends T, T extends IForgeRegistry<T>> EnumObject<E, V> registerEnum(E[] values, String name, BiFunction<String, E, Supplier<? extends V>> register) {
        if (values.length == 0)
            throw new IllegalArgumentException("Must have at least one value");

        EnumObject.Builder<E, V> builder = new EnumObject.Builder<>(values[0].getDeclaringClass());

        for (var value : values)
            builder.put(value, register.apply(value.getSerializedName() + "_" + name, value));

        return builder.build();
    }

    protected static <E extends Enum<E> & StringRepresentable, V extends T, T extends IForgeRegistry<T>> EnumObject<E, V> registerEnum(String name, E[] values, BiFunction<String, E, Supplier<? extends V>> register) {
        if (values.length == 0)
            throw new IllegalArgumentException("Must have at least one value");

        EnumObject.Builder<E, V> builder = new EnumObject.Builder<>(values[0].getDeclaringClass());

        for (var value : values)
            builder.put(value, register.apply(name + "_" + value.getSerializedName(), value));

        return builder.build();
    }
}
