package hantonik.atomiccore.utils;

import net.minecraft.ChatFormatting;

public class Tooltip extends Localizable {
    public Tooltip(String key) {
        super(key, ChatFormatting.GRAY);
    }

    public Tooltip(String key, ChatFormatting textFormatting) {
        super(key, textFormatting);
    }
}
