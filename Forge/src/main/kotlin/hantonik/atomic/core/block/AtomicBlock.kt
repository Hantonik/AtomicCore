package hantonik.atomic.core.block

import net.minecraft.world.level.block.Block

open class AtomicBlock(properties: (Properties) -> Properties) : Block(properties.invoke(Properties.of()))