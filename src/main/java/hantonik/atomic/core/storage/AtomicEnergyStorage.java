package hantonik.atomic.core.storage;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

public class AtomicEnergyStorage implements IEnergyStorage, INetworkHandler, IStorage<AtomicEnergyStorage>, INBTSerializable<CompoundTag> {
    protected final int baseCapacity;
    protected final int baseMaxReceive;
    protected final int baseMaxExtract;

    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public AtomicEnergyStorage(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public AtomicEnergyStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public AtomicEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public AtomicEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        this.baseCapacity = capacity;
        this.capacity = capacity;

        this.baseMaxReceive = maxReceive;
        this.maxReceive = maxReceive;

        this.baseMaxExtract = maxExtract;
        this.maxExtract = maxExtract;

        this.energy = Mth.clamp(energy, 0, capacity);
    }

    @Override
    @CanIgnoreReturnValue
    public AtomicEnergyStorage setCapacity(int capacity) {
        this.capacity = Math.max(0, capacity);
        this.energy = Math.max(0, Math.min(capacity, this.energy));

        return this;
    }

    @CanIgnoreReturnValue
    public AtomicEnergyStorage setMaxReceive(int maxReceive) {
        this.maxReceive = Mth.clamp(maxReceive, 0, this.capacity);

        return this;
    }

    @CanIgnoreReturnValue
    public AtomicEnergyStorage setMaxExtract(int maxExtract) {
        this.maxExtract = Mth.clamp(maxExtract, 0, this.capacity);

        return this;
    }

    @CanIgnoreReturnValue
    public AtomicEnergyStorage setEnergyStoredCreative(int amount) {
        this.energy = Math.min(amount, 0);

        return this;
    }

    @CanIgnoreReturnValue
    public AtomicEnergyStorage setEnergyStored(int amount) {
        this.energy = Mth.clamp(amount, 0, this.capacity);

        return this;
    }

    public int getMaxReceive() {
        return this.maxReceive;
    }

    public int getMaxExtract() {
        return this.maxExtract;
    }

    public int receiveEnergyCreative(int amount, boolean simulate) {
        int received = Mth.clamp(amount, 0, this.capacity - this.energy);

        if (!simulate)
            this.energy += received;

        return received;
    }

    public int extractEnergyCreative(int amount, boolean simulate) {
        int extracted = Mth.clamp(amount, 0, this.energy);

        if (!simulate)
            this.energy -= extracted;

        return extracted;
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        int received = Mth.clamp(amount, 0, Math.min(this.capacity - this.energy, this.maxReceive));

        if (!simulate)
            this.energy += received;

        return received;
    }

    @Override
    public int extractEnergy(int amount, boolean simulate) {
        int extracted = Mth.clamp(amount, 0, Math.min(this.energy, this.maxExtract));

        if (!simulate)
            this.energy -= extracted;

        return extracted;
    }

    @Override
    public void readFromBuffer(FriendlyByteBuf buffer) {
        this.setCapacity(buffer.readInt());
        this.setEnergyStored(buffer.readInt());
        this.setMaxReceive(buffer.readInt());
        this.setMaxExtract(buffer.readInt());
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeInt(this.getCapacity());
        buffer.writeInt(this.getStored());
        buffer.writeInt(this.getMaxReceive());
        buffer.writeInt(this.getMaxExtract());
    }

    @CanIgnoreReturnValue
    public AtomicEnergyStorage read(CompoundTag tag) {
        this.energy = tag.getInt("EnergyStored");
        this.capacity = tag.getInt("EnergyCapacity");
        this.maxReceive = tag.getInt("EnergyMaxReceive");
        this.maxExtract = tag.getInt("EnergyMaxExtract");

        return this;
    }

    public CompoundTag write(CompoundTag tag) {
        tag.putInt("Energy", this.energy);
        tag.putInt("EnergyCapacity", this.capacity);
        tag.putInt("EnergyMaxReceive", this.maxReceive);
        tag.putInt("EnergyMaxExtract", this.maxExtract);

        return tag;
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.write(new CompoundTag());
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.read(tag);
    }

    @Override
    public int getEnergyStored() {
        return this.energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return this.capacity;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }

    @Override
    public void clear() {
        this.energy = 0;
        this.capacity = this.baseCapacity;
        this.maxReceive = this.baseMaxReceive;
        this.maxExtract = this.baseMaxExtract;
    }

    @Override
    public boolean isEmpty() {
        return this.energy <= 0 && this.capacity > 0;
    }

    @Override
    public int getCapacity() {
        return this.getMaxEnergyStored();
    }

    @Override
    public int getStored() {
        return this.getEnergyStored();
    }

    @Override
    public StorageUnit getUnit() {
        return StorageUnit.ENERGY;
    }
}
