package hantonik.atomic.core.util.helper

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
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
}