package hantonik.atomic.core.storage;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import lombok.Setter;
import net.minecraft.util.Mth;
import net.minecraftforge.energy.EnergyStorage;

public class AtomicEnergyStorage extends EnergyStorage {
    @Setter
    private Runnable onContentsChanged;

    public AtomicEnergyStorage(int capacity) {
        this(capacity, capacity, capacity, 0, null);
    }

    public AtomicEnergyStorage(int capacity, Runnable onContentsChanged) {
        this(capacity, capacity, capacity, 0, onContentsChanged);
    }

    public AtomicEnergyStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, 0, null);
    }

    public AtomicEnergyStorage(int capacity, int maxTransfer, Runnable onContentsChanged) {
        this(capacity, maxTransfer, maxTransfer, 0, onContentsChanged);
    }

    public AtomicEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        this(capacity, maxReceive, maxExtract, 0, null);
    }

    public AtomicEnergyStorage(int capacity, int maxReceive, int maxExtract, Runnable onContentsChanged) {
        this(capacity, maxReceive, maxExtract, 0, onContentsChanged);
    }

    public AtomicEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        this(capacity, maxReceive, maxExtract, energy, null);
    }

    public AtomicEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, Runnable onContentsChanged) {
        super(capacity, maxReceive, maxExtract, energy);

        this.onContentsChanged = onContentsChanged;
    }

    public int getMaxReceive() {
        return this.maxReceive;
    }

    public int getMaxExtract() {
        return this.maxExtract;
    }

    @CanIgnoreReturnValue
    public AtomicEnergyStorage setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;

        if (this.onContentsChanged != null)
            this.onContentsChanged.run();

        return this;
    }

    @CanIgnoreReturnValue
    public AtomicEnergyStorage setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;

        if (this.onContentsChanged != null)
            this.onContentsChanged.run();

        return this;
    }

    @CanIgnoreReturnValue
    public AtomicEnergyStorage setCapacity(int amount) {
        this.capacity = amount;

        if (this.onContentsChanged != null)
            this.onContentsChanged.run();

        return this;
    }

    @CanIgnoreReturnValue
    public AtomicEnergyStorage setEnergy(int amount) {
        this.energy = Mth.clamp(amount, 0, this.capacity);

        if (this.onContentsChanged != null)
            this.onContentsChanged.run();

        return this;
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        var received = Mth.clamp(amount, 0, Math.min(this.capacity - this.energy, this.maxReceive));

        if (!simulate && received != 0) {
            this.energy += received;

            if (this.onContentsChanged != null)
                this.onContentsChanged.run();
        }

        return received;
    }

    @Override
    public int extractEnergy(int amount, boolean simulate) {
        var extracted = Mth.clamp(amount, 0, Math.min(this.energy, this.maxExtract));

        if (!simulate && extracted != 0) {
            this.energy -= extracted;

            if (this.onContentsChanged != null)
                this.onContentsChanged.run();
        }

        return extracted;
    }

    public int receiveBypassLimit(int amount, boolean simulate) {
        var received = Mth.clamp(amount, 0, this.capacity - this.energy);

        if (!simulate && received != 0) {
            this.energy += received;

            if (this.onContentsChanged != null)
                this.onContentsChanged.run();
        }

        return received;
    }

    public int extractBypassLimit(int amount, boolean simulate) {
        var extracted = Mth.clamp(amount, 0, this.energy);

        if (!simulate && extracted != 0) {
            this.energy -= extracted;

            if (this.onContentsChanged != null)
                this.onContentsChanged.run();
        }

        return extracted;
    }
}

