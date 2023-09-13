package hantonik.atomic.core.registration.object;

import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Supplier;

public class FluidObject<F extends ForgeFlowingFluid> implements Supplier<F>, ItemLike {
    protected ResourceLocation id;

    @Nullable
    private final Supplier<? extends LiquidBlock> block;
    private final Supplier<? extends F> still;
    private final Supplier<? extends F> flowing;

    @Getter
    private TagKey<Fluid> localTag;
    @Getter
    private TagKey<Fluid> forgeTag;

    public FluidObject(ResourceLocation id, String tagName, Supplier<? extends F> still, Supplier<? extends F> flowing, @Nullable Supplier<? extends LiquidBlock> block) {
        this.id = id;

        this.block = block;
        this.still = still;
        this.flowing = flowing;

        this.localTag = FluidTags.create(id);
        this.localTag = FluidTags.create(new ResourceLocation("forge", tagName));
    }

    @Deprecated
    public FluidObject(Supplier<? extends F> still, Supplier<? extends F> flowing, @Nullable Supplier<? extends LiquidBlock> block) {
        this.block = block;
        this.still = still;
        this.flowing = flowing;

        this.localTag = FluidTags.create(this.getId());
        this.forgeTag = FluidTags.create(new ResourceLocation("forge", this.getId().getPath()));
    }

    public ResourceLocation getId() {
        if (this.id == null)
            this.id = Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(this.getStill()), "Fluid has null ID");

        return this.id;
    }

    @Nullable
    public LiquidBlock getBlock() {
        return this.block == null ? null : this.block.get();
    }

    public F getStill() {
        return Objects.requireNonNull(this.still.get(), "Fluid object missing still fluid");
    }

    public F getFlowing() {
        return Objects.requireNonNull(this.flowing.get(), "Fluid object missing flowing fluid");
    }

    @Override
    public F get() {
        return this.getStill();
    }

    @Nonnull
    public Item asItem() {
        return this.still.get().getBucket();
    }
}
