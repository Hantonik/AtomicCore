package hantonik.atomic.core;

import hantonik.atomic.core.utils.ARecipeHelper;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class AtomicCore implements ModInitializer {
    public static final String MOD_ID = "atomiccore";
    public static final String MOD_NAME = "AtomicCore";

    public static final Logger LOGGER = LogManager.getLogger(AtomicCore.MOD_NAME);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing...");

        ARecipeHelper.init();
    }
}
