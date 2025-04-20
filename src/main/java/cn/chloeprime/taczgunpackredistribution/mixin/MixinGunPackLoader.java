package cn.chloeprime.taczgunpackredistribution.mixin;

import cn.chloeprime.taczgunpackredistribution.TaCZGunpackRedistribution;
import com.google.common.collect.Iterables;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.tacz.guns.resource.GunPackLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(value = GunPackLoader.class, remap = false)
public class MixinGunPackLoader {
    @ModifyExpressionValue(
            method = "discoverExtensions",
            at = @At(value = "INVOKE", target = "Lcom/tacz/guns/resource/GunPackLoader;scanExtensions(Ljava/nio/file/Path;)Ljava/util/List;"))
    private List<GunPackLoader.GunPack> loadGunpacksFromResourcePacksAndShaderPacksFolder(List<GunPackLoader.GunPack> original) {
        var resourcePacks = FMLPaths.GAMEDIR.get().resolve("resourcepacks");
        var shaderPacks = FMLPaths.GAMEDIR.get().resolve("shaderpacks");
        Iterables.concat(
                TaCZGunpackRedistribution.scanPotentialExtensions(resourcePacks),
                TaCZGunpackRedistribution.scanPotentialExtensions(shaderPacks)
        ).forEach(original::add);
        return original;
    }
}
