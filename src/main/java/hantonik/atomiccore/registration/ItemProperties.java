package hantonik.atomiccore.registration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemProperties {
    public static final Item.Properties BUCKET_PROPS = new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC);
}
