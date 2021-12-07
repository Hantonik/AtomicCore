package hantonik.atomiccore;

import hantonik.atomiccore.utils.helpers.RecipeHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Mod(AtomicCore.MOD_ID)
public final class AtomicCore {
    public static final String MOD_ID = "atomiccore";
    public static final String NAME = "AtomicCore";

    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static final Marker MOD_MARKER = MarkerManager.getMarker("MOD");

    public AtomicCore() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.debug(MOD_MARKER, "Starting common setup");

        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new RecipeHelper());

        LOGGER.debug(MOD_MARKER, "Completed common setup");
    }
}
