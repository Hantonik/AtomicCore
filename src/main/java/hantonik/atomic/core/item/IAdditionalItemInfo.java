package hantonik.atomic.core.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IAdditionalItemInfo {
    void additionalTooltips(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flag);
}
