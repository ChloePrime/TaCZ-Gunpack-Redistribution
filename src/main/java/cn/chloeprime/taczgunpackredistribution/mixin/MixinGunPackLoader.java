package cn.chloeprime.taczgunpackredistribution.mixin;

import cn.chloeprime.taczgunpackredistribution.TaCZGunpackRedistribution;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.tacz.guns.resource.GunPackLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;
import java.util.List;

@Mixin(value = GunPackLoader.class, remap = false)
public class MixinGunPackLoader {
    @ModifyExpressionValue(
            method = "discoverExtensions",
            at = @At(value = "INVOKE", target = "Lcom/tacz/guns/resource/GunPackLoader;scanExtensions(Ljava/nio/file/Path;)Ljava/util/List;"))
    private List<GunPackLoader.GunPack> loadGunpacksFromResourcePacksAndShaderPacksFolder(List<GunPackLoader.GunPack> original) {
        TaCZGunpackRedistribution.CANDIDATES_PATHS.stream()
                .map(FMLPaths.GAMEDIR.get()::resolve)
                .map(TaCZGunpackRedistribution::scanPotentialExtensions)
                .flatMap(Collection::stream)
                .forEach(original::add);
        return original;
    }
}
