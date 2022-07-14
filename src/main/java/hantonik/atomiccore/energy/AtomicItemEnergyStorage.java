package hantonik.atomiccore.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class AtomicItemEnergyStorage extends AtomicEnergyStorage {
    private static final String NBT_ENERGY = "Energy";

    private final ItemStack stack;

    public AtomicItemEnergyStorage(ItemStack stack, int capacity) {
        super(capacity);

        this.stack = stack;
        this.energy = stack.hasTag() && stack.getTag().contains(NBT_ENERGY) ? stack.getTag().getInt(NBT_ENERGY) : 0;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        var received = super.receiveEnergy(maxReceive, simulate);

        if (received > 0 && !simulate) {
            if (!this.stack.hasTag())
                this.stack.setTag(new CompoundTag());

            this.stack.getTag().putInt(NBT_ENERGY, getEnergyStored());
        }

        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        var extracted = super.extractEnergy(maxExtract, simulate);

        if (extracted > 0 && !simulate) {
            if (!this.stack.hasTag())
                this.stack.setTag(new CompoundTag());

            this.stack.getTag().putInt(NBT_ENERGY, getEnergyStored());
        }

        return extracted;
    }
}
