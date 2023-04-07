package hantonik.atomic.core;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/**
 * Core class! The beating heart of this mod and the driving wheel for others from The Atomic Series!
 *
 * @author Hantonik
 */

@Mod(AtomicCore.MOD_ID)
public class AtomicCore {
    public static final String MOD_ID = "atomiccore";
    public static final String MOD_NAME = "AtomicCore";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    private static final Marker SETUP_MARKER = MarkerManager.getMarker("SETUP");

    public AtomicCore() {
        LOGGER.debug(SETUP_MARKER, "Initializing {}.", MOD_NAME);

        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.debug(SETUP_MARKER, "{} common setup.", MOD_NAME);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.debug(SETUP_MARKER, "{} client setup.", MOD_NAME);

    }
}
