package hantonik.atomiccore.libs;

import hantonik.atomiccore.AtomicCore;
import hantonik.atomiccore.utils.Tooltip;
import net.minecraft.network.chat.TextComponent;

public class CoreTooltips {
    public static final Tooltip CRAFTING_COMPONENT = generate("crafting_component");
    public static final Tooltip CRAFTING = generate("crafting");

    public static final Tooltip HOLD_SHIFT_FOR_INFO = generate("hold_shift_for_info");
    public static final Tooltip HOLD_CTRL_FOR_INFO = generate("hold_ctrl_for_info");

    public static final TextComponent EMPTY = new TextComponent(" ");

    protected static Tooltip generate(String id) {
        return new Tooltip("tooltip." + AtomicCore.MOD_ID + "." + id);
    }
}
