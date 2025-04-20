package cn.chloeprime.taczgunpackredistribution.mixin;

import com.tacz.guns.resource.GunPackLoader;
import org.apache.logging.log4j.Marker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.nio.file.Path;

@Mixin(value = GunPackLoader.class, remap = false)
public interface GunPackLoaderAccessor {
    @Accessor("MARKER") static Marker getMarker() {
        throw new AbstractMethodError();
    }

    @Invoker static GunPackLoader.GunPack invokeFromDirPath(Path path) {
        throw new AbstractMethodError();
    }

    @Invoker static GunPackLoader.GunPack invokeFromZipPath(Path path) {
        throw new AbstractMethodError();
    }
}
