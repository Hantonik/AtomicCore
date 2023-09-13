package hantonik.atomic.core.registration.deferred;

import hantonik.atomic.core.AtomicCore;
import hantonik.atomic.core.client.model.fluid.ModelFluidType;
import hantonik.atomic.core.registration.DelayedSupplier;
import hantonik.atomic.core.registration.FluidBuilder;
import hantonik.atomic.core.registration.ItemProperties;
import hantonik.atomic.core.registration.object.FluidObject;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class FluidDeferredRegister extends DeferredRegisterWrapper<Fluid> {
    private final DeferredRegister<FluidType> fluidTypeRegistry;
    private final DeferredRegister<Block> blockRegistry;
    private final DeferredRegister<Item> itemRegistry;

    public FluidDeferredRegister(IForgeRegistry<Fluid> registry, String modId, DeferredRegister<FluidType> fluidTypeRegistry, DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry) {
        super(registry, modId);

        this.fluidTypeRegistry = fluidTypeRegistry;
        this.blockRegistry = blockRegistry;
        this.itemRegistry = itemRegistry;
    }

    public FluidDeferredRegister(String modId) {
        this(ForgeRegistries.FLUIDS, modId, DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, modId), DeferredRegister.create(ForgeRegistries.BLOCKS, modId), DeferredRegister.create(ForgeRegistries.ITEMS, modId));
    }

    @Override
    public void register(IEventBus bus) {
        super.register(bus);
        
        this.fluidTypeRegistry.register(bus);
        this.blockRegistry.register(bus);
        this.itemRegistry.register(bus);
    }
    
    public <I extends Fluid> RegistryObject<I> registerFluid(final String name, final Supplier<? extends I> fluid) {
        return this.register.register(name, fluid);
    }
    
    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, String tagName, FluidBuilder builder, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, Function<Supplier<? extends FlowingFluid>, ? extends LiquidBlock> block, Item.Properties bucketProperties, boolean showTemperature) {
        var stillDelayed = new DelayedSupplier<F>();
        var flowingDelayed = new DelayedSupplier<F>();
        
        var blockObj = this.blockRegistry.register(name + "_fluid", () -> block.apply(stillDelayed));
        
        builder.bucket(this.itemRegistry.register(name + "_bucket", () -> new BucketItem(stillDelayed, bucketProperties) {
            @Override
            public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, components, isAdvanced);
                
                if (showTemperature)
                    components.add(Component.translatable("tooltip." + AtomicCore.MOD_ID + ".temperature").withStyle(ChatFormatting.GRAY).append(": ").append(Component.literal(stillDelayed.get().getFluidType().getTemperature() + "K").withStyle(ChatFormatting.RED)));
            }
        }));
        
        var stillProps = builder.block(blockObj).build(builder.getStillFluidType(stillDelayed), stillDelayed, flowingDelayed);
        Supplier<F> stillSupplier = this.registerFluid(name, () -> still.apply(stillProps));
        stillDelayed.setSupplier(stillSupplier);
        this.fluidTypeRegistry.register(name, builder.getStillFluidType(stillSupplier));
        
        var flowingProps = builder.block(blockObj).build(builder.getFlowingFluidType(flowingDelayed), stillDelayed, flowingDelayed);
        Supplier<F> flowingSupplier = this.registerFluid("flowing_" + name, () -> flowing.apply(flowingProps));
        flowingDelayed.setSupplier(flowingSupplier);
        this.fluidTypeRegistry.register("flowing_" + name, builder.getFlowingFluidType(flowingSupplier));
        
        return new FluidObject<>(this.resource(name), tagName, stillSupplier, flowingSupplier, blockObj);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, FluidBuilder builder, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, Function<Supplier<? extends FlowingFluid>, ? extends LiquidBlock> block, Item.Properties bucketProperties, boolean showTemperature) {
        return this.register(name, name, builder, still, flowing, block, bucketProperties, showTemperature);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, String tagName, ModelFluidType.Properties properties, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, int lightLevel, Item.Properties bucketProperties, boolean showTemperature) {
        return this.register(name, tagName, new FluidBuilder(properties, new ResourceLocation(this.modId, name)).explosionResistance(100F), still, flowing, (fluid) -> new LiquidBlock(fluid, Block.Properties.of().air().strength(100.0F).noLootTable().lightLevel((state) -> lightLevel)) {
            @Override
            public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> components, TooltipFlag flag) {
                super.appendHoverText(stack, level, components, flag);

                if (showTemperature)
                    components.add(Component.translatable("tooltip." + AtomicCore.MOD_ID + ".temperature").withStyle(ChatFormatting.GRAY).append(": ").append(Component.literal(fluid.get().getFluidType().getTemperature() + "K").withStyle(ChatFormatting.RED)));
            }

            @Override
            public String getDescriptionId() {
                return "block." + modId + "." + name + "_fluid";
            }
        }, bucketProperties, showTemperature);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, ModelFluidType.Properties properties, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, int lightLevel, Item.Properties bucketProperties, boolean showTemperature) {
        return this.register(name, name, properties, still, flowing, lightLevel, bucketProperties, showTemperature);
    }

    public FluidObject<ForgeFlowingFluid> register(String name, String tagName, ModelFluidType.Properties properties, int lightLevel, Item.Properties bucketProperties, boolean showTemperature) {
        return this.register(name, tagName, properties, ForgeFlowingFluid.Source::new, ForgeFlowingFluid.Flowing::new, lightLevel, bucketProperties, showTemperature);
    }

    public FluidObject<ForgeFlowingFluid> register(String name, ModelFluidType.Properties properties, int lightLevel, Item.Properties bucketProperties, boolean showTemperature) {
        return this.register(name, name, properties, lightLevel, bucketProperties, showTemperature);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, String tagName, FluidBuilder builder, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, Function<Supplier<? extends FlowingFluid>, ? extends LiquidBlock> block, boolean showTemperature) {
        return this.register(name, tagName, builder, still, flowing, block, ItemProperties.BUCKET_PROPERTIES, showTemperature);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, FluidBuilder builder, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, Function<Supplier<? extends FlowingFluid>, ? extends LiquidBlock> block, boolean showTemperature) {
        return this.register(name, builder, still, flowing, block, ItemProperties.BUCKET_PROPERTIES, showTemperature);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, String tagName, ModelFluidType.Properties properties, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, int lightLevel, boolean showTemperature) {
        return this.register(name, tagName, properties, still, flowing, lightLevel, ItemProperties.BUCKET_PROPERTIES, showTemperature);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, ModelFluidType.Properties properties, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, int lightLevel, boolean showTemperature) {
        return this.register(name, properties, still, flowing, lightLevel, ItemProperties.BUCKET_PROPERTIES, showTemperature);
    }

    public FluidObject<ForgeFlowingFluid> register(String name, String tagName, ModelFluidType.Properties properties, int lightLevel, boolean showTemperature) {
        return this.register(name, tagName, properties, lightLevel, ItemProperties.BUCKET_PROPERTIES, showTemperature);
    }

    public FluidObject<ForgeFlowingFluid> register(String name, ModelFluidType.Properties properties, int lightLevel, boolean showTemperature) {
        return this.register(name, properties, lightLevel, ItemProperties.BUCKET_PROPERTIES, showTemperature);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, FluidBuilder builder, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, Function<Supplier<? extends FlowingFluid>, ? extends LiquidBlock> block, Item.Properties bucketProperties) {
        return this.register(name, name, builder, still, flowing, block, bucketProperties, false);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, String tagName, ModelFluidType.Properties properties, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, int lightLevel, Item.Properties bucketProperties) {
        return this.register(name, tagName, new FluidBuilder(properties, new ResourceLocation(this.modId, name)).explosionResistance(100F), still, flowing, (fluid) -> new LiquidBlock(fluid, Block.Properties.of().air().strength(100.0F).noLootTable().lightLevel((state) -> lightLevel)) {
            @Override
            public String getDescriptionId() {
                return "block." + modId + "." + name + "_fluid";
            }
        }, bucketProperties, false);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, ModelFluidType.Properties properties, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, int lightLevel, Item.Properties bucketProperties) {
        return this.register(name, name, properties, still, flowing, lightLevel, bucketProperties, false);
    }

    public FluidObject<ForgeFlowingFluid> register(String name, String tagName, ModelFluidType.Properties properties, int lightLevel, Item.Properties bucketProperties) {
        return this.register(name, tagName, properties, ForgeFlowingFluid.Source::new, ForgeFlowingFluid.Flowing::new, lightLevel, bucketProperties, false);
    }

    public FluidObject<ForgeFlowingFluid> register(String name, ModelFluidType.Properties properties, int lightLevel, Item.Properties bucketProperties) {
        return this.register(name, name, properties, lightLevel, bucketProperties, false);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, String tagName, FluidBuilder builder, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, Function<Supplier<? extends FlowingFluid>, ? extends LiquidBlock> block) {
        return this.register(name, tagName, builder, still, flowing, block, ItemProperties.BUCKET_PROPERTIES, false);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, FluidBuilder builder, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, Function<Supplier<? extends FlowingFluid>, ? extends LiquidBlock> block) {
        return this.register(name, builder, still, flowing, block, ItemProperties.BUCKET_PROPERTIES, false);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, String tagName, ModelFluidType.Properties properties, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, int lightLevel) {
        return this.register(name, tagName, properties, still, flowing, lightLevel, ItemProperties.BUCKET_PROPERTIES, false);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, ModelFluidType.Properties properties, Function<ForgeFlowingFluid.Properties, ? extends F> still, Function<ForgeFlowingFluid.Properties, ? extends F> flowing, int lightLevel) {
        return this.register(name, properties, still, flowing, lightLevel, ItemProperties.BUCKET_PROPERTIES, false);
    }

    public FluidObject<ForgeFlowingFluid> register(String name, String tagName, ModelFluidType.Properties properties, int lightLevel) {
        return this.register(name, tagName, properties, lightLevel, ItemProperties.BUCKET_PROPERTIES, false);
    }

    public FluidObject<ForgeFlowingFluid> register(String name, ModelFluidType.Properties properties, int lightLevel) {
        return this.register(name, properties, lightLevel, ItemProperties.BUCKET_PROPERTIES, false);
    }
}
