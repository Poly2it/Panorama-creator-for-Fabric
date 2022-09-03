package com.Hashibutogarasu.panorama

import Global.Func.Image.deepCopy
import Global.Func.Image.scaleImage
import Global.Settings.Settings.*
import mcmeta
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.TranslatableText
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import javax.imageio.ImageIO
import kotlin.io.path.createFile


/**
 * エントリーポイント。起動時にinit関数が呼ばれる
 * */
@Suppress("unused")
fun init() {

    var keyBinding = KeyBindingHelper.registerKeyBinding(
        KeyBinding(
            translationKey,  // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM,  // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            key,  // The keycode of the key
            category // The translation key of the keybinding's category.
        )
    )

    ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: MinecraftClient ->
        while (keyBinding.wasPressed()) {
            var biomenamespace = client.world!!.getBiomeKey(client.player!!.blockPos).get().value.namespace
            var biomepath = client.world!!.getBiomeKey(client.player!!.blockPos).get().value.path
            try {
                TakePanorama(client,biomenamespace, biomepath)
            }
            catch (ex : Exception){
                try{
                    TakePanorama(client,biomenamespace, biomepath)
                }
                catch(ex : Exception){
                    ex.printStackTrace()
                }
            }
        }
    })
}

fun TakePanorama(client : MinecraftClient,biomenamespace : String, biomepath : String ,random : String = (Math.random()*10000).toInt().toString()) {
    var biomename = TranslatableText("biome.$biomenamespace.$biomepath").asTruncatedString(100)
    var resourcefilename = "$biomepath$random"
    var file = File("./$modid/");

    try {
        file.mkdirs()
    } catch (ex: Exception) {
        println(ex)
    }

    client.takePanorama(file, width, height)

    var screenshot = File("./$modid/screenshots/")
    var screenshotfiles = screenshot.listFiles()
    var resourcepackstring = "./resourcepacks/$resourcefilename/assets/minecraft/textures/gui/title/background/"
    var mcmetapathstring = "./resourcepacks/$resourcefilename/pack.mcmeta"
    var packiconpathstring = "./resourcepacks/$resourcefilename/pack.png"
    var packicon = File(packiconpathstring)
    var baseimagepath = "./$modid/screenshots/panorama_0.png"
    var baseimagefile = Paths.get(baseimagepath).toRealPath().toFile()

    var background = File(resourcepackstring)
    background.mkdirs()

    screenshotfiles.forEach { f ->
        Thread.sleep(100)
        var panorama_img = Paths.get(f.toString()).toRealPath()
        var newimgpath = Paths.get(resourcepackstring + "\\" + panorama_img.fileName)
        newimgpath.createFile()
        Files.copy(panorama_img, newimgpath, StandardCopyOption.REPLACE_EXISTING)
        var img = ImageIO.read(panorama_img.toFile())
        deepCopy(img, newimgpath.toFile(), "png")
    }

    packicon.createNewFile()
    scaleImage(baseimagefile,packicon, 64, 64)

    var resourcedescription = TranslatableText("text.panorama.tookscreenshot", biomename).asTruncatedString(100)
    mcmeta.pack.description = resourcedescription

    var textfile = File(mcmetapathstring)
    var bufferedWriter = textfile.bufferedWriter()

    minecraft_version = client.game.version.releaseTarget

    when {
        minecraft_version != null && minecraft_version == "" -> { mcmeta.pack.pack_format = 8 }
        minecraft_version == "1.18.2" ||
                minecraft_version == "1.18.1" ||
                minecraft_version == "1.18" -> { mcmeta.pack.pack_format = 8 }

        minecraft_version == "1.17.1" ||
                minecraft_version == "1.17" -> { mcmeta.pack.pack_format = 7 }

        minecraft_version == "1.16.5" ||
                minecraft_version == "1.16.4" ||
                minecraft_version == "1.16.3" ||
                minecraft_version == "1.16.2" -> { mcmeta.pack.pack_format = 6 }

        minecraft_version == "1.16.1" ||
                minecraft_version == "1.16" ||
                minecraft_version == "1.15.2" ||
                minecraft_version == "1.15.1" ||
                minecraft_version == "1.15" -> { mcmeta.pack.pack_format = 5 }

        minecraft_version == "1.14.4" ||
                minecraft_version == "1.14.3" ||
                minecraft_version == "1.14.2" ||
                minecraft_version == "1.14.1" ||
                minecraft_version == "1.14"   -> { mcmeta.pack.pack_format = 4 }
        else -> { mcmeta.pack.pack_format = 8 }
    }


    bufferedWriter.write("{\n\t\"pack\":{\n\t\t\"pack_format\":${mcmeta.pack.pack_format},\n\t\t\"description\":\"${mcmeta.pack.description}\"\n\t}\n}");
    bufferedWriter.close()
}


