package hantonik.atomic.core.client.model.fluid

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions
import net.minecraftforge.fluids.FluidType
import net.minecraftforge.fml.DistExecutor
import java.util.function.Supplier

class ModelFluidType(properties: (Properties) -> Properties, private val fluid: () -> Fluid) : FluidType(properties.invoke(Properties.create())), IClientFluidTypeExtensions {
    companion object {
        val PROVIDER: IFluidModelProvider = DistExecutor.unsafeRunForDist({ Supplier { FluidTextureModel.LOADER } }, { Supplier { IFluidModelProvider.EMPTY } })
    }

    override fun getStillTexture() = PROVIDER.getStillTexture(this.fluid.invoke())

    override fun getFlowingTexture() = PROVIDER.getFlowingTexture(this.fluid.invoke())

    override fun getOverlayTexture() = PROVIDER.getOverlayTexture(this.fluid.invoke())

    override fun getTemperature() = PROVIDER.getTemperature(this.fluid.invoke())

    override fun getDensity() = PROVIDER.getDensity(this.fluid.invoke())

    override fun getViscosity() = PROVIDER.getViscosity(this.fluid.invoke())

    override fun getLightLevel() = PROVIDER.getLightLevel(this.fluid.invoke())

    override fun getTintColor() = PROVIDER.getTintColor(this.fluid.invoke())

    interface IFluidModelProvider {
        companion object {
            val EMPTY: IFluidModelProvider = object : IFluidModelProvider {}
        }

        fun getStillTexture(fluid: Fluid?): ResourceLocation = ResourceLocation("block/water_still")

        fun getFlowingTexture(fluid: Fluid?): ResourceLocation = ResourceLocation("block/water_flow")

        fun getOverlayTexture(fluid: Fluid?): ResourceLocation? = null

        fun getTemperature(fluid: Fluid?): Int = 300

        fun getDensity(fluid: Fluid?): Int = 1000

        fun getViscosity(fluid: Fluid?): Int = 1000

        fun getLightLevel(fluid: Fluid?): Int = 0

        fun getTintColor(fluid: Fluid?): Int = 0
    }
}