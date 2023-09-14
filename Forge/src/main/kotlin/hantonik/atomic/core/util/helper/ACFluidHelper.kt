package hantonik.atomic.core.util.helper

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.TagParser
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.GsonHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.registries.ForgeRegistries

object ACFluidHelper {
    private val GSON = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

    fun serialize(stack: FluidStack): JsonObject {
        val json = JsonObject()

        json.addProperty("fluid", ForgeRegistries.FLUIDS.getKey(stack.fluid)?.toString())
        json.addProperty("count", stack.amount)

        if (stack.hasTag())
            json.addProperty("nbt", stack.tag!!.toString())

        return json
    }

    fun deserializeStack(json: JsonObject): FluidStack {
        val fluidName = GsonHelper.getAsString(json, "fluid")
        val amount = GsonHelper.getAsInt(json, "amount", 1000)
        val fluidKey = ResourceLocation(fluidName)

        if (!ForgeRegistries.FLUIDS.containsKey(fluidKey))
            throw JsonSyntaxException("Unknown item '$fluidName'")

        val fluid = ForgeRegistries.FLUIDS.getValue(fluidKey)

        if (json.has("nbt")) {
            try {
                val element = json["nbt"]

                val nbt = if (element.isJsonObject)
                    TagParser.parseTag(GSON.toJson(element))
                else
                    TagParser.parseTag(GsonHelper.convertToString(element, "nbt"))

                val tmp = CompoundTag()

                tmp.put("tag", nbt)
                tmp.putString("fluid", fluidName)
                tmp.putInt("amount", amount)

                return FluidStack.loadFluidStackFromNBT(tmp)
            } catch (e: CommandSyntaxException) {
                throw JsonSyntaxException("Invalid NBT Entry: $e")
            }
        }

        return FluidStack(fluid, amount)
    }

    fun serialize(fluid: Fluid): JsonObject {
        val json = JsonObject()

        json.addProperty("fluid", ForgeRegistries.FLUIDS.getKey(fluid)!!.toString())

        return json
    }

    fun deserializeFluid(json: JsonObject): Fluid {
        if (json.has("fluid")) {
            val element = json.get("fluid")

            if (element.isJsonPrimitive) {
                val id = element.asString

                return ForgeRegistries.FLUIDS.getDelegate(ResourceLocation(id)).orElseThrow { JsonSyntaxException("Expected fluid to be an fluid, was unknown string '$id'") }.value()
            } else
                throw JsonSyntaxException("Expected fluid to be an item, was '${GsonHelper.getType(element)}'")
        } else
            throw JsonSyntaxException("Missing fluid, expected to find an item")
    }

    fun withAmount(stack: FluidStack, amount: Int): FluidStack {
        if (stack.isEmpty)
            return FluidStack.EMPTY

        if (amount <= 0)
            return FluidStack.EMPTY

        val fluid = stack.copy()
        fluid.amount = amount

        return fluid
    }

    fun grow(stack: FluidStack, amount: Int) = this.withAmount(stack, stack.amount + amount)

    fun shrink(stack: FluidStack, amount: Int) = this.withAmount(stack, stack.amount - amount)

    fun canCombineStacks(stack1: FluidStack, stack2: FluidStack, maxAmount: Int): Boolean {
        if (!stack1.isEmpty && stack2.isEmpty)
            return true

        return stack1.isFluidEqual(stack2) && FluidStack.areFluidStackTagsEqual(stack1, stack2) && (stack1.amount + stack2.amount) <= maxAmount
    }

    fun combineStacks(stack1: FluidStack, stack2: FluidStack): FluidStack {
        if (stack1.isEmpty)
            return stack2.copy()

        return this.grow(stack1, stack2.amount)
    }
}