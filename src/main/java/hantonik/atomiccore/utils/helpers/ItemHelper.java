package hantonik.atomiccore.utils.helpers;

import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.IItemHandler;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemHelper {
    public static JsonObject serialize(ItemStack stack) {
        var json = new JsonObject();

        json.addProperty("item", Objects.requireNonNull(Registry.ITEM.getKey(stack.getItem())).toString());

        if (stack.getCount() > 1)
            json.addProperty("count", stack.getCount());

        if (stack.hasTag())
            json.addProperty("nbt", Objects.requireNonNull(stack.getTag()).toString());

        return json;
    }

    public static ItemStack deserializeStack(JsonObject json) {
        return CraftingHelper.getItemStack(json, true);
    }

    public static JsonObject serialize(Item item) {
        var json = new JsonObject();

        json.addProperty("item", Objects.requireNonNull(Registry.ITEM.getKey(item)).toString());

        return json;
    }

    public static Item deserializeItem(JsonObject json) {
        return GsonHelper.getAsItem(json, "item");
    }

    public static NonNullList<Ingredient> toIngredientsList(Ingredient... ingredients) {
        return Arrays.stream(ingredients)
                .collect(Collectors.toCollection(NonNullList::create));
    }

    public static NonNullList<ItemStack> toStacksList(ItemStack... stacks) {
        return Arrays.stream(stacks)
                .collect(Collectors.toCollection(NonNullList::create));
    }

    public static void dropAll(IItemHandler itemHandler, Level level, BlockPos pos) {
        if (itemHandler == null)
            return;

        for (int i = 0; i < itemHandler.getSlots(); i++)
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), itemHandler.getStackInSlot(i));
    }

    public static boolean compare(ItemStack stack, Ingredient ingredient) {
        return Arrays.stream(ingredient.getItems()).anyMatch(itemStack -> itemStack.sameItem(stack));
    }

    public static boolean compare(Ingredient input, Ingredient other) {
        var inputString = Arrays.stream(input.getItems()).map(stack -> Registry.ITEM.getKey(stack.getItem()).toString()).toArray(String[]::new);
        var otherString = Arrays.stream(other.getItems()).map(stack -> Registry.ITEM.getKey(stack.getItem()).toString()).toArray(String[]::new);

        Arrays.sort(inputString);
        Arrays.sort(otherString);

        return Arrays.equals(inputString, otherString);
    }
}
