package hantonik.atomic.core.inventory.menu;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class AtomicContainerMenu extends AbstractContainerMenu {
    @Getter
    private final BlockPos blockPos;

    protected AtomicContainerMenu(MenuType<?> type, int id, BlockPos pos) {
        super(type, id);

        this.blockPos = pos;
    }

    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(this.blockPos.getX() + 0.5, this.blockPos.getY() + 0.5, this.blockPos.getZ() + 0.5) <= 64;
    }
}
