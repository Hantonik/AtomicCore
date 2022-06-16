package hantonik.atomiccore.registration;

// TODO

//import hantonik.atomiccore.registration.deferred.FluidDeferredRegister;
//import hantonik.atomiccore.registration.object.FluidObject;
//import net.minecraft.world.level.material.Material;
//import net.minecraftforge.fluids.FluidAttributes;
//import net.minecraftforge.fluids.ForgeFlowingFluid;
//
//import java.util.function.Function;
//
//public class FluidDeferredRegisterExtension extends FluidDeferredRegister {
//    public FluidDeferredRegisterExtension(String modID) {
//        super(modID);
//    }
//
//    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, String tagName, FluidAttributes.Builder builder, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, Material material, int lightLevel) {
//        return super.register(name, tagName, builder.luminosity(lightLevel), still, flowing, material, lightLevel);
//    }
//}
