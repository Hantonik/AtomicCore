package hantonik.atomic.core.storage

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

    var maxReceive: Int
        get() = super.maxReceive
        set(value) {
            super.maxReceive = value

            if (this.onContentsChanged != null)
                this.onContentsChanged!!.invoke()
        }

    var maxExtract: Int
        get() = super.maxExtract
        set(value) {
            super.maxExtract = value

            if (this.onContentsChanged != null)
                this.onContentsChanged!!.invoke()
        }

    var capacity: Int
        get() = super.capacity
        set(value) {
            super.capacity = value

            if (this.onContentsChanged != null)
                this.onContentsChanged!!.invoke()
        }

    var energy: Int
        get() = super.energy
        set(value) {
            super.energy = Mth.clamp(value, 0, super.capacity)

            if (this.onContentsChanged != null)
                this.onContentsChanged!!.invoke()
        }

    override fun receiveEnergy(amount: Int, simulate: Boolean): Int {
        val received = Mth.clamp(amount, 0, min(super.capacity - super.energy, super.maxReceive))

        if (!simulate && received != 0) {
            super.energy += received

            if (this.onContentsChanged != null)
                this.onContentsChanged!!.invoke()
        }

        return received
    }

    override fun extractEnergy(amount: Int, simulate: Boolean): Int {
        val extracted = Mth.clamp(amount, 0, min(super.energy, super.maxExtract))

        if (!simulate && extracted != 0) {
            super.energy -= extracted

            if (this.onContentsChanged != null)
                this.onContentsChanged!!.invoke()
        }

        return extracted
    }

    fun receiveBypassLimit(amount: Int, simulate: Boolean): Int {
        val received = Mth.clamp(amount, 0, super.capacity - super.energy)

        if (!simulate && received != 0) {
            super.energy += received

            if (this.onContentsChanged != null)
                this.onContentsChanged!!.invoke()
        }

        return received
    }

    fun extractBypassLimit(amount: Int, simulate: Boolean): Int {
        val extracted = Mth.clamp(amount, 0, super.energy)

        if (!simulate && extracted != 0) {
            super.energy -= extracted

            if (this.onContentsChanged != null)
                this.onContentsChanged!!.invoke()
        }

        return extracted
    }
}