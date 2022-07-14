package hantonik.atomiccore.inventory.slot;

import hantonik.atomiccore.utils.handlers.AtomicItemStackHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class BasicItemStackHandlerSlot extends SlotItemHandler {
    private final AtomicItemStackHandler inventory;
    private final int index;

    public BasicItemStackHandlerSlot(AtomicItemStackHandler inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);

        this.inventory = inventory;
        this.index = index;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return !this.inventory.extractItemSuper(this.index, 1, true).isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack remove(int amount) {
        return this.inventory.extractItemSuper(this.index, amount, false);
    }
}
