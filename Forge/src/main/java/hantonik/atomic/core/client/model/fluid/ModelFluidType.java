package hantonik.atomic.core.client.model.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ModelFluidType extends FluidType implements IClientFluidTypeExtensions {
    public static final IFluidModelProvider PROVIDER = DistExecutor.unsafeRunForDist(() -> () -> FluidTextureModel.LOADER, () -> () -> IFluidModelProvider.EMPTY);

    private final Supplier<? extends Fluid> fluid;

    public ModelFluidType(Properties properties, Supplier<? extends Fluid> fluid) {
        super(properties);

        this.fluid = fluid;
    }

    @Override
    public ResourceLocation getStillTexture() {
        var texture = PROVIDER.getStillTexture(this.fluid.get());

        if (texture == null)
            return new ResourceLocation("block/water_still");

        return texture;
    }

    @Override
    public ResourceLocation getFlowingTexture() {
        var texture = PROVIDER.getFlowingTexture(this.fluid.get());

        if (texture == null)
            return new ResourceLocation("block/water_flow");

        return texture;
    }

    @Override
    @Nullable
    public ResourceLocation getOverlayTexture() {
        return PROVIDER.getOverlayTexture(this.fluid.get());
    }

    @Override
    public int getDensity() {
        return PROVIDER.getDensity(this.fluid.get());
    }

    @Override
    public int getTemperature() {
        return PROVIDER.getTemperature(this.fluid.get());
    }

    @Override
    public int getViscosity() {
        return PROVIDER.getViscosity(this.fluid.get());
    }

    @Override
    public int getLightLevel() {
        return PROVIDER.getLightLevel(this.fluid.get());
    }

    @Override
    public int getTintColor() {
        return PROVIDER.getTintColor(this.fluid.get());
    }

    public interface IFluidModelProvider {
        IFluidModelProvider EMPTY = new IFluidModelProvider() {};

        @Nullable
        default ResourceLocation getStillTexture(Fluid fluid) {
            return null;
        }

        @Nullable
        default ResourceLocation getFlowingTexture(Fluid fluid) {
            return null;
        }

        @Nullable
        default ResourceLocation getOverlayTexture(Fluid fluid) {
            return null;
        }

        default int getDensity(Fluid fluid) {
            return 1000;
        }

        default int getTemperature(Fluid fluid) {
            return 300;
        }

        default int getViscosity(Fluid fluid) {
            return 1000;
        }

        default int getLightLevel(Fluid fluid) {
            return 0;
        }

        default int getTintColor(Fluid fluid) {
            return -1;
        }
    }
}
