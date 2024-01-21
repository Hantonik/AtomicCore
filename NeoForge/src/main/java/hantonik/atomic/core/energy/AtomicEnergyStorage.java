package hantonik.atomic.core.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.energy.EnergyStorage;
import org.jetbrains.annotations.Nullable;

public class AtomicEnergyStorage extends EnergyStorage {
    @Nullable
    private final ContentsChanged onContentsChanged;

    public AtomicEnergyStorage(int capacity, ContentsChanged onContentsChanged) {
        super(capacity);

        this.onContentsChanged = onContentsChanged;
    }

    public AtomicEnergyStorage(int capacity) {
        this(capacity, null);
    }

    public AtomicEnergyStorage(int capacity, int maxTransfer, ContentsChanged onContentsChanged) {
        super(capacity, maxTransfer);

        this.onContentsChanged = onContentsChanged;
    }

    public AtomicEnergyStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, null);
    }

    public AtomicEnergyStorage(int capacity, int maxReceive, int maxExtract, ContentsChanged onContentsChanged) {
        super(capacity, maxReceive, maxExtract);

        this.onContentsChanged = onContentsChanged;
    }

    public AtomicEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        this(capacity, maxReceive, maxExtract, null);
    }

    public AtomicEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, ContentsChanged onContentsChanged) {
        super(capacity, maxReceive, maxExtract, energy);

        this.onContentsChanged = onContentsChanged;
    }

    public AtomicEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        this(capacity, maxReceive, maxExtract, energy, null);
    }

    public int getMaxReceive() {
        return this.maxReceive;
    }

    public int getMaxExtract() {
        return this.maxExtract;
    }

    public void setEnergyStored(int energy) {
        var oldEnergy = this.energy;

        this.energy = energy;

        if (this.onContentsChanged != null)
            this.onContentsChanged.onContentsChanged(this, Content.ENERGY, this.energy - oldEnergy);
    }

    public void setCapacity(int capacity) {
        var oldCapacity = this.capacity;

        this.capacity = capacity;

        if (this.onContentsChanged != null)
            this.onContentsChanged.onContentsChanged(this, Content.CAPACITY, this.capacity - oldCapacity);
    }

    public void setMaxReceive(int maxReceive) {
        var oldMaxReceive = this.maxReceive;

        this.maxReceive = maxReceive;

        if (this.onContentsChanged != null)
            this.onContentsChanged.onContentsChanged(this, Content.MAX_RECEIVE, this.maxReceive - oldMaxReceive);
    }

    public void setMaxExtract(int maxExtract) {
        var oldMaxExtract = this.maxExtract;

        this.maxExtract = maxExtract;

        if (this.onContentsChanged != null)
            this.onContentsChanged.onContentsChanged(this, Content.MAX_EXTRACT, this.maxExtract - oldMaxExtract);
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        var received = Mth.clamp(amount, 0, Math.min(this.capacity - this.energy, this.maxReceive));

        if (!simulate && received != 0) {
            this.energy += received;

            if (this.onContentsChanged != null)
                this.onContentsChanged.onContentsChanged(this, Content.ENERGY, received);
        }

        return received;
    }

    @Override
    public int extractEnergy(int amount, boolean simulate) {
        var extracted = Mth.clamp(amount, 0, Math.min(this.energy, this.maxExtract));

        if (!simulate && extracted != 0) {
            this.energy -= extracted;

            if (this.onContentsChanged != null)
                this.onContentsChanged.onContentsChanged(this, Content.ENERGY, -extracted);
        }

        return extracted;
    }

    public int receiveBypassLimit(int amount, boolean simulate) {
        var received = Mth.clamp(amount, 0, this.capacity - this.energy);

        if (!simulate && received != 0) {
            this.energy += received;

            if (this.onContentsChanged != null)
                this.onContentsChanged.onContentsChanged(this, Content.ENERGY, received);
        }

        return received;
    }

    public int extractBypassLimit(int amount, boolean simulate) {
        var extracted = Mth.clamp(amount, 0, this.energy);

        if (!simulate && extracted != 0) {
            this.energy -= extracted;

            if (this.onContentsChanged != null)
                this.onContentsChanged.onContentsChanged(this, Content.ENERGY, -extracted);
        }

        return extracted;
    }

    @Override
    public CompoundTag serializeNBT() {
        var tag = new CompoundTag();

        tag.putInt("EnergyStored", this.energy);
        tag.putInt("EnergyCapacity", this.capacity);
        tag.putInt("EnergyMaxReceive", this.maxReceive);
        tag.putInt("EnergyMaxExtract", this.maxExtract);

        return tag;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        var tag = (CompoundTag) nbt;

        this.energy = tag.getInt("EnergyStored");
        this.capacity = tag.getInt("EnergyCapacity");
        this.maxReceive = tag.getInt("EnergyMaxReceive");
        this.maxExtract = tag.getInt("EnergyMaxExtract");
    }

    @FunctionalInterface
    public interface ContentsChanged {
        void onContentsChanged(AtomicEnergyStorage storage, Content content, int difference);
    }

    public enum Content {
        ENERGY,
        CAPACITY,
        MAX_RECEIVE,
        MAX_EXTRACT
    }
}
