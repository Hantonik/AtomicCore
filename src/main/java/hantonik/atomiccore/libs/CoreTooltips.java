package hantonik.atomiccore.libs;

import hantonik.atomiccore.AtomicCore;
import hantonik.atomiccore.utils.Tooltip;
import net.minecraft.network.chat.Component;

public class CoreTooltips {
    public static final Tooltip CRAFTING_COMPONENT = generate("crafting_component");
    public static final Tooltip CRAFTING = generate("crafting");

    public static final Component EMPTY = Component.literal(" ");

    protected static Tooltip generate(String id) {
        return new Tooltip("tooltip." + AtomicCore.MOD_ID + "." + id);
    }
}
