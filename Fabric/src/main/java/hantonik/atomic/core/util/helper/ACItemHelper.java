package hantonik.atomic.core.util.helper;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ACItemHelper {
    public static final Codec<ItemStack> ITEMSTACK_WITH_NBT_CODEC = Codec.pair(ItemStack.ITEM_WITH_COUNT_CODEC, TagParser.AS_CODEC).xmap(codec -> {
        var stack = codec.getFirst().copy();
        stack.setTag(codec.getSecond());

        return stack;
    }, stack -> Pair.of(stack, stack.getTag()));

    public static JsonElement serializeItem(Item item) {
        return Util.getOrThrow(BuiltInRegistries.ITEM.byNameCodec().encodeStart(JsonOps.INSTANCE, item), IllegalStateException::new);
    }

    public static JsonElement serializeStack(ItemStack stack, boolean serializeNbt) {
        return Util.getOrThrow((serializeNbt ? ITEMSTACK_WITH_NBT_CODEC.encodeStart(JsonOps.INSTANCE, stack) : ItemStack.RESULT_CODEC.codec().encodeStart(JsonOps.INSTANCE, stack)), IllegalStateException::new);
    }

    public static Item deserializeItem(JsonElement json) {
        return Util.getOrThrow(BuiltInRegistries.ITEM.byNameCodec().parse(JsonOps.INSTANCE, json), IllegalStateException::new);
    }

    public static ItemStack deserializeStack(JsonElement json, boolean deserializeNbt) {
        return Util.getOrThrow((deserializeNbt ? ITEMSTACK_WITH_NBT_CODEC.parse(JsonOps.INSTANCE, json) : ItemStack.RESULT_CODEC.codec().parse(JsonOps.INSTANCE, json)), IllegalStateException::new);
    }

    public static ItemStack withSize(ItemStack stack, int size, boolean container) {
        if (size <= 0) {
            if (container && stack.getItem().hasCraftingRemainingItem())
                return stack.getRecipeRemainder();
            else
                return ItemStack.EMPTY;
        }

        var copy = stack.copy();
        copy.setCount(size);

        return copy;
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

        if (!areItemsEqual(stack1, stack2))
            throw new IllegalStateException("Cannot combine different items");

        return grow(stack1, stack2.getCount());
    }
}
