package hantonik.atomic.core.utils.helpers;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

/**
 * Some helpful code for config. <p>
 *
 * @author Hantonik
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigHelper {
    public static void load(ForgeConfigSpec config, String location) {
        var path = FMLPaths.CONFIGDIR.get().resolve(location);
        var data = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        data.load();
        config.setConfig(data);
    }
}
