package hantonik.atomic.core.item;

import hantonik.atomic.core.AtomicCore;
import hantonik.atomic.core.utils.helpers.StringHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AtomicItem extends Item implements IAdditionalItemInfo {
    public AtomicItem() {
        this(p -> p);
    }

    public AtomicItem(Function<Properties, Properties> properties) {
        super(properties.apply(new Properties()));
    }

    @Override
    public void additionalTooltips(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flag) {}

    @Override
    public final void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flag) {
        List<Component> additionalTooltips = new ArrayList<>();
        this.additionalTooltips(stack, level, additionalTooltips, flag);

        if (!additionalTooltips.isEmpty()) {
            if (Screen.hasShiftDown())
                tooltips.addAll(additionalTooltips);

            else
                tooltips.add(StringHelper.getTextComponent("info." + AtomicCore.MOD_ID + ".hold_for_info", Component.literal("SHIFT").withStyle(ChatFormatting.DARK_GREEN)).withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, level, tooltips, flag);
    }
}
