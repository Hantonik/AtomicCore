package hantonik.atomic.core.registration

import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

object ItemProperties {
    val BUCKET: Item.Properties = Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
}