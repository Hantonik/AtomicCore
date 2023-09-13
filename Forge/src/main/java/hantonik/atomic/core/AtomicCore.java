package hantonik.atomic.core;

import hantonik.atomic.core.client.model.fluid.FluidTextureModel;
import hantonik.atomic.core.utils.helpers.ModelHelper;
import hantonik.atomic.core.utils.helpers.RecipeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.client.event.ModelEvent;
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
public final class AtomicCore {
    public static final String MOD_ID = "atomiccore";
    public static final String MOD_NAME = "AtomicCore";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static final Marker SETUP_MARKER = MarkerManager.getMarker("SETUP");

    public AtomicCore() {
        AtomicCore.LOGGER.info(AtomicCore.SETUP_MARKER, "Initializing...");

        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void commonSetup(final FMLCommonSetupEvent event) {
        AtomicCore.LOGGER.info(AtomicCore.SETUP_MARKER, "Starting common setup...");

        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new RecipeHelper());
    }

    @SubscribeEvent
    public void clientSetup(final FMLClientSetupEvent event) {
        AtomicCore.LOGGER.info(AtomicCore.SETUP_MARKER, "Starting client setup...");

        if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager manager)
            manager.registerReloadListener(ModelHelper.LISTENER);
    }

    @SubscribeEvent
    public void registerGeometryLoaders(final ModelEvent.RegisterGeometryLoaders event) {
        event.register("fluid_texture", FluidTextureModel.LOADER);
    }
}
