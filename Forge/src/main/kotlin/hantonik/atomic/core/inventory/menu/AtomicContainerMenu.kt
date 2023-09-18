package hantonik.atomic.core.inventory.menu

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.ItemStack

open class AtomicContainerMenu protected constructor(type: MenuType<out AtomicContainerMenu>, id: Int, val blockPos: BlockPos) : AbstractContainerMenu(type, id) {
    override fun quickMoveStack(player: Player, index: Int): ItemStack = ItemStack.EMPTY

    override fun stillValid(player: Player) = player.distanceToSqr(this.blockPos.x + 0.5, this.blockPos.y + 0.5, this.blockPos.z + 0.5) <= 64
}