package hantonik.atomic.core.item

import net.minecraft.world.item.Item

open class AtomicItem(properties: (Properties) -> Properties) : Item(properties.invoke(Properties())) {
    constructor() : this({ it })
}