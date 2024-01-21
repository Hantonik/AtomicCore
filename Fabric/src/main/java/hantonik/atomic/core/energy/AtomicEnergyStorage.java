package hantonik.atomic.core.energy;

import lombok.Getter;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

public class AtomicEnergyStorage extends SnapshotParticipant<CompoundTag> implements EnergyStorage {
    @Getter
    protected long energy;
    @Getter
    protected long capacity;
    @Getter
    protected long maxReceive;
    @Getter
    protected long maxExtract;

    @Nullable
    protected final ContentsChanged onContentsChanged;

    public AtomicEnergyStorage(long capacity) {
        this(capacity, null);
    }

    public AtomicEnergyStorage(long capacity, ContentsChanged onContentsChanged) {
        this(capacity, capacity, capacity, onContentsChanged);
    }

    public AtomicEnergyStorage(long capacity, long maxTransfer) {
        this(capacity, maxTransfer, null);
    }

    public AtomicEnergyStorage(long capacity, long maxTransfer, ContentsChanged onContentsChanged) {
        this(capacity, maxTransfer, maxTransfer, onContentsChanged);
    }

    public AtomicEnergyStorage(long capacity, long maxReceive, long maxExtract) {
        this(capacity, maxReceive, maxExtract, null);
    }

    public AtomicEnergyStorage(long capacity, long maxReceive, long maxExtract, ContentsChanged onContentsChanged) {
        this(capacity, maxReceive, maxExtract, 0, onContentsChanged);
    }

    public AtomicEnergyStorage(long capacity, long maxReceive, long maxExtract, long energy) {
        this(capacity, maxReceive, maxExtract, energy, null);
    }

    public AtomicEnergyStorage(long capacity, long maxReceive, long maxExtract, long energy, ContentsChanged onContentsChanged) {
        this.energy = energy;
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;

        this.onContentsChanged = onContentsChanged;
    }

    @Override
    public long getAmount() {
        return this.getEnergy();
    }

    public void setEnergy(long energy) {
        var oldEnergy = this.energy;

        this.energy = energy;

        if (this.onContentsChanged != null)
            this.onContentsChanged.onContentsChanged(this, Content.ENERGY, this.energy - oldEnergy);
    }

    public void setCapacity(long capacity) {
        var oldCapacity = this.capacity;

        this.capacity = capacity;

        if (this.onContentsChanged != null)
            this.onContentsChanged.onContentsChanged(this, Content.CAPACITY, this.capacity - oldCapacity);
    }

    public void setMaxReceive(long maxReceive) {
        var oldMaxReceive = this.maxReceive;

        this.maxReceive = maxReceive;

        if (this.onContentsChanged != null)
            this.onContentsChanged.onContentsChanged(this, Content.MAX_RECEIVE, this.maxReceive - oldMaxReceive);
    }

    public void setMaxExtract(long maxExtract) {
        var oldMaxExtract = this.maxExtract;

        this.maxExtract = maxExtract;

        if (this.onContentsChanged != null)
            this.onContentsChanged.onContentsChanged(this, Content.MAX_EXTRACT, this.maxExtract - oldMaxExtract);
    }


    @Override
    public long insert(long amount, TransactionContext transaction) {
        var received = Mth.clamp(amount, 0, Math.min(this.capacity - this.energy, this.maxReceive));

        if (received != 0) {
            this.updateSnapshots(transaction);

            this.energy += received;

            if (this.onContentsChanged != null)
                this.onContentsChanged.onContentsChanged(this, Content.ENERGY, received);

            return received;
        }

        return 0;
    }

    @Override
    public long extract(long amount, TransactionContext transaction) {
        var extracted = Mth.clamp(amount, 0, Math.min(this.energy, this.maxExtract));

        if (extracted != 0) {
            this.updateSnapshots(transaction);

            this.energy -= extracted;

            if (this.onContentsChanged != null)
                this.onContentsChanged.onContentsChanged(this, Content.ENERGY, -extracted);

            return extracted;
        }

        return 0;
    }

    public long insertBypassLimit(long amount, TransactionContext transaction) {
        var received = Mth.clamp(amount, 0, this.capacity - this.energy);

        if (received != 0) {
            this.updateSnapshots(transaction);

            this.energy += received;

            if (this.onContentsChanged != null)
                this.onContentsChanged.onContentsChanged(this, Content.ENERGY, received);

            return received;
        }

        return 0;
    }

    public long extractBypassLimit(long amount, TransactionContext transaction) {
        var extracted = Mth.clamp(amount, 0, this.energy);

        if (extracted != 0) {
            this.updateSnapshots(transaction);

            this.energy -= extracted;

            if (this.onContentsChanged != null)
                this.onContentsChanged.onContentsChanged(this, Content.ENERGY, -extracted);

            return extracted;
        }

        return 0;
    }

    @Override
    protected CompoundTag createSnapshot() {
        return this.serializeNBT();
    }

    @Override
    protected void readSnapshot(CompoundTag snapshot) {
        this.deserializeNBT(snapshot);
    }

    public CompoundTag serializeNBT() {
        var tag = new CompoundTag();

        tag.putLong("Energy", this.energy);
        tag.putLong("Capacity", this.capacity);
        tag.putLong("MaxReceive", this.maxReceive);
        tag.putLong("MaxExtract", this.maxExtract);

        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        this.energy = tag.getLong("Energy");
        this.capacity = tag.getLong("Capacity");
        this.maxReceive = tag.getLong("MaxReceive");
        this.maxExtract = tag.getLong("MaxExtract");
    }

    @FunctionalInterface
    public interface ContentsChanged {
        void onContentsChanged(AtomicEnergyStorage storage, Content content, long difference);
    }

    public enum Content {
        ENERGY,
        CAPACITY,
        MAX_RECEIVE,
        MAX_EXTRACT
    }
}
