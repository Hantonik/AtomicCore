package hantonik.atomiccore.registration.deferred;

import hantonik.atomiccore.registration.DelayedSupplier;
import hantonik.atomiccore.registration.FluidBuilder;
import hantonik.atomiccore.registration.ItemProperties;
import hantonik.atomiccore.registration.object.FluidObject;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings({"unused", "WeakerAccess"})
public class FluidDeferredRegister extends DeferredRegisterWrapper<Fluid> {
    private final DeferredRegister<Block> blockRegister;
    private final DeferredRegister<Item> itemRegister;

    public FluidDeferredRegister(String modId) {
        super(ForgeRegistries.FLUIDS, modId);

        this.blockRegister = DeferredRegister.create(ForgeRegistries.BLOCKS, modId);
        this.itemRegister = DeferredRegister.create(ForgeRegistries.ITEMS, modId);
    }

    public void register(IEventBus bus) {
        super.register(bus);

        this.blockRegister.register(bus);
        this.itemRegister.register(bus);
    }

    public <I extends Fluid> RegistryObject<I> registerFluid(final String name, final Supplier<? extends I> sup) {
        return this.register.register(name, sup);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, String tagName, FluidBuilder builder, Function<Properties, ? extends F> still, Function<Properties, ? extends F> flowing, Function<Supplier<? extends FlowingFluid>, ? extends LiquidBlock> block) {
        DelayedSupplier<F> stillDelayed = new DelayedSupplier<>();
        DelayedSupplier<F> flowingDelayed = new DelayedSupplier<>();

        RegistryObject<LiquidBlock> blockObj = this.blockRegister.register(name + "_fluid", () -> block.apply(stillDelayed));
        builder.bucket(this.itemRegister.register(name + "_bucket", () -> new BucketItem(stillDelayed, ItemProperties.BUCKET_PROPS)));

        var props = builder.block(blockObj).build(stillDelayed, flowingDelayed);

        Supplier<F> stillSup = registerFluid(name, () -> still.apply(props));
        stillDelayed.setSupplier(stillSup);

        Supplier<F> flowingSup = registerFluid("flowing_" + name, () -> flowing.apply(props));
        flowingDelayed.setSupplier(flowingSup);

        return new FluidObject<>(resource(name), tagName, stillSup, flowingSup, blockObj);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, FluidBuilder builder, Function<Properties, ? extends F> still, Function<Properties, ? extends F> flowing, Function<Supplier<? extends FlowingFluid>, ? extends LiquidBlock> block) {
        return register(name, name, builder, still, flowing, block);
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, String tagName, FluidType.Properties properties, Function<Properties, ? extends F> still, Function<Properties, ? extends F> flowing, Material material, int lightLevel) {
        return register(name, tagName, new FluidBuilder(properties).explosionResistance(100F), still, flowing, (fluid) -> new LiquidBlock(fluid, Block.Properties.of(material).air().strength(100.0F).noLootTable().lightLevel((state) -> lightLevel)));
    }

    public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, FluidType.Properties properties, Function<Properties, ? extends F> still, Function<Properties, ? extends F> flowing, Material material, int lightLevel) {
        return register(name, name, properties, still, flowing, material, lightLevel);
    }

    public FluidObject<ForgeFlowingFluid> register(String name, String tagName, FluidType.Properties properties, Material material, int lightLevel) {
        return register(name, tagName, properties, ForgeFlowingFluid.Source::new, ForgeFlowingFluid.Flowing::new, material, lightLevel);
    }

    public FluidObject<ForgeFlowingFluid> register(String name, FluidType.Properties properties, Material material, int lightLevel) {
        return register(name, name, properties, material, lightLevel);
    }
}
