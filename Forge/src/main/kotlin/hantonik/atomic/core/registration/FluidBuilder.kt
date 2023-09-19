package hantonik.atomic.core.registration

import com.google.errorprone.annotations.CanIgnoreReturnValue
import hantonik.atomic.core.client.model.fluid.ModelFluidType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.fluids.FluidType
import net.minecraftforge.fluids.FluidType.Properties
import net.minecraftforge.fluids.ForgeFlowingFluid

open class FluidBuilder(private val properties: Properties, private val location: ResourceLocation) {
    private var bucket: (() -> Item)? = null
    private var block: (() -> LiquidBlock)? = null

    private var explosionResistance = 1.0F

    private var slopeFindDistance = 4
    private var levelDecreasePerBlock = 1
    private var tickRate = 5


    @CanIgnoreReturnValue
    fun bucket(bucket: () -> Item): FluidBuilder {
        this.bucket = bucket

        return this
    }

    @CanIgnoreReturnValue
    fun block(block: () -> LiquidBlock): FluidBuilder {
        this.block = block

        return this
    }

    @CanIgnoreReturnValue
    fun slopeFindDistance(slopeFindDistance: Int): FluidBuilder {
        this.slopeFindDistance = slopeFindDistance

        return this
    }

    @CanIgnoreReturnValue
    fun levelDecreasePerBlock(levelDecreasePerBlock: Int): FluidBuilder {
        this.levelDecreasePerBlock = levelDecreasePerBlock

        return this
    }

    @CanIgnoreReturnValue
    fun explosionResistance(explosionResistance: Float): FluidBuilder {
        this.explosionResistance = explosionResistance

        return this
    }

    @CanIgnoreReturnValue
    fun tickRate(tickRate: Int): FluidBuilder {
        this.tickRate = tickRate

        return this
    }

    open fun build(type: () -> FluidType, still: () -> Fluid, flowing: () -> Fluid): ForgeFlowingFluid.Properties {
        return ForgeFlowingFluid.Properties(type, still, flowing)
            .slopeFindDistance(this.slopeFindDistance)
            .levelDecreasePerBlock(this.levelDecreasePerBlock)
            .explosionResistance(this.explosionResistance)
            .tickRate(this.tickRate)
            .block(this.block)
            .bucket(this.bucket)
    }

    open fun getFluidType(fluid: () -> Fluid, descriptionId: String): () -> ModelFluidType = { ModelFluidType({ this.properties.descriptionId(descriptionId) }, fluid) }

    fun getStillFluidType(fluid: () -> Fluid): () -> ModelFluidType = this.getFluidType(fluid, "fluid.${this.location.namespace}.${this.location.path}")

    fun getFlowingFluidType(fluid: () -> Fluid): () -> ModelFluidType = this.getFluidType(fluid, "fluid.${this.location.namespace}.flowing_${this.location.path}")
}