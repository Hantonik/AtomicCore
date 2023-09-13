package hantonik.atomic.core.util.helper

import net.minecraft.client.Minecraft
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.client.resources.model.MultiPartBakedModel
import net.minecraft.client.resources.model.WeightedBakedModel
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManagerReloadListener
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

object ModelHelper {
    private val TEXTURE_CACHE: MutableMap<Block, ResourceLocation> = mutableMapOf()

    val LISTENER = ResourceManagerReloadListener { TEXTURE_CACHE.clear() }

    fun <T : BakedModel> getBakedModel(state: BlockState, modelClass: Class<T>): T? {
        var baked = Minecraft.getInstance().modelManager.blockModelShaper.getBlockModel(state)

        if (baked is MultiPartBakedModel)
            baked = baked.selectors[0].right

        if (baked is WeightedBakedModel)
            baked = baked.wrapped

        if (modelClass.isInstance(baked))
            return modelClass.cast(baked)

        return null
    }

    fun <T : BakedModel> getBakedModel(item: ItemLike, modelClass: Class<T>): T? {
        val baked = Minecraft.getInstance().itemRenderer.itemModelShaper.getItemModel(item.asItem())

        if (modelClass.isInstance(baked))
            return modelClass.cast(baked)

        return null
    }

    fun getParticleTexture(block: Block): ResourceLocation = TEXTURE_CACHE.computeIfAbsent(block) { Minecraft.getInstance().modelManager.blockModelShaper.getBlockModel(it.defaultBlockState()).particleIcon.contents().name() }
}