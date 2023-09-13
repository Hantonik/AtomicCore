package hantonik.atomic.core.block

import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.block.DropExperienceBlock

open class AtomicDropExperienceBlock(properties: (Properties) -> Properties, xpRange: IntProvider) : DropExperienceBlock(properties.invoke(Properties.of()), xpRange) {
    constructor(properties: (Properties) -> Properties) : this(properties, ConstantInt.of(0))
}