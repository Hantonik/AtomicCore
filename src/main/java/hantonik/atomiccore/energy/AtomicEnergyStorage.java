package hantonik.atomiccore.energy;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import net.minecraftforge.energy.EnergyStorage;

public class AtomicEnergyStorage extends EnergyStorage {
    public AtomicEnergyStorage(int capacity) {
        super(capacity);
    }

    public AtomicEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public AtomicEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public AtomicEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    @CanIgnoreReturnValue
    public AtomicEnergyStorage setEnergy(int amount) {
        this.energy = Math.min(this.capacity, amount);

        return this;
    }

    @CanIgnoreReturnValue
    public AtomicEnergyStorage decreaseEnergy(int amount) {
        this.setEnergy(this.getEnergyStored() - amount);

        return this;
    }

    @CanIgnoreReturnValue
    public AtomicEnergyStorage increaseEnergy(int amount) {
        this.setEnergy(this.getEnergyStored() + amount);

        return this;
    }

    @CanIgnoreReturnValue
    public AtomicEnergyStorage setCapacity(int amount) {
        this.capacity = amount;

        return this;
    }
}
