package hantonik.atomic.core;

import com.mojang.logging.LogUtils;
import hantonik.atomic.core.util.helper.ACModelHelper;
import hantonik.atomic.core.util.helper.ACRecipeHelper;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@Mod(AtomicCore.MOD_ID)
public final class AtomicCore {
    public static final String MOD_ID = "atomiccore";

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Marker SETUP_MARKER = MarkerFactory.getMarker("SETUP");

    public AtomicCore() {
        LOGGER.info(SETUP_MARKER, "Initializing...");

        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.register(this);
    }

    @SubscribeEvent
    public void commonEvent(final FMLCommonSetupEvent event) {
        LOGGER.info(SETUP_MARKER, "Starting common setup...");

        MinecraftForge.EVENT_BUS.register(ACRecipeHelper.INSTANCE);

        LOGGER.info(SETUP_MARKER, "Finished common setup!");
    }

    @SubscribeEvent
    public void onRegisterClientReloadListeners(final RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(ACModelHelper.LISTENER);
    }
}
