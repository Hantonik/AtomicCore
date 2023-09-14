package hantonik.atomic.core.util.helper

import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.io.WritingMode
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.loading.FMLPaths

object ACConfigHelper {
    fun load(config: ForgeConfigSpec, location: String) {
        val path = FMLPaths.CONFIGDIR.get().resolve(location)
        val data = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build()
        data.load()

        config.setConfig(data)
    }
}