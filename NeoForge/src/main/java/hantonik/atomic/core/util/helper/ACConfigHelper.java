package hantonik.atomic.core.util.helper;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ACConfigHelper {
    public static void load(ModConfigSpec config, String location) {
        var path = FMLPaths.CONFIGDIR.get().resolve(location);
        var data = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        data.load();

        config.setConfig(data);
    }
}
