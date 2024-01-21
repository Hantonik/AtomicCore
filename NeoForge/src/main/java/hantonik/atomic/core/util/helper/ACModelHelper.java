package hantonik.atomic.core.util.helper;

import com.google.common.collect.Maps;
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

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ACModelHelper {
    private static final Map<Block, ResourceLocation> TEXTURE_CACHE = Maps.newHashMap();

    public static final ResourceManagerReloadListener LISTENER = manager -> TEXTURE_CACHE.clear();

    @Nullable
    public static <T extends BakedModel> T getBakedModel(BlockState state, Class<T> modelClass) {
        var baked = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state);

        if (baked instanceof MultiPartBakedModel multiPart)
            baked = multiPart.selectors.get(0).getRight();

        if (baked instanceof WeightedBakedModel weighted)
            baked = weighted.wrapped;

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
}
