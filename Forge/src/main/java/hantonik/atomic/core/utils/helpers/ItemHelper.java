package hantonik.atomic.core.utils.helpers;

import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Some helpful code for items. <p>
 *
 * @author Hantonik
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemHelper {
    public static JsonObject serialize(Item item) {
        var json = new JsonObject();

        json.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString());

        return json;
    }

    public static JsonObject serialize(ItemStack stack) {
        var json = new JsonObject();

        json.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).toString());

        if (stack.getCount() > 1)
            json.addProperty("count", stack.getCount());

        if (stack.hasTag())
            json.addProperty("nbt", Objects.requireNonNull(stack.getTag()).toString());

        return json;
    }

    public static Item deserializeItem(JsonObject json) {
        return GsonHelper.getAsItem(json, "item");
    }

    public static ItemStack deserializeStack(JsonObject json) {
        return CraftingHelper.getItemStack(json, true);
    }

    public static NonNullList<Ingredient> toIngredientList(Ingredient... ingredients) {
        return Arrays.stream(ingredients).collect(Collectors.toCollection(NonNullList::create));
    }

    public static NonNullList<ItemStack> toStackList(ItemStack... stacks) {
        return Arrays.stream(stacks).collect(Collectors.toCollection(NonNullList::create));
    }

    public static void dropAll(IItemHandler itemHandler, Level level, BlockPos pos) {
        if (itemHandler == null)
            return;

        for (int i = 0; i < itemHandler.getSlots(); i++)
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), itemHandler.getStackInSlot(i));
    }

    public static boolean compare(ItemStack stack, Ingredient ingredient) {
        return Arrays.stream(ingredient.getItems()).anyMatch(itemStack -> ItemStack.isSameItem(itemStack, stack));
    }

    public static boolean compare(Ingredient input, Ingredient other) {
        var inputString = Arrays.stream(input.getItems()).map(stack -> ForgeRegistries.ITEMS.getKey(stack.getItem()).toString()).toArray(String[]::new);
        var otherString = Arrays.stream(other.getItems()).map(stack -> ForgeRegistries.ITEMS.getKey(stack.getItem()).toString()).toArray(String[]::new);

        Arrays.sort(inputString);
        Arrays.sort(otherString);

        return Arrays.equals(inputString, otherString);
    }

    public static ItemStack withSize(ItemStack stack, int size, boolean container) {
        if (size <= 0) {
            if (container && stack.hasCraftingRemainingItem())
                return stack.getCraftingRemainingItem();

            else
                return ItemStack.EMPTY;
        }

        stack = stack.copy();
        stack.setCount(size);

        return stack;
    }

    public static ItemStack grow(ItemStack stack, int amount) {
        return withSize(stack, stack.getCount() + amount, false);
    }

    public static ItemStack shrink(ItemStack stack, int amount, boolean container) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        return withSize(stack, stack.getCount() - amount, container);
    }

    public static boolean areItemsEqual(ItemStack stack1, ItemStack stack2) {
        if (stack1.isEmpty() && stack2.isEmpty())
            return true;

        return !stack1.isEmpty() && ItemStack.isSameItem(stack1, stack2);
    }

    public static boolean areStacksEqual(ItemStack stack1, ItemStack stack2) {
        return areItemsEqual(stack1, stack2) && ItemStack.isSameItemSameTags(stack1, stack2);
    }

    public static boolean canCombineStacks(ItemStack stack1, ItemStack stack2) {
        if (!stack1.isEmpty() && stack2.isEmpty())
            return true;

        return areStacksEqual(stack1, stack2) && (stack1.getCount() + stack2.getCount()) <= stack1.getMaxStackSize();
    }

    public static ItemStack combineStacks(ItemStack stack1, ItemStack stack2) {
        if (stack1.isEmpty())
            return stack2.copy();

        return grow(stack1, stack2.getCount());
    }

    public static boolean compareTags(ItemStack stack1, ItemStack stack2) {
        if (!stack1.hasTag())
            return true;

        if (stack1.hasTag() && !stack2.hasTag())
            return false;

        var stack1Keys = NBTHelper.getTagCompound(stack1).getAllKeys();
        var stack2Keys = NBTHelper.getTagCompound(stack2).getAllKeys();

        for (var key : stack1Keys) {
            if (stack2Keys.contains(key)) {
                if (!NbtUtils.compareNbt(NBTHelper.getTag(stack1, key), NBTHelper.getTag(stack2, key), true))
                    return false;
            } else
                return false;
        }

        return true;
    }
}
