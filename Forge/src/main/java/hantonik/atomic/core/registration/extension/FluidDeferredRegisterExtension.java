package hantonik.atomic.core.registration.extension;

import hantonik.atomic.core.registration.deferred.FluidDeferredRegister;
import hantonik.atomic.core.registration.object.FluidObject;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.function.Function;

public class FluidDeferredRegisterExtension extends FluidDeferredRegister {
    public FluidDeferredRegisterExtension(String modId) {
        super(modId);
    }

    @Override
    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, String tagName, FluidType.Properties properties, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, int lightLevel) {
        return super.register(name, tagName, properties.lightLevel(lightLevel), still, flowing, lightLevel);
    }

    @Override
    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, String tagName, FluidType.Properties properties, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, int lightLevel, boolean showTemperature) {
        return super.register(name, tagName, properties.lightLevel(lightLevel), still, flowing, lightLevel, showTemperature);
    }
}
