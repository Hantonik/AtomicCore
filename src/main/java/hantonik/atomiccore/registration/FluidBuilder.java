package hantonik.atomiccore.registration;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.function.Supplier;

public class FluidBuilder {
    private Supplier<? extends Item> bucket;
    private Supplier<? extends LiquidBlock> block;

    private final FluidType.Properties properties;

    private boolean canConvertToSource = false;
    private float explosionResistance = 1.0F;

    private int slopeFindDistance = 4;
    private int levelDecreasePerBlock = 1;
    private int tickRate = 5;

    public FluidBuilder canConvertToSource() {
        this.canConvertToSource = true;

        return this;
    }

    public ForgeFlowingFluid.Properties build(Supplier<? extends Fluid> still, Supplier<? extends Fluid> flowing) {
        var properties = (new ForgeFlowingFluid.Properties(() -> new FluidType(this.properties), still, flowing)).slopeFindDistance(this.slopeFindDistance).levelDecreasePerBlock(this.levelDecreasePerBlock).explosionResistance(this.explosionResistance).tickRate(this.tickRate).block(this.block).bucket(this.bucket);

        if (this.canConvertToSource)
            this.properties.canConvertToSource(this.canConvertToSource);

        return properties;
    }

    public FluidBuilder canConvertToSource(boolean canConvertToSource) {
        this.canConvertToSource = canConvertToSource;

        return this;
    }

    public FluidBuilder bucket(Supplier<? extends Item> bucket) {
        this.bucket = bucket;

        return this;
    }

    public FluidBuilder block(Supplier<? extends LiquidBlock> block) {
        this.block = block;

        return this;
    }

    public FluidBuilder slopeFindDistance(int slopeFindDistance) {
        this.slopeFindDistance = slopeFindDistance;

        return this;
    }

    public FluidBuilder levelDecreasePerBlock(int levelDecreasePerBlock) {
        this.levelDecreasePerBlock = levelDecreasePerBlock;

        return this;
    }

    public FluidBuilder explosionResistance(float explosionResistance) {
        this.explosionResistance = explosionResistance;

        return this;
    }

    public FluidBuilder tickRate(int tickRate) {
        this.tickRate = tickRate;

        return this;
    }

    public FluidBuilder(FluidType.Properties properties) {
        this.properties = properties;
    }
}
