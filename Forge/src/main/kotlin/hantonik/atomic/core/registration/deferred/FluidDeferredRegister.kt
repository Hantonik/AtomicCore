package hantonik.atomic.core.registration.deferred

import hantonik.atomic.core.AtomicCore
import hantonik.atomic.core.registration.DelayedSupplier
import hantonik.atomic.core.registration.FluidBuilder
import hantonik.atomic.core.registration.ItemProperties
import hantonik.atomic.core.registration.`object`.FluidObject
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fluids.FluidType
import net.minecraftforge.fluids.ForgeFlowingFluid
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.RegistryObject

class FluidDeferredRegister(registry: IForgeRegistry<Fluid>, modId: String, private val fluidTypeRegistry: DeferredRegister<FluidType>, private val blockRegistry: DeferredRegister<Block>, private val itemRegistry: DeferredRegister<Item>) : DeferredRegisterWrapper<Fluid>(registry, modId) {
    constructor(modId: String) : this(ForgeRegistries.FLUIDS, modId, DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, modId), DeferredRegister.create(ForgeRegistries.BLOCKS, modId), DeferredRegister.create(ForgeRegistries.ITEMS, modId))

    override fun register(bus: IEventBus) {
        super.register(bus)

        this.fluidTypeRegistry.register(bus)
        this.blockRegistry.register(bus)
        this.itemRegistry.register(bus)
    }

    fun <I : Fluid> registerFluid(name: String, fluid: () -> I): RegistryObject<I> = this.register.register(name, fluid)

    fun <F : ForgeFlowingFluid> register(name: String, tagName: String, builder: FluidBuilder, still: (ForgeFlowingFluid.Properties) -> F, flowing: (ForgeFlowingFluid.Properties) -> F, block: (() -> FlowingFluid) -> LiquidBlock, bucketProperties: Properties, showTemperature: Boolean): FluidObject<F> {
        val stillDelayed = DelayedSupplier<F>()
        val flowingDelayed = DelayedSupplier<F>()

        val blockObj: () -> LiquidBlock = this.blockRegistry.register("${name}_fluid") { block.invoke(stillDelayed) }::get

        builder.bucket(this.itemRegistry.register("${name}_bucket") {
            object : BucketItem(stillDelayed, bucketProperties) {
                override fun appendHoverText(stack: ItemStack, level: Level?, components: MutableList<Component>, flag: TooltipFlag) {
                    super.appendHoverText(stack, level, components, flag)

                    if (showTemperature)
                        components.add(Component.translatable("tooltip.${AtomicCore.MOD_ID}.temperature").withStyle(ChatFormatting.GRAY).append(": ").append(Component.literal(stillDelayed.invoke().fluidType.temperature.toString()).append("K").withStyle(ChatFormatting.RED)))
                }
            }
        }::get)

        val stillProps = builder.block(blockObj).build(builder.getStillFluidType(stillDelayed), stillDelayed, flowingDelayed)
        val stillSupplier: () -> F = this.registerFluid(name) { still.invoke(stillProps) }::get
        stillDelayed.supplier = stillSupplier
        this.fluidTypeRegistry.register(name, builder.getStillFluidType(stillSupplier))

        val flowingProps = builder.block(blockObj).build(builder.getFlowingFluidType(flowingDelayed), stillDelayed, flowingDelayed)
        val flowingSupplier: () -> F = this.registerFluid("flowing_$name") { flowing.invoke(flowingProps) }::get
        flowingDelayed.supplier = flowingSupplier
        this.fluidTypeRegistry.register("flowing_$name", builder.getFlowingFluidType(flowingSupplier))

        return FluidObject(this.resource(name), tagName, stillSupplier, flowingSupplier, blockObj)
    }

    fun <F : ForgeFlowingFluid> register(name: String, builder: FluidBuilder, still: (ForgeFlowingFluid.Properties) -> F, flowing: (ForgeFlowingFluid.Properties) -> F, block: (() -> FlowingFluid) -> LiquidBlock, bucketProperties: Properties, showTemperature: Boolean) = this.register(name, name, builder, still, flowing, block, bucketProperties, showTemperature)

    fun <F : ForgeFlowingFluid> register(name: String, tagName: String, properties: FluidType.Properties, still: (ForgeFlowingFluid.Properties) -> F, flowing: (ForgeFlowingFluid.Properties) -> F, lightLevel: Int, bucketProperties: Properties, showTemperature: Boolean): FluidObject<F> {
        return this.register(name, tagName, FluidBuilder(properties, ResourceLocation(this.modId, name)).explosionResistance(100.0F), still, flowing, {
            object : LiquidBlock(it, Properties.of().air().strength(100.0F).noLootTable().lightLevel { lightLevel }) {
                override fun appendHoverText(stack: ItemStack, level: BlockGetter?, components: MutableList<Component>, flag: TooltipFlag) {
                    super.appendHoverText(stack, level, components, flag)

                    if (showTemperature)
                        components.add(Component.translatable("tooltip.${AtomicCore.MOD_ID}.temperature").withStyle(ChatFormatting.GRAY).append(": ").append(Component.literal(it.invoke().fluidType.temperature.toString()).append("K").withStyle(ChatFormatting.RED)))
                }

                override fun getDescriptionId() = "block.$modId.${name}_fluid"
            }
        }, bucketProperties, showTemperature)
    }

    fun <F : ForgeFlowingFluid> register(name: String, properties: FluidType.Properties, still: (ForgeFlowingFluid.Properties) -> F, flowing: (ForgeFlowingFluid.Properties) -> F, lightLevel: Int, bucketProperties: Properties, showTemperature: Boolean) = this.register(name, name, properties, still, flowing, lightLevel, bucketProperties, showTemperature)

    fun <F : ForgeFlowingFluid> register(name: String, tagName: String, builder: FluidBuilder, still: (ForgeFlowingFluid.Properties) -> F, flowing: (ForgeFlowingFluid.Properties) -> F, block: (() -> FlowingFluid) -> LiquidBlock, showTemperature: Boolean) = this.register(name, tagName, builder, still, flowing, block, ItemProperties.BUCKET, showTemperature)

    fun <F : ForgeFlowingFluid> register(name: String, builder: FluidBuilder, still: (ForgeFlowingFluid.Properties) -> F, flowing: (ForgeFlowingFluid.Properties) -> F, block: (() -> FlowingFluid) -> LiquidBlock, showTemperature: Boolean) = this.register(name, builder, still, flowing, block, ItemProperties.BUCKET, showTemperature)

    fun <F : ForgeFlowingFluid> register(name: String, tagName: String, properties: FluidType.Properties, still: (ForgeFlowingFluid.Properties) -> F, flowing: (ForgeFlowingFluid.Properties) -> F, lightLevel: Int, showTemperature: Boolean) = this.register(name, tagName, properties, still, flowing, lightLevel, ItemProperties.BUCKET, showTemperature)

    fun <F : ForgeFlowingFluid> register(name: String, properties: FluidType.Properties, still: (ForgeFlowingFluid.Properties) -> F, flowing: (ForgeFlowingFluid.Properties) -> F, lightLevel: Int, showTemperature: Boolean) = this.register(name, name, properties, still, flowing, lightLevel, ItemProperties.BUCKET, showTemperature)

    fun <F : ForgeFlowingFluid> register(name: String, builder: FluidBuilder, still: (ForgeFlowingFluid.Properties) -> F, flowing: (ForgeFlowingFluid.Properties) -> F, block: (() -> FlowingFluid) -> LiquidBlock, bucketProperties: Properties) = this.register(name, name, builder, still, flowing, block, bucketProperties, false)

    fun <F : ForgeFlowingFluid> register(name: String, tagName: String, properties: FluidType.Properties, still: (ForgeFlowingFluid.Properties) -> F, flowing: (ForgeFlowingFluid.Properties) -> F, lightLevel: Int, bucketProperties: Properties): FluidObject<F> {
        return this.register(name, tagName, FluidBuilder(properties, ResourceLocation(this.modId, name)).explosionResistance(100.0F), still, flowing, {
            object : LiquidBlock(it, Properties.of().air().strength(100.0F).noLootTable().lightLevel { lightLevel }) {
                override fun getDescriptionId() = "block.$modId.${name}_fluid"
            }
        }, bucketProperties, false)
    }

    fun <F : ForgeFlowingFluid> register(name: String, properties: FluidType.Properties, still: (ForgeFlowingFluid.Properties) -> F, flowing: (ForgeFlowingFluid.Properties) -> F, lightLevel: Int, bucketProperties: Properties) = this.register(name, name, properties, still, flowing, lightLevel, bucketProperties, false)

    fun <F : ForgeFlowingFluid> register(name: String, tagName: String, builder: FluidBuilder, still: (ForgeFlowingFluid.Properties) -> F, flowing: (ForgeFlowingFluid.Properties) -> F, block: (() -> FlowingFluid) -> LiquidBlock) = this.register(name, tagName, builder, still, flowing, block, ItemProperties.BUCKET, false)

    fun <F : ForgeFlowingFluid> register(name: String, builder: FluidBuilder, still: (ForgeFlowingFluid.Properties) -> F, flowing: (ForgeFlowingFluid.Properties) -> F, block: (() -> FlowingFluid) -> LiquidBlock) = this.register(name, builder, still, flowing, block, ItemProperties.BUCKET, false)

    fun <F : ForgeFlowingFluid> register(name: String, tagName: String, properties: FluidType.Properties, still: (ForgeFlowingFluid.Properties) -> F, flowing: (ForgeFlowingFluid.Properties) -> F, lightLevel: Int) = this.register(name, tagName, properties, still, flowing, lightLevel, ItemProperties.BUCKET, false)

    fun <F : ForgeFlowingFluid> register(name: String, properties: FluidType.Properties, still: (ForgeFlowingFluid.Properties) -> F, flowing: (ForgeFlowingFluid.Properties) -> F, lightLevel: Int) = this.register(name, properties, still, flowing, lightLevel, ItemProperties.BUCKET, false)

    fun register(name: String, tagName: String, properties: FluidType.Properties, lightLevel: Int, bucketProperties: Properties, showTemperature: Boolean): FluidObject<ForgeFlowingFluid> = this.register(name, tagName, properties, ForgeFlowingFluid::Source, ForgeFlowingFluid::Flowing, lightLevel, bucketProperties, showTemperature)

    fun register(name: String, properties: FluidType.Properties, lightLevel: Int, bucketProperties: Properties, showTemperature: Boolean): FluidObject<ForgeFlowingFluid> = this.register(name, name, properties, lightLevel, bucketProperties, showTemperature)

    fun register(name: String, tagName: String, properties: FluidType.Properties, lightLevel: Int, showTemperature: Boolean): FluidObject<ForgeFlowingFluid> = this.register(name, tagName, properties, ForgeFlowingFluid::Source, ForgeFlowingFluid::Flowing, lightLevel, ItemProperties.BUCKET, showTemperature)

    fun register(name: String, properties: FluidType.Properties, lightLevel: Int, showTemperature: Boolean): FluidObject<ForgeFlowingFluid> = this.register(name, name, properties, lightLevel, ItemProperties.BUCKET, showTemperature)

    fun register(name: String, tagName: String, properties: FluidType.Properties, lightLevel: Int, bucketProperties: Properties): FluidObject<ForgeFlowingFluid> = this.register(name, tagName, properties, ForgeFlowingFluid::Source, ForgeFlowingFluid::Flowing, lightLevel, bucketProperties, false)

    fun register(name: String, properties: FluidType.Properties, lightLevel: Int, bucketProperties: Properties): FluidObject<ForgeFlowingFluid> = this.register(name, name, properties, lightLevel, bucketProperties, false)

    fun register(name: String, tagName: String, properties: FluidType.Properties, lightLevel: Int): FluidObject<ForgeFlowingFluid> = this.register(name, tagName, properties, lightLevel, ItemProperties.BUCKET, false)

    fun register(name: String, properties: FluidType.Properties, lightLevel: Int): FluidObject<ForgeFlowingFluid> = this.register(name, properties, lightLevel, ItemProperties.BUCKET, false)
}