//@Experimental
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
//public class AtomicEnergyStorage implements IEnergyStorage, INBTSerializable<Tag> {
//    private IntValue energy;
//    private IntValue capacity;
//    private IntValue maxReceive;
//    private IntValue maxExtract;
//
//    @Setter
//    private Runnable onContentsChanged;
//
//    public AtomicEnergyStorage(int capacity) {
//        this(capacity, capacity, capacity, 0, null);
//    }
//
//    public AtomicEnergyStorage(int capacity, Runnable onContentsChanged) {
//        this(capacity, capacity, capacity, 0, onContentsChanged);
//    }
//
//    public AtomicEnergyStorage(int capacity, int maxTransfer) {
//        this(capacity, maxTransfer, maxTransfer, 0, null);
//    }
//
//    public AtomicEnergyStorage(int capacity, int maxTransfer, Runnable onContentsChanged) {
//        this(capacity, maxTransfer, maxTransfer, 0, onContentsChanged);
//    }
//
//    public AtomicEnergyStorage(int capacity, int maxReceive, int maxExtract) {
//        this(capacity, maxReceive, maxExtract, 0, null);
//    }
//
//    public AtomicEnergyStorage(int capacity, int maxReceive, int maxExtract, Runnable onContentsChanged) {
//        this(capacity, maxReceive, maxExtract, 0, onContentsChanged);
//    }
//
//    public AtomicEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
//        this(capacity, maxReceive, maxExtract, energy, null);
//    }
//
//    public AtomicEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, Runnable onContentsChanged) {
//        this.capacity = new IntValue(capacity);
//        this.maxReceive = new IntValue(maxReceive);
//        this.maxExtract = new IntValue(maxExtract);
//        this.energy = new IntValue(Math.max(0 , Math.min(capacity, energy)));
//
//        this.onContentsChanged = onContentsChanged;
//    }
//
//    public AtomicEnergyStorage getForCapability(int maxReceive, int maxExtract) {
//        return new AtomicEnergyStorage(this.energy, this.capacity, new IntValue(Math.min(this.maxReceive.getValue(), maxReceive)), new IntValue(Math.min(this.maxExtract.getValue(), maxExtract)), this.onContentsChanged);
//    }
//
//    public int getMaxReceive() {
//        return this.maxReceive.getValue();
//    }
//
//    public int getMaxExtract() {
//        return this.maxExtract.getValue();
//    }
//
//    @CanIgnoreReturnValue
//    public AtomicEnergyStorage setMaxReceive(int maxReceive) {
//        this.maxReceive.setValue(maxReceive);
//
//        if (this.onContentsChanged != null)
//            this.onContentsChanged.run();
//
//        return this;
//    }
//
//    @CanIgnoreReturnValue
//    public AtomicEnergyStorage setMaxExtract(int maxExtract) {
//        this.maxExtract.setValue(maxExtract);
//
//        if (this.onContentsChanged != null)
//            this.onContentsChanged.run();
//
//        return this;
//    }
//
//    @CanIgnoreReturnValue
//    public AtomicEnergyStorage setCapacity(int amount) {
//        this.capacity.setValue(amount);
//
//        if (this.onContentsChanged != null)
//            this.onContentsChanged.run();
//
//        return this;
//    }
//
//    @CanIgnoreReturnValue
//    public AtomicEnergyStorage setEnergy(int amount) {
//        this.energy.setValue(Mth.clamp(amount, 0, this.capacity.getValue()));
//
//        if (this.onContentsChanged != null)
//            this.onContentsChanged.run();
//
//        return this;
//    }
//
//    @Override
//    public int receiveEnergy(int amount, boolean simulate) {
//        var received = Mth.clamp(amount, 0, Math.min(this.capacity.getValue() - this.energy.getValue(), this.maxReceive.getValue()));
//
//        if (!simulate && received != 0) {
//            this.energy.add(received);
//
//            if (this.onContentsChanged != null)
//                this.onContentsChanged.run();
//        }
//
//        return received;
//    }
//
//    @Override
//    public int extractEnergy(int amount, boolean simulate) {
//        var extracted = Mth.clamp(amount, 0, Math.min(this.energy.getValue(), this.maxExtract.getValue()));
//
//        if (!simulate && extracted != 0) {
//            this.energy.subtract(extracted);
//
//            if (this.onContentsChanged != null)
//                this.onContentsChanged.run();
//        }
//
//        return extracted;
//    }
//
//    public int receiveBypassLimit(int amount, boolean simulate) {
//        var received = Mth.clamp(amount, 0, this.capacity.getValue() - this.energy.getValue());
//
//        if (!simulate && received != 0) {
//            this.energy.add(received);
//
//            if (this.onContentsChanged != null)
//                this.onContentsChanged.run();
//        }
//
//        return received;
//    }
//
//    public int extractBypassLimit(int amount, boolean simulate) {
//        var extracted = Mth.clamp(amount, 0, this.energy.getValue());
//
//        if (!simulate && extracted != 0) {
//            this.energy.subtract(extracted);
//
//            if (this.onContentsChanged != null)
//                this.onContentsChanged.run();
//        }
//
//        return extracted;
//    }
//
//    @Override
//    public int getEnergyStored() {
//        return this.energy.getValue();
//    }
//
//    @Override
//    public int getMaxEnergyStored() {
//        return this.capacity.getValue();
//    }
//
//    @Override
//    public boolean canExtract() {
//        return this.maxExtract.getValue() > 0;
//    }
//
//    @Override
//    public boolean canReceive() {
//        return this.maxReceive.getValue() > 0;
//    }
//
//    @Override
//    public Tag serializeNBT() {
//        return IntTag.valueOf(this.getEnergyStored());
//    }
//
//    @Override
//    public void deserializeNBT(Tag tag) {
//        if (!(tag instanceof IntTag intTag))
//            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
//
//        this.energy.setValue(intTag.getAsInt());
//    }
//
//    @AllArgsConstructor
//    public static class IntValue {
//        @Getter
//        private int value;
//
//        @CanIgnoreReturnValue
//        public int setValue(int value) {
//            this.value = value;
//
//            return this.value;
//        }
//
//        @CanIgnoreReturnValue
//        public int add(int value) {
//            return this.value += value;
//        }
//
//        @CanIgnoreReturnValue
//        public int subtract(int value) {
//            return this.value -= value;
//        }
//
//        @CanIgnoreReturnValue
//        public int increment() {
//            return this.value++;
//        }
//
//        @CanIgnoreReturnValue
//        public int decrement() {
//            return this.value--;
//        }
//    }
//}
