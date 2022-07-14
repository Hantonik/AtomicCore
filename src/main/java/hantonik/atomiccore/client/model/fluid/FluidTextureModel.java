package hantonik.atomiccore.client.model.fluid;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import hantonik.atomiccore.utils.helpers.ModelHelper;
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

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class FluidTextureModel implements IUnbakedGeometry<FluidTextureModel> {
    public static Loader LOADER = new Loader();

    private final int color;

    public FluidTextureModel(int color) {
        this.color = color;
    }

    private static boolean isMissing(Material material) {
        return MissingTextureAtlasSprite.getLocation().equals(material.texture());
    }

    private static void getTexture(IGeometryBakingContext context, String name, Collection<Material> textures, Set<Pair<String, String>> missingTextureErrors) {
        var material = context.getMaterial(name);

        if (isMissing(material))
            missingTextureErrors.add(Pair.of(name, context.getModelName()));

        textures.add(material);
    }

    @Override
    public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        Set<Material> textures = new HashSet<>();

        getTexture(context, "still", textures, missingTextureErrors);
        getTexture(context, "flowing", textures, missingTextureErrors);

        var overlay = context.getMaterial("overlay");

        if (!isMissing(overlay))
            textures.add(overlay);

        return textures;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
        var overlay = context.getMaterial("overlay");

        return new Baked(new SimpleBakedModel.Builder(context.useAmbientOcclusion(), context.useBlockLight(), context.isGui3d(), context.getTransforms(), overrides).build(), context.getMaterial("still").texture(), context.getMaterial("flowing").texture(), isMissing(overlay) ? null : overlay.texture(), this.color);
    }

    private static class Baked extends BakedModelWrapper<BakedModel> {
        private final ResourceLocation still;
        private final ResourceLocation flowing;
        private final ResourceLocation overlay;

        private final int color;

        public Baked(BakedModel originalModel, ResourceLocation still, ResourceLocation flowing, @Nullable ResourceLocation overlay, int color) {
            super(originalModel);

            this.still = still;
            this.flowing = flowing;
            this.overlay = overlay;
            this.color = color;
        }

        public ResourceLocation getStill() {
            return this.still;
        }

        public ResourceLocation getFlowing() {
            return this.flowing;
        }

        public ResourceLocation getOverlay() {
            return this.overlay;
        }

        public int getColor() {
            return this.color;
        }
    }

    private static class Loader implements IGeometryLoader<FluidTextureModel> {
        private final Map<Fluid,Baked> modelCache = new HashMap<>();

        @Nullable
        private Baked getFluidModel(Fluid fluid) {
            return ModelHelper.getBakedModel(fluid.defaultFluidState().createLegacyBlock(), Baked.class);
        }

        @Nullable
        private Baked getCachedModel(Fluid fluid) {
            return this.modelCache.computeIfAbsent(fluid, this::getFluidModel);
        }

        @Nullable
        public ResourceLocation getStillTexture(Fluid fluid) {
            var model = getCachedModel(fluid);

            return model == null ? null : model.getStill();
        }

        @Nullable
        public ResourceLocation getFlowingTexture(Fluid fluid) {
            var model = getCachedModel(fluid);

            return model == null ? null : model.getFlowing();
        }

        @Nullable
        public ResourceLocation getOverlayTexture(Fluid fluid) {
            var model = getCachedModel(fluid);

            return model == null ? null : model.getOverlay();
        }

        public int getColor(Fluid fluid) {
            var model = getCachedModel(fluid);

            return model == null ? -1 : model.getColor();
        }

        @Override
        public FluidTextureModel read(JsonObject json, JsonDeserializationContext context) {
            int color = -1;

            if (json.has("color")) {
                var colorString = GsonHelper.getAsString(json, "color");

                var length = colorString.length();

                if (colorString.charAt(0) == '-' || (length != 6 && length != 8))
                    throw new JsonSyntaxException("Invalid color '" + colorString + "'");

                try {
                    color = (int)Long.parseLong(colorString, 16);

                    if (length == 6)
                        color |= 0xFF000000;
                } catch (NumberFormatException e) {
                    throw new JsonSyntaxException("Invalid color '" + colorString + "'");
                }
            }

            return new FluidTextureModel(color);
        }
    }
}
