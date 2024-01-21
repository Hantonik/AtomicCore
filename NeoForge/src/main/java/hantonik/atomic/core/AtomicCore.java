package hantonik.atomic.core;

import com.mojang.logging.LogUtils;
import hantonik.atomic.core.util.helper.ACModelHelper;
import hantonik.atomic.core.util.helper.ACRecipeHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@Mod(AtomicCore.MOD_ID)
public final class AtomicCore {
    public static final String MOD_ID = "atomiccore";

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Marker SETUP_MARKER = MarkerFactory.getMarker("SETUP");

    public AtomicCore(IEventBus bus) {
        LOGGER.info(SETUP_MARKER, "Initializing...");

        bus.register(this);
    }

    @SubscribeEvent
    public void commonEvent(final FMLCommonSetupEvent event) {
        LOGGER.info(SETUP_MARKER, "Starting common setup...");

        NeoForge.EVENT_BUS.register(ACRecipeHelper.INSTANCE);

        LOGGER.info(SETUP_MARKER, "Finished common setup!");
    }

    @SubscribeEvent
    public void onRegisterClientReloadListeners(final RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(ACModelHelper.LISTENER);
    }
}
