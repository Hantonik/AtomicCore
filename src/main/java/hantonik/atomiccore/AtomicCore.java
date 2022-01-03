package hantonik.atomiccore;

import hantonik.atomiccore.client.model.fluid.FluidTextureModel;
import hantonik.atomiccore.utils.helpers.ModelHelper;
import hantonik.atomiccore.utils.helpers.RecipeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
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
    public void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.debug(AtomicCore.MOD_MARKER, "Starting client setup");

        ResourceManager manager = Minecraft.getInstance().getResourceManager();

        if (manager instanceof ReloadableResourceManager)
            ((ReloadableResourceManager) manager).registerReloadListener(ModelHelper.LISTENER);

        LOGGER.debug(AtomicCore.MOD_MARKER, "Completed client setup");
    }

    @SubscribeEvent
    public void registerModelLoaders(final ModelRegistryEvent event) {
        LOGGER.debug(AtomicCore.MOD_MARKER, "Starting model registry setup");

        ModelLoaderRegistry.registerLoader(new ResourceLocation(AtomicCore.MOD_ID, "fluid_texture"), FluidTextureModel.LOADER);

        LOGGER.debug(AtomicCore.MOD_MARKER, "Completed model registry setup");
    }

    @SubscribeEvent
    public void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.debug(MOD_MARKER, "Starting common setup");

        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new RecipeHelper());

        LOGGER.debug(MOD_MARKER, "Completed common setup");
    }
}
