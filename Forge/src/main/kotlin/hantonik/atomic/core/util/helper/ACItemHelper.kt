package hantonik.atomic.core.util.helper

import com.google.gson.JsonObject
import net.minecraft.util.GsonHelper
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.registries.ForgeRegistries

object ACItemHelper {
    fun serialize(item: Item): JsonObject {
        val json = JsonObject()

        json.addProperty("item", ForgeRegistries.ITEMS.getKey(item)!!.toString())

        return json
    }

    fun serialize(stack: ItemStack): JsonObject {
        val json = JsonObject()

        json.addProperty("item", ForgeRegistries.ITEMS.getKey(stack.item)!!.toString())

        if (stack.count != 1)
            json.addProperty("count", stack.count)

        if (stack.hasTag())
            json.addProperty("nbt", stack.tag!!.toString())

        return json
    }

    fun deserializeItem(json: JsonObject): Item = GsonHelper.getAsItem(json, "item")

    fun deserializeStack(json: JsonObject): ItemStack = CraftingHelper.getItemStack(json, true)

    fun withSize(stack: ItemStack, size: Int, container: Boolean): ItemStack {
        if (size <= 0) {
            return if (container && stack.hasCraftingRemainingItem())
                stack.craftingRemainingItem
            else
                ItemStack.EMPTY
        }

        val stackCopy = stack.copy()
        stackCopy.count = size

        return stackCopy
    }

    fun grow(stack: ItemStack, amount: Int): ItemStack = this.withSize(stack, stack.count + amount, false)

    fun shrink(stack: ItemStack, amount: Int, container: Boolean): ItemStack {
        if (stack.isEmpty)
            return ItemStack.EMPTY

        return this.withSize(stack, stack.count - amount, container)
    }

    fun areItemsEqual(stack1: ItemStack, stack2: ItemStack): Boolean {
        if (stack1.isEmpty && stack2.isEmpty)
            return true

        return !stack1.isEmpty && ItemStack.isSameItem(stack1, stack2)
    }

    fun areStacksEqual(stack1: ItemStack, stack2: ItemStack) = this.areItemsEqual(stack1, stack2) && ItemStack.isSameItemSameTags(stack1, stack2)

    fun canCombineStacks(stack1: ItemStack, stack2: ItemStack): Boolean {
        if (!stack1.isEmpty && stack2.isEmpty)
            return true

        return this.areStacksEqual(stack1, stack2) && (stack1.count + stack2.count) <= stack1.maxStackSize
    }

    fun combineStacks(stack1: ItemStack, stack2: ItemStack): ItemStack {
        if (stack1.isEmpty)
            return stack2.copy()

        return this.grow(stack1, stack2.count)
    }
}