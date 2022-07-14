package hantonik.atomiccore.registration;

import hantonik.atomiccore.registration.deferred.FluidDeferredRegister;
import hantonik.atomiccore.registration.object.FluidObject;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.function.Function;

public class FluidDeferredRegisterExtension extends FluidDeferredRegister {
    public FluidDeferredRegisterExtension(String modId) {
        super(modId);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, String tagName, FluidType.Properties properties, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, Material material, int lightLevel) {
        return super.register(name, tagName, properties.lightLevel(lightLevel), still, flowing, material, lightLevel);
    }
}
