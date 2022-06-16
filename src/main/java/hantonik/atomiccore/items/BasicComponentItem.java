package hantonik.atomiccore.items;

import hantonik.atomiccore.libs.CoreTooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class BasicComponentItem extends BasicItem {
    public BasicComponentItem(Function<Properties, Properties> properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);

        tooltip.add(CoreTooltips.CRAFTING_COMPONENT.build());
    }
}
