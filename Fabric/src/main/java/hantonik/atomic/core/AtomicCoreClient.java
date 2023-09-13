package hantonik.atomic.core;

import net.fabricmc.api.ClientModInitializer;

public final class AtomicCoreClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AtomicCore.LOGGER.info("Initializing client...");
    }
}
