package hantonik.atomic.core.registration;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import hantonik.atomic.core.client.model.fluid.ModelFluidType;
import lombok.RequiredArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class FluidBuilder {
    private final FluidType.Properties properties;
    private final ResourceLocation location;

    private Supplier<? extends Item> bucket;
    private Supplier<? extends LiquidBlock> block;

    private boolean canConvertToSource = false;
    private float explosionResistance = 1.0F;

    private int slopeFindDistance = 4;
    private int levelDecreasePerBlock = 1;
    private int tickRate = 5;

    @CanIgnoreReturnValue
    public FluidBuilder canConvertToSource(boolean canConvertToSource) {
        this.canConvertToSource = canConvertToSource;

        return this;
    }

    @CanIgnoreReturnValue
    public FluidBuilder bucket(Supplier<? extends Item> bucket) {
        this.bucket = bucket;

        return this;
    }

    @CanIgnoreReturnValue
    public FluidBuilder block(Supplier<? extends LiquidBlock> block) {
        this.block = block;

        return this;
    }

    @CanIgnoreReturnValue
    public FluidBuilder slopeFindDistance(int slopeFindDistance) {
        this.slopeFindDistance = slopeFindDistance;

        return this;
    }

    @CanIgnoreReturnValue
    public FluidBuilder levelDecreasePerBlock(int levelDecreasePerBlock) {
        this.levelDecreasePerBlock = levelDecreasePerBlock;

        return this;
    }

    @CanIgnoreReturnValue
    public FluidBuilder explosionResistance(float explosionResistance) {
        this.explosionResistance = explosionResistance;

        return this;
    }

    @CanIgnoreReturnValue
    public FluidBuilder tickRate(int tickRate) {
        this.tickRate = tickRate;

        return this;
    }

    public ForgeFlowingFluid.Properties build(Supplier<? extends FluidType> type, Supplier<? extends Fluid> still, Supplier<? extends Fluid> flowing) {
        var properties = new ForgeFlowingFluid.Properties(type, still, flowing)
                .slopeFindDistance(this.slopeFindDistance)
                .levelDecreasePerBlock(this.levelDecreasePerBlock)
                .explosionResistance(this.explosionResistance)
                .tickRate(this.tickRate)
                .block(this.block)
                .bucket(this.bucket);

        if (this.canConvertToSource)
            this.properties.canConvertToSource(this.canConvertToSource);

        return properties;
    }

    public Supplier<? extends FluidType> getFluidType(Supplier<? extends Fluid> fluid, String descriptionId) {
        return () -> new ModelFluidType(this.properties.descriptionId(descriptionId), fluid);
    }

    public Supplier<? extends FluidType> getStillFluidType(Supplier<? extends Fluid> fluid) {
        return this.getFluidType(fluid, "fluid." + this.location.getNamespace() + "." + this.location.getPath());
    }

    public Supplier<? extends FluidType> getFlowingFluidType(Supplier<? extends Fluid> fluid) {
        return this.getFluidType(fluid, "fluid." + this.location.getNamespace() + ".flowing_" + this.location.getPath());
    }
}
