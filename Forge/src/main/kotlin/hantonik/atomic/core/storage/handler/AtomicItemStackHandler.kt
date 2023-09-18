package hantonik.atomic.core.storage.handler

import net.minecraft.core.NonNullList
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.ItemStack
import net.minecraftforge.items.ItemStackHandler

open class AtomicItemStackHandler(size: Int, private val onContentsChanged: ((Int) -> Unit)?) : ItemStackHandler(size) {
    private val slotSizeMap: MutableMap<Int, Int> = mutableMapOf()
    private var slotValidator: ((Int, ItemStack) -> Boolean)? = null
    private var maxStackSize = 64

    var outputsSlots: IntArray? = null

    constructor(size: Int) : this(size, null)

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (this.outputsSlots != null && this.outputsSlots!!.contains(slot))
            return stack

        return super.insertItem(slot, stack, simulate)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (this.outputsSlots != null && this.outputsSlots!!.contains(slot))
            return ItemStack.EMPTY

        return super.extractItem(slot, amount, simulate)
    }

    override fun getSlotLimit(slot: Int): Int {
        return if (this.slotSizeMap.containsKey(slot))
            this.slotSizeMap[slot]!!
        else
            this.maxStackSize
    }

    override fun isItemValid(slot: Int, stack: ItemStack) = this.slotValidator == null || this.slotValidator!!.invoke(slot, stack)

    override fun onContentsChanged(slot: Int) {
        if (this.onContentsChanged != null)
            this.onContentsChanged.invoke(slot)
    }

    fun insertItemSuper(slot: Int, stack: ItemStack, simulate: Boolean) = super.insertItem(slot, stack, simulate)

    fun extractItemSuper(slot: Int, amount: Int, simulate: Boolean) = super.extractItem(slot, amount, simulate)

    fun getStacks(): NonNullList<ItemStack> = this.stacks

    fun setDefaultSlotLimit(size: Int) {
        this.maxStackSize = size
    }

    fun addSlotLimit(slot: Int, size: Int) {
        this.slotSizeMap[slot] = size
    }

    fun setSlotValidator(validator: ((Int, ItemStack) -> Boolean)?) {
        this.slotValidator = validator
    }

    fun setOutputSlots(vararg slots: Int) {
        this.outputsSlots = slots
    }

    fun toIInventory(): Container {
        return SimpleContainer(*this.stacks.toTypedArray())
    }

    fun copy(): AtomicItemStackHandler {
        val copy = AtomicItemStackHandler(this.slots, this.onContentsChanged)

        copy.setDefaultSlotLimit(this.maxStackSize)
        copy.setSlotValidator(this.slotValidator)

        if (this.outputsSlots != null)
            copy.setOutputSlots(*this.outputsSlots!!)

        this.slotSizeMap.forEach(copy::addSlotLimit)

        for (index in 0 until this.slots)
            copy.setStackInSlot(index, this.getStackInSlot(index))

        return copy
    }
}