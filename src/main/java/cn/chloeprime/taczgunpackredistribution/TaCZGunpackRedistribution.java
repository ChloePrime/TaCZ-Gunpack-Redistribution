package cn.chloeprime.taczgunpackredistribution;

import cn.chloeprime.taczgunpackredistribution.mixin.GunPackLoaderAccessor;
import com.mojang.logging.LogUtils;
import com.tacz.guns.GunMod;
import com.tacz.guns.resource.GunPackLoader;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Mod(TaCZGunpackRedistribution.MODID)
public class TaCZGunpackRedistribution {
    public static final String MODID = "tacz_gunpack_redistribution";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static List<GunPackLoader.GunPack> scanPotentialExtensions(Path extensionsPath) {
        List<GunPackLoader.GunPack> gunPacks = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(extensionsPath)){
            for (Path entry : stream) {
                GunPackLoader.GunPack gunPack = null;
                if (Files.isDirectory(entry) && isDirectoryAGunPack(entry)) {
                    gunPack = GunPackLoaderAccessor.invokeFromDirPath(entry);
                } else if (entry.toString().endsWith(".zip") && isZipAGunPack(entry)) {
                    gunPack = GunPackLoaderAccessor.invokeFromZipPath(entry);
                }
                //noinspection ConstantValue
                if (gunPack != null) {
                    gunPacks.add(gunPack);
                }
            }
        } catch (IOException e) {
            GunMod.LOGGER.error(GunPackLoaderAccessor.getMarker(),"Failed to scan extensions from {}. Error: {}", extensionsPath, e);
        }

        return gunPacks;
    }

    private static boolean isDirectoryAGunPack(Path dir) {
        return Files.isRegularFile(dir.resolve("gunpack.meta.json"));
    }

    private static boolean isZipAGunPack(Path dir) throws IOException {
        if (!Files.isRegularFile(dir) || !dir.toString().endsWith(".zip")) {
            return false;
        }
        try (var zip = new ZipFile(dir.toFile())) {
            ZipEntry entry = zip.getEntry("gunpack.meta.json");
            return entry != null && !entry.isDirectory();
        }
    }

    public TaCZGunpackRedistribution() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
