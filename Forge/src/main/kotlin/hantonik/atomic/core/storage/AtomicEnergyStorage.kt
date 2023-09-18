package hantonik.atomic.core.storage

import com.google.errorprone.annotations.CanIgnoreReturnValue
import net.minecraft.util.Mth
import net.minecraftforge.energy.EnergyStorage
import kotlin.math.min

class AtomicEnergyStorage(capacity: Int, maxReceive: Int, maxExtract: Int, energy: Int, var onContentsChanged: (() -> Unit)?) : EnergyStorage(capacity, maxReceive, maxExtract, energy) {
    constructor(capacity: Int, maxReceive: Int, maxExtract: Int, energy: Int) : this(capacity, maxReceive, maxExtract, energy, null)

    constructor(capacity: Int, maxReceive: Int, maxExtract: Int, onContentsChanged: (() -> Unit)?) : this(capacity, maxReceive, maxExtract, 0, onContentsChanged)

    constructor(capacity: Int, maxReceive: Int, maxExtract: Int) : this(capacity, maxReceive, maxExtract, 0, null)

    constructor(capacity: Int, maxTransfer: Int, onContentsChanged: (() -> Unit)?) : this(capacity, maxTransfer, maxTransfer, 0, onContentsChanged)

    constructor(capacity: Int, maxTransfer: Int) : this(capacity, maxTransfer, maxTransfer, 0, null)

    constructor(capacity: Int, onContentsChanged: (() -> Unit)?) : this(capacity, capacity, capacity, 0, onContentsChanged)

    constructor(capacity: Int) : this(capacity, capacity, capacity, 0, null)

    fun getMaxReceive() = this.maxReceive

    fun getMaxExtract() = this.maxExtract

    @CanIgnoreReturnValue
    fun setMaxReceive(maxReceive: Int): AtomicEnergyStorage {
        this.maxReceive = maxReceive

        if (this.onContentsChanged != null)
            this.onContentsChanged!!.invoke()

        return this
    }

    @CanIgnoreReturnValue
    fun setMaxExtract(maxExtract: Int): AtomicEnergyStorage {
        this.maxExtract = maxExtract

        if (this.onContentsChanged != null)
            this.onContentsChanged!!.invoke()

        return this
    }

    @CanIgnoreReturnValue
    fun setCapacity(capacity: Int): AtomicEnergyStorage {
        this.capacity = capacity

        if (this.onContentsChanged != null)
            this.onContentsChanged!!.invoke()

        return this
    }

    @CanIgnoreReturnValue
    fun setEnergy(amount: Int): AtomicEnergyStorage {
        this.energy = Mth.clamp(amount, 0, this.capacity)

        if (this.onContentsChanged != null)
            this.onContentsChanged!!.invoke()

        return this
    }

    override fun receiveEnergy(amount: Int, simulate: Boolean): Int {
        val received = Mth.clamp(amount, 0, min(this.capacity - this.energy, this.maxReceive))

        if (!simulate && received != 0) {
            this.energy += received

            if (this.onContentsChanged != null)
                this.onContentsChanged!!.invoke()
        }

        return received
    }

    override fun extractEnergy(amount: Int, simulate: Boolean): Int {
        val extracted = Mth.clamp(amount, 0, min(this.energy, this.maxExtract))

        if (!simulate && extracted != 0) {
            this.energy -= extracted

            if (this.onContentsChanged != null)
                this.onContentsChanged!!.invoke()
        }

        return extracted
    }

    fun receiveEnergyBypassLimit(amount: Int, simulate: Boolean): Int {
        val received = Mth.clamp(amount, 0, this.capacity - this.energy)

        if (!simulate && received != 0) {
            this.energy += received

            if (this.onContentsChanged != null)
                this.onContentsChanged!!.invoke()
        }

        return received
    }

    fun extractEnergyBypassLimit(amount: Int, simulate: Boolean): Int {
        val extracted = Mth.clamp(amount, 0, this.energy)

        if (!simulate && extracted != 0) {
            this.energy -= extracted

            if (this.onContentsChanged != null)
                this.onContentsChanged!!.invoke()
        }

        return extracted
    }
}