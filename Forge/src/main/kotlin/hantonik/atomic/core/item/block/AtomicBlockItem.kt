package hantonik.atomic.core.item.block

import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block

open class AtomicBlockItem(block: Block, properties: (Properties) -> Properties) : BlockItem(block, properties.invoke(Properties()))
