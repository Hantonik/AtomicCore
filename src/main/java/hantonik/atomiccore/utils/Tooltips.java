package hantonik.atomiccore.utils;

import hantonik.atomiccore.AtomicCore;
import net.minecraft.network.chat.Component;

public final class Tooltips {
    public static Component getHoldForInfo(Component key) {
        return Component.translatable("attributes." + AtomicCore.MOD_ID + ".hold_for_info", key);
    }
}
