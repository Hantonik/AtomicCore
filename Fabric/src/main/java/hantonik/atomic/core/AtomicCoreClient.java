package hantonik.atomic.core;

import hantonik.atomic.core.util.helper.ACModelHelper;
import hantonik.atomic.core.util.helper.ACRecipeHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public final class AtomicCoreClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AtomicCore.LOGGER.info(AtomicCore.SETUP_MARKER, "Initializing client...");

        ACRecipeHelper.registerClient();

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(ACModelHelper.LISTENER);
    }
}
