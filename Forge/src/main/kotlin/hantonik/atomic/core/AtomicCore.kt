package hantonik.atomic.core

import hantonik.atomic.core.client.model.fluid.FluidTextureModel
import hantonik.atomic.core.util.helper.ACModelHelper
import hantonik.atomic.core.util.helper.ACRecipeHelper
import net.minecraftforge.client.event.ModelEvent
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.Marker
import org.apache.logging.log4j.MarkerManager

@Mod(AtomicCore.MOD_ID)
class AtomicCore {
    companion object {
        const val MOD_ID = "atomiccore"
        const val MOD_NAME = "AtomicCore"

        val LOGGER: Logger = LogManager.getLogger(MOD_NAME)
        val SETUP_MARKER: Marker = MarkerManager.getMarker("SETUP")
    }

    init {
        LOGGER.info(SETUP_MARKER, "Initializing...")

        FMLJavaModLoadingContext.get().modEventBus.register(this)
    }

    @SubscribeEvent
    fun commonSetup(event: FMLCommonSetupEvent) {
        LOGGER.info(SETUP_MARKER, "Starting common setup...")

        MinecraftForge.EVENT_BUS.register(this)

        MinecraftForge.EVENT_BUS.register(ACRecipeHelper)
    }

    @SubscribeEvent
    fun registerGeometryLoaders(event: ModelEvent.RegisterGeometryLoaders) {
        event.register("fluid_texture", FluidTextureModel.LOADER)
    }

    @SubscribeEvent
    fun registerClientReloadListeners(event: RegisterClientReloadListenersEvent) {
        event.registerReloadListener(ACModelHelper.LISTENER)
        event.registerReloadListener(FluidTextureModel.LOADER)
    }
}