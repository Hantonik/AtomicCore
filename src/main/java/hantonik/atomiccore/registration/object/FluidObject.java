package hantonik.atomiccore.registration.object;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

public class FluidObject<F extends ForgeFlowingFluid> implements Supplier<F>, ItemLike {
    @Nullable
    private final Supplier<? extends LiquidBlock> block;
    private final Supplier<? extends F> still;
    private final Supplier<? extends F> flowing;
    private TagKey<Fluid> localTag;
    private TagKey<Fluid> forgeTag;
    protected ResourceLocation id;

    public FluidObject(ResourceLocation id, String tagName, Supplier<? extends F> still, Supplier<? extends F> flowing, @Nullable Supplier<? extends LiquidBlock> block) {
        this.id = id;
        this.localTag = FluidTags.create(id);
        this.forgeTag = FluidTags.create(new ResourceLocation("forge", tagName));
        this.still = still;
        this.flowing = flowing;
        this.block = block;
    }

    @Deprecated
    public FluidObject(Supplier<? extends F> still, Supplier<? extends F> flowing, @Nullable Supplier<? extends LiquidBlock> block) {
        this.still = still;
        this.flowing = flowing;
        this.block = block;
    }

    public ResourceLocation getId() {
        if (this.id == null)
            this.id = Objects.requireNonNull(this.getStill().getRegistryName(), "Fluid has null ID");

        return this.id;
    }

    public F getStill() {
        return Objects.requireNonNull(this.still.get(), "Fluid object missing still fluid");
    }

    public F get() {
        return this.getStill();
    }

    public F getFlowing() {
        return Objects.requireNonNull(this.flowing.get(), "Fluid object missing flowing fluid");
    }

    @Nullable
    public LiquidBlock getBlock() {
        return this.block == null ? null : this.block.get();
    }

    public @NotNull Item asItem() {
        return this.still.get().getBucket();
    }

    public TagKey<Fluid> getLocalTag() {
        if (this.localTag == null)
            this.localTag = FluidTags.create(this.getId());

        return this.localTag;
    }

    public TagKey<Fluid> getForgeTag() {
        if (this.forgeTag == null)
            this.forgeTag = FluidTags.create(new ResourceLocation("forge", this.getId().getPath()));

        return this.forgeTag;
    }
}
