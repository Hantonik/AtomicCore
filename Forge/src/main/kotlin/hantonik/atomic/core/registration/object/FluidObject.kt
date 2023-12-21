package hantonik.atomic.core.registration.`object`

import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.FluidTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.fluids.ForgeFlowingFluid

open class FluidObject<F : ForgeFlowingFluid>(val id: ResourceLocation, tagName: String, private val still: () -> F?, private val flowing: () -> F?, val block: (() -> LiquidBlock)?) : () -> F, ItemLike {
    val localTag: TagKey<Fluid> = FluidTags.create(this.id)
    val forgeTag: TagKey<Fluid> = FluidTags.create(ResourceLocation("forge", tagName))

    fun getStill(): F {
        this.still.invoke() ?: throw NullPointerException("Fluid object missing still fluid")

        return this.still.invoke()!!
    }

    fun getFlowing(): F {
        this.flowing.invoke() ?: throw NullPointerException("Fluid object missing flowing fluid")

        return this.flowing.invoke()!!
    }

    override fun invoke(): F = this.getStill()

    override fun asItem(): Item = this.getStill().bucket
}