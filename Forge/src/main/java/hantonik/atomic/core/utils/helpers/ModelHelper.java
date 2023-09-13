package hantonik.atomic.core.utils.helpers;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.MultiPartBakedModel;
import net.minecraft.client.resources.model.WeightedBakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Map;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModelHelper {
    private static final Map<Block, ResourceLocation> TEXTURE_CACHE = Maps.newHashMap();

    public static final ResourceManagerReloadListener LISTENER = manager -> TEXTURE_CACHE.clear();

    @Nullable
    public static <T extends BakedModel> T getBakedModel(BlockState state, Class<T> modelClass) {
        var baked = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state);

        if (baked instanceof MultiPartBakedModel model)
            baked = model.selectors.get(0).getRight();

        if (baked instanceof WeightedBakedModel model)
            baked = model.wrapped;

        if (modelClass.isInstance(baked))
            return modelClass.cast(baked);

        return null;
    }

    @Nullable
    public static <T extends BakedModel> T getBakedModel(ItemLike item, Class<T> modelClass) {
        var baked = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(item.asItem());

        if (modelClass.isInstance(baked))
            return modelClass.cast(baked);

        return null;
    }

    public static ResourceLocation getParticleTexture(Block block) {
        return TEXTURE_CACHE.computeIfAbsent(block, b -> Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(b.defaultBlockState()).getParticleIcon().contents().name());
    }

    public static <T> T arrayToObject(JsonObject json, String name, int size, Function<float[], T> mapper) {
        var array = json.getAsJsonArray(name);

        if (array.size() != size)
            throw new JsonParseException("Expected " + size + " " + name + " values, found: " + array.size());

        var vec = new float[size];

        for (var i = 0; i < size; i++)
            vec[i] = ((JsonObject) array.get(i)).getAsJsonPrimitive(name + "[" + i + "]").getAsFloat();

        return mapper.apply(vec);
    }

    public static Vector3f arrayToVector(JsonObject json, String name) {
        return arrayToObject(json, name, 3, arr -> new Vector3f(arr[0], arr[1], arr[2]));
    }

    public static int getRotation(JsonObject json, String key) {
        var i = 0;

        if (json.has(key))
            i = json.getAsJsonPrimitive(key).getAsInt();

        if (i >= 0 && i % 90 == 0 && i / 90 <= 3)
            return i;
        else
            throw new JsonParseException("Invalid '" + key + "' " + i + " found, only 0/90/180/270 allowed");
    }
}
