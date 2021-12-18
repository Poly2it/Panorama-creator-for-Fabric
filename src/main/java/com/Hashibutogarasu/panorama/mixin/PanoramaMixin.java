package com.Hashibutogarasu.panorama.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

import static Global.Settings.Settings.minecraft_version;

@Mixin(TitleScreen.class)
// Mixins HAVE to be written in java due to constraints in the mixin system.
public class PanoramaMixin {
    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        try {
            var file = new File("./panorama/");
            file.mkdirs();
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }
}
