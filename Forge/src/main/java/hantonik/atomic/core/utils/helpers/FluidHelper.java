package hantonik.atomic.core.utils.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * Some helpful code for fluids. <p>
 *
 * @author Hantonik
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FluidHelper {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static JsonObject serialize(FluidStack stack) {
        var json = new JsonObject();

        json.addProperty("fluid", Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(stack.getFluid())).toString());
        json.addProperty("count", stack.getAmount());

        if (stack.hasTag())
            json.addProperty("nbt", Objects.requireNonNull(stack.getTag()).toString());

        return json;
    }

    public static FluidStack deserializeStack(JsonObject json) {
        var fluidName = GsonHelper.getAsString(json, "fluid");
        var amount = GsonHelper.getAsInt(json, "amount", 1000);
        var fluidKey = new ResourceLocation(fluidName);

        if (!ForgeRegistries.FLUIDS.containsKey(fluidKey))
            throw new JsonSyntaxException("Unknown item '" + fluidName + "'");

        var fluid = ForgeRegistries.FLUIDS.getValue(fluidKey);

        if (json.has("nbt")) {
            try {
                var element = json.get("nbt");
                CompoundTag nbt;

                if (element.isJsonObject())
                    nbt = TagParser.parseTag(GSON.toJson(element));
                else
                    nbt = TagParser.parseTag(GsonHelper.convertToString(element, "nbt"));

                var tmp = new CompoundTag();

                tmp.put("Tag", nbt);
                tmp.putString("FluidName", fluidName);
                tmp.putInt("Amount", amount);

                return FluidStack.loadFluidStackFromNBT(tmp);
            } catch (CommandSyntaxException e) {
                throw new JsonSyntaxException("Invalid NBT Entry: " + e);
            }
        }

        return new FluidStack(fluid, amount);
    }

    public static JsonObject serialize(Fluid fluid) {
        var json = new JsonObject();

        json.addProperty("fluid", Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluid)).toString());

        return json;
    }

    public static Fluid deserializeFluid(JsonObject json) {
        if (json.has("fluid")) {
            var element = json.get("fluid");

            if (element.isJsonPrimitive()) {
                var id = element.getAsString();

                return BuiltInRegistries.FLUID.getOptional(new ResourceLocation(id)).orElseThrow(() -> new JsonSyntaxException("Expected fluid to be an fluid, was unknown string '" + id + "'"));
            } else
                throw new JsonSyntaxException("Expected fluid to be an item, was " + GsonHelper.getType(element));
        } else
            throw new JsonSyntaxException("Missing fluid, expected to find an item");
    }

    public static FluidStack withAmount(FluidStack stack, int amount, boolean container) {
        if (amount <= 0)
            return FluidStack.EMPTY;

        stack = stack.copy();
        stack.setAmount(amount);

        return stack;
    }

    public static FluidStack grow(FluidStack stack, int amount) {
        return withAmount(stack, stack.getAmount() + amount, false);
    }

    public static FluidStack shrink(FluidStack stack, int amount, boolean container) {
        if (stack.isEmpty())
            return FluidStack.EMPTY;

        return withAmount(stack, stack.getAmount() - amount, container);
    }

    public static boolean areFluidsEqual(FluidStack stack1, FluidStack stack2) {
        return !stack1.isEmpty() && !stack2.isEmpty() && stack1.isFluidEqual(stack2);
    }

    public static boolean areFluidsEqual(FluidStack[] stacks1, FluidStack[] stacks2) {
        boolean mark = true;

        for (var stack : stacks2) {
            if (!mark)
                break;

            if (!Arrays.stream(stacks2).allMatch(stack::isFluidEqual))
                mark = false;
        }

        return !Arrays.stream(stacks1).allMatch(FluidStack::isEmpty) && !Arrays.stream(stacks2).allMatch(FluidStack::isEmpty) && mark;
    }

    public static boolean areStacksEqual(FluidStack stack1, FluidStack stack2) {
        return areFluidsEqual(stack1, stack2) && FluidStack.areFluidStackTagsEqual(stack1, stack2);
    }

    public static boolean areStacksEqual(FluidStack[] stacks1, FluidStack[] stacks2) {
        boolean mark = true;

        for (var stack : stacks2) {
            if (!mark)
                break;

            if (Arrays.stream(stacks2).allMatch(itemStack -> FluidStack.areFluidStackTagsEqual(stack, itemStack)))
                mark = true;
        }

        return areFluidsEqual(stacks1, stacks2) && mark;
    }

    public static boolean canCombineStacks(FluidStack stack1, FluidStack stack2, int maxAmount) {
        if (!stack1.isEmpty() && stack2.isEmpty())
            return true;

        return areStacksEqual(stack1, stack2) && (stack1.getAmount() + stack2.getAmount()) <= maxAmount;
    }

    public static FluidStack combineStacks(FluidStack stack1, FluidStack stack2) {
        if (stack1.isEmpty())
            return stack2.copy();

        return grow(stack1, stack2.getAmount());
    }

    public static boolean compareTags(FluidStack stack1, FluidStack stack2) {
        if (!stack1.hasTag())
            return true;

        if (stack1.hasTag() && !stack2.hasTag())
            return false;

        Set<String> stack1Keys = stack1.hasTag() ? stack1.getTag().getAllKeys() : new CompoundTag().getAllKeys();
        Set<String> stack2Keys = stack2.hasTag() ? stack2.getTag().getAllKeys() : new CompoundTag().getAllKeys();

        for (String key : stack1Keys) {
            if (stack2Keys.contains(key)) {
                if (!NbtUtils.compareNbt(stack1.hasTag() ? stack1.getTag().get(key) : null, stack2.hasTag() ? stack2.getTag().get(key) : null, true))
                    return false;

            } else
                return false;
        }

        return true;
    }
}
