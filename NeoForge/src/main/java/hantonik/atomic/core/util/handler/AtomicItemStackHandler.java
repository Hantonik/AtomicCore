package hantonik.atomic.core.util.handler;

import com.google.common.collect.Maps;
import lombok.Setter;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class AtomicItemStackHandler extends ItemStackHandler {
    private final Map<Integer, Integer> slotSizeMap = Maps.newHashMap();
    @Nullable
    private final Consumer<Integer> onContentsChanged;
    @Setter
    @Nullable
    private BiFunction<Integer, ItemStack, Boolean> slotValidator;
    private List<Integer> outputSlots = Lists.newArrayList();
    @Setter
    private int defaultSlotLimit = 64;

    public AtomicItemStackHandler(int size, Consumer<Integer> onContentsChanged) {
        super(size);

        this.onContentsChanged = onContentsChanged;
    }

    public AtomicItemStackHandler(int size) {
        this(size, null);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (this.outputSlots != null && this.outputSlots.contains(slot))
            return stack;

        return super.insertItem(slot, stack, simulate);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (this.outputSlots != null && this.outputSlots.contains(slot))
            return ItemStack.EMPTY;

        return super.extractItem(slot, amount, simulate);
    }

    @NotNull
    public ItemStack insertItemSuper(int slot, @NotNull ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    @NotNull
    public ItemStack extractItemSuper(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        if (this.slotSizeMap.containsKey(slot))
            return this.slotSizeMap.get(slot);

        return this.defaultSlotLimit;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return this.slotValidator == null || this.slotValidator.apply(slot, stack);
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (this.onContentsChanged != null)
            this.onContentsChanged.accept(slot);
        else
            super.onContentsChanged(slot);
    }

    public void addSlotLimit(int slot, int size) {
        this.slotSizeMap.put(slot, size);
    }

    public void setOutputSlots(int... slots) {
        this.outputSlots.clear();

        for (var slot : slots)
            this.outputSlots.add(slot);
    }

    public Container toContainer() {
        return new SimpleContainer(this.stacks.toArray(ItemStack[]::new));
    }

    public AtomicItemStackHandler copy() {
        var copy = new AtomicItemStackHandler(this.getSlots(), this.onContentsChanged);

        copy.defaultSlotLimit = this.defaultSlotLimit;
        copy.slotValidator = this.slotValidator;
        copy.outputSlots = this.outputSlots;
        copy.slotSizeMap.putAll(this.slotSizeMap);
        copy.stacks.addAll(this.stacks);

        return copy;
    }
}
