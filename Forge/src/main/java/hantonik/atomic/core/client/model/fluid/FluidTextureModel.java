package hantonik.atomic.core.client.model.fluid;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import hantonik.atomic.core.utils.helpers.ModelHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public class FluidTextureModel implements IUnbakedGeometry<FluidTextureModel> {
    public final static Loader LOADER = new Loader();

    private final int density;
    private final int temperature;
    private final int viscosity;

    private final int lightLevel;
    private final int color;

    private static boolean isMissing(Material material) {
        return MissingTextureAtlasSprite.getLocation().equals(material.texture());
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        var overlay = context.getMaterial("overlay");

        return new Baked(new SimpleBakedModel.Builder(context.useAmbientOcclusion(), context.useBlockLight(), context.isGui3d(), context.getTransforms(), overrides).particle(spriteGetter.apply(overlay)).build(), context.getMaterial("still").texture(), context.getMaterial("flowing").texture(), isMissing(overlay) ? null : overlay.texture(), this.density, this.temperature, this.viscosity, this.lightLevel, this.color);
    }

    @Getter
    private static class Baked extends BakedModelWrapper<BakedModel> {
        private final ResourceLocation still;
        private final ResourceLocation flowing;
        private final ResourceLocation overlay;

        private final int density;
        private final int temperature;
        private final int viscosity;

        private final int lightLevel;
        private final int tintColor;

        public Baked(BakedModel model, ResourceLocation still, ResourceLocation flowing, @Nullable ResourceLocation overlay, int density, int temperature, int viscosity, int lightLevel, int tintColor) {
            super(model);

            this.still = still;
            this.flowing = flowing;
            this.overlay = overlay;

            this.density = density;
            this.temperature = temperature;
            this.viscosity = viscosity;

            this.lightLevel = lightLevel;
            this.tintColor = tintColor;
        }
    }

    private static class Loader implements IGeometryLoader<FluidTextureModel>, ModelFluidType.IFluidModelProvider {
        private final Map<Fluid, Baked> modelCache = Maps.newHashMap();

        @Nullable
        private Baked getFluidModel(Fluid fluid) {
            return ModelHelper.getBakedModel(fluid.defaultFluidState().createLegacyBlock(), Baked.class);
        }

        @Nullable
        private Baked getCachedModel(Fluid fluid) {
            return this.modelCache.computeIfAbsent(fluid, this::getFluidModel);
        }

        @Override
        @Nullable
        public ResourceLocation getStillTexture(Fluid fluid) {
            var model = this.getCachedModel(fluid);

            return model == null ? ModelFluidType.IFluidModelProvider.super.getStillTexture(fluid) : model.getStill();
        }

        @Override
        @Nullable
        public ResourceLocation getFlowingTexture(Fluid fluid) {
            var model = this.getCachedModel(fluid);

            return model == null ? ModelFluidType.IFluidModelProvider.super.getFlowingTexture(fluid) : model.getFlowing();
        }

        @Override
        @Nullable
        public ResourceLocation getOverlayTexture(Fluid fluid) {
            var model = this.getCachedModel(fluid);

            return model == null ? ModelFluidType.IFluidModelProvider.super.getOverlayTexture(fluid) : model.getOverlay();
        }

        @Override
        public int getDensity(Fluid fluid) {
            var model = this.getCachedModel(fluid);

            return model == null ? ModelFluidType.IFluidModelProvider.super.getDensity(fluid) : model.getDensity();
        }

        @Override
        public int getTemperature(Fluid fluid) {
            var model = this.getCachedModel(fluid);

            return model == null ? ModelFluidType.IFluidModelProvider.super.getTemperature(fluid) : model.getTemperature();
        }

        @Override
        public int getViscosity(Fluid fluid) {
            var model = this.getCachedModel(fluid);

            return model == null ? ModelFluidType.IFluidModelProvider.super.getViscosity(fluid) : model.getViscosity();
        }

        @Override
        public int getLightLevel(Fluid fluid) {
            var model = this.getCachedModel(fluid);

            return model == null ? ModelFluidType.IFluidModelProvider.super.getLightLevel(fluid) : model.getLightLevel();
        }

        @Override
        public int getTintColor(Fluid fluid) {
            var model = this.getCachedModel(fluid);

            return model == null ? ModelFluidType.IFluidModelProvider.super.getTintColor(fluid) : model.getTintColor();
        }

        @Override
        public FluidTextureModel read(JsonObject json, JsonDeserializationContext context) {
            var density = GsonHelper.getAsInt(json, "density", ModelFluidType.IFluidModelProvider.super.getDensity(null));
            var temperature = GsonHelper.getAsInt(json, "temperature", ModelFluidType.IFluidModelProvider.super.getTemperature(null));
            var viscosity = GsonHelper.getAsInt(json, "viscosity", ModelFluidType.IFluidModelProvider.super.getViscosity(null));

            var lightLevel = GsonHelper.getAsInt(json, "lightLevel", ModelFluidType.IFluidModelProvider.super.getLightLevel(null));
            var color = ModelFluidType.IFluidModelProvider.super.getTintColor(null);

            if (json.has("color")) {
                var colorString = GsonHelper.getAsString(json, "color");

                if (colorString.charAt(0) == '-' || (colorString.length() != 6 && colorString.length() != 8))
                    throw new JsonSyntaxException("Invalid color '" + colorString + "'");

                try {
                    color = (int) Long.parseLong(colorString, 16);

                    if (colorString.length() == 6)
                        color |= 0xFF000000;
                } catch (NumberFormatException e) {
                    throw new JsonSyntaxException("Invalid color '" + colorString + "'");
                }
            }

            return new FluidTextureModel(density, temperature, viscosity, lightLevel, color);
        }
    }
}
