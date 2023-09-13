package hantonik.atomic.core.utils.helpers;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class StringHelper {
    public static boolean canLocalize(String key) {
        return I18n.exists(key);
    }

    public static MutableComponent getTextComponent(String key) {
        return canLocalize(key) ? Component.translatable(key) : Component.literal(key);
    }

    public static MutableComponent getTextComponent(String key, Object... args) {
        return canLocalize(key) ? Component.translatable(key, args) : Component.literal(key);
    }
}
