package hantonik.atomiccore.mixins;

import com.google.gson.JsonArray;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.common.ForgeConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapelessRecipe.Serializer.class)
public abstract class MixinShapelessRecipe implements RecipeSerializer<ShapelessRecipe> {
    @Inject(at = @At("HEAD"), method = "itemsFromJson", cancellable = true)
    private static void itemsFromJson(JsonArray array, CallbackInfoReturnable<NonNullList<Ingredient>> callback) {
        NonNullList<Ingredient> list = NonNullList.create();

        for(int i = 0; i < array.size(); ++i) {
            Ingredient ingredient = Ingredient.fromJson(array.get(i));

            try {
                if (ForgeConfig.SERVER.skipEmptyShapelessCheck.get() || !ingredient.isEmpty())
                    list.add(ingredient);
            } catch (IllegalStateException e) {
                if (ForgeConfig.SERVER.skipEmptyShapelessCheck.getDefault() || !ingredient.isEmpty())
                    list.add(ingredient);
            }
        }

        callback.setReturnValue(list);
    }
}
