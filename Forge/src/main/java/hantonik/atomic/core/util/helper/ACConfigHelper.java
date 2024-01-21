package hantonik.atomic.core.util.helper;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ACConfigHelper {
    public static void load(ForgeConfigSpec config, String location) {
        var path = FMLPaths.CONFIGDIR.get().resolve(location);
        var data = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        data.load();

        config.setConfig(data);
    }
}
