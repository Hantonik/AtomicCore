package hantonik.atomic.core;

import com.mojang.logging.LogUtils;
import hantonik.atomic.core.util.helper.ACRecipeHelper;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public final class AtomicCore implements ModInitializer {
    public static final String MOD_ID = "atomiccore";
    public static final String MOD_NAME = "AtomicCore";

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Marker SETUP_MARKER = MarkerFactory.getMarker("SETUP");

    @Override
    public void onInitialize() {
        LOGGER.info(SETUP_MARKER, "Initializing...");

        ACRecipeHelper.register();
    }
}
