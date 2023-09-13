package hantonik.atomic.core.client.model.fluid

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import hantonik.atomic.core.util.helper.ModelHelper
import net.minecraft.client.renderer.block.model.ItemOverrides
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.GsonHelper
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.client.model.BakedModelWrapper
import net.minecraftforge.client.model.geometry.IGeometryBakingContext
import net.minecraftforge.client.model.geometry.IGeometryLoader
import net.minecraftforge.client.model.geometry.IUnbakedGeometry
import java.util.function.Function

class FluidTextureModel(private val temperature: Int, private val density: Int, private val viscosity: Int, private val lightLevel: Int, private val tintColor: Int) : IUnbakedGeometry<FluidTextureModel> {
    companion object {
        val LOADER = Loader()

        private fun isMissing(material: Material) = MissingTextureAtlasSprite.getLocation() == material.texture()
    }

    override fun bake(context: IGeometryBakingContext, baker: ModelBaker, spriteGetter: Function<Material, TextureAtlasSprite>, modelState: ModelState, overrides: ItemOverrides, modelLocation: ResourceLocation): BakedModel {
        val overlay = context.getMaterial("overlay")

        return Baked(SimpleBakedModel.Builder(context.useAmbientOcclusion(), context.useBlockLight(), context.isGui3d, context.transforms, overrides).particle(spriteGetter.apply(overlay)).build(), context.getMaterial("still").texture(), context.getMaterial("flowing").texture(), if (isMissing(overlay)) null else overlay.texture(), this.temperature, this.density, this.viscosity, this.lightLevel, this.tintColor)
    }

    internal class Baked(model: BakedModel, val still: ResourceLocation, val flowing: ResourceLocation, val overlay: ResourceLocation?, val temperature: Int, val density: Int, val viscosity: Int, val lightLevel: Int, val tintColor: Int) : BakedModelWrapper<BakedModel>(model)

    class Loader : IGeometryLoader<FluidTextureModel>, ModelFluidType.IFluidModelProvider {
        private val modelCache: MutableMap<Fluid?, Baked?> = mutableMapOf()

        override fun getStillTexture(fluid: Fluid?): ResourceLocation {
            val model = this.getCachedModel(fluid)

            model ?: return super.getStillTexture(fluid)

            return model.still
        }

        override fun getFlowingTexture(fluid: Fluid?): ResourceLocation {
            val model = this.getCachedModel(fluid)

            model ?: return super.getFlowingTexture(fluid)

            return model.flowing
        }

        override fun getOverlayTexture(fluid: Fluid?): ResourceLocation? {
            val model = this.getCachedModel(fluid)

            model ?: return super.getOverlayTexture(fluid)

            return model.overlay
        }

        override fun getTemperature(fluid: Fluid?): Int {
            val model = this.getCachedModel(fluid)

            model ?: return super.getTemperature(fluid)

            return model.temperature
        }

        override fun getDensity(fluid: Fluid?): Int {
            val model = this.getCachedModel(fluid)

            model ?: return super.getDensity(fluid)

            return model.density
        }

        override fun getViscosity(fluid: Fluid?): Int {
            val model = this.getCachedModel(fluid)

            model ?: return super.getViscosity(fluid)

            return model.viscosity
        }

        override fun getLightLevel(fluid: Fluid?): Int {
            val model = this.getCachedModel(fluid)

            model ?: return super.getLightLevel(fluid)

            return model.lightLevel
        }

        override fun getTintColor(fluid: Fluid?): Int {
            val model = this.getCachedModel(fluid)

            model ?: return super.getTintColor(fluid)

            return model.tintColor
        }

        private fun getFluidModel(fluid: Fluid?): Baked? = ModelHelper.getBakedModel(fluid?.defaultFluidState()!!.createLegacyBlock(), Baked::class.java)

        private fun getCachedModel(fluid: Fluid?): Baked? = this.modelCache.computeIfAbsent(fluid, this::getFluidModel)

        override fun read(json: JsonObject, context: JsonDeserializationContext): FluidTextureModel {
            val temperature = GsonHelper.getAsInt(json, "temperature", super.getTemperature(null))
            val density = GsonHelper.getAsInt(json, "density", super.getDensity(null))
            val viscosity = GsonHelper.getAsInt(json, "viscosity", super.getViscosity(null))
            val lightLevel = GsonHelper.getAsInt(json, "lightLevel", super.getLightLevel(null))
            var color = super.getTintColor(null)

            if (json.has("color")) {
                val colorString = GsonHelper.getAsString(json, "color")

                if (colorString[0] == '-' || colorString.length != 6 && colorString.length != 8)
                    throw JsonSyntaxException("Invalid color '$colorString'")

                try {
                    color = colorString.toLong().toInt()

                    if (colorString.length == 6)
                        color = color.or(0xFF000000.toInt())
                } catch (e: NumberFormatException) {
                    throw JsonSyntaxException("Invalid color '$colorString'")
                }
            }

            return FluidTextureModel(temperature, density, viscosity, lightLevel, color)
        }
    }
}