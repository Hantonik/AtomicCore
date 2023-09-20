package hantonik.atomic.core.client.model.fluid

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions
import net.minecraftforge.fluids.FluidType
import net.minecraftforge.fml.DistExecutor
import java.util.function.Consumer
import java.util.function.Supplier

class ModelFluidType(properties: (Properties) -> Properties, private val fluid: () -> Fluid) : FluidType(properties.invoke(Properties.create())), IClientFluidTypeExtensions {
    companion object {
        val PROVIDER: IFluidModelProvider = DistExecutor.unsafeRunForDist({ Supplier { FluidTextureModel.LOADER } }, { Supplier { IFluidModelProvider.EMPTY } })
    }

    override fun initializeClient(consumer: Consumer<IClientFluidTypeExtensions>) {
        consumer.accept(object : IClientFluidTypeExtensions {
            override fun getStillTexture() = PROVIDER.getStillTexture(fluid.invoke())

            override fun getFlowingTexture() = PROVIDER.getFlowingTexture(fluid.invoke())

            override fun getOverlayTexture() = PROVIDER.getOverlayTexture(fluid.invoke())

            override fun getTintColor() = PROVIDER.getTintColor(fluid.invoke())
        })
    }

    override fun getLightLevel() = PROVIDER.getLightLevel(this.fluid.invoke())

    interface IFluidModelProvider {
        companion object {
            val EMPTY: IFluidModelProvider = object : IFluidModelProvider {}
        }

        fun getStillTexture(fluid: Fluid?): ResourceLocation = ResourceLocation("block/water_still")

        fun getFlowingTexture(fluid: Fluid?): ResourceLocation = ResourceLocation("block/water_flow")

        fun getOverlayTexture(fluid: Fluid?): ResourceLocation? = null

        fun getLightLevel(fluid: Fluid?): Int = 0

        fun getTintColor(fluid: Fluid?): Int = 0
    }
}