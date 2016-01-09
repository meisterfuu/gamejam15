package de.dogedevs.photoria.utils.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.google.gson.Gson;
import de.dogedevs.photoria.MainGame;
import de.dogedevs.photoria.content.mob.MobManager;
import de.dogedevs.photoria.utils.assets.enums.*;

/**
 * Created by elektropapst on 03.01.2016.
 */
public class AssetLoader {

    static AssetManager manager = new AssetManager();

    public AssetLoader() {
        loadTextures();
        loadParticles();
        loadMusics();
        loadSounds();
        loadBitmapFonts();
        manager.finishLoading();
        MainGame.log("Loaded Assets: " + manager.getLoadedAssets());

        Gson gson = new Gson();
        MobManager mm = new MobManager();
        System.out.println(gson.toJson(mm.getRandomTemplateForBiome(0)));
    }

    private void loadParticles() {
        for(Particles particle : Particles.values()) {
            ParticleEffectLoader.ParticleEffectParameter params = new ParticleEffectLoader.ParticleEffectParameter();
            params.imagesDir = Gdx.files.internal(particle.imageDir);
            manager.load(particle.effectFile, ParticleEffect.class, params);
        }
    }

    private void loadTextures() {
        for(Textures tex : Textures.values()) {
            manager.load(tex.name, Texture.class);
        }
    }

    private void loadMusics() {
        for(Musics music : Musics.values()) {
            manager.load(music.name, Music.class);
        }
    }

    private void loadSounds() {
        for(Sounds sound : Sounds.values()) {
            manager.load(sound.name, Sound.class);
        }
    }

    private void loadBitmapFonts() {
        for(BitmapFonts font : BitmapFonts.values()) {
            manager.load(font.name, BitmapFont.class);
        }
    }

    public static BitmapFont getBitmapFont(BitmapFonts font, boolean markupEnabled) {
        BitmapFont bitmapFont = manager.get(font.name, BitmapFont.class);
        bitmapFont.getData().markupEnabled = markupEnabled;
        return bitmapFont;
    }

    public static ParticleEffect getParticleEffect(Particles effect) {
        return manager.get(effect.effectFile, ParticleEffect.class);
    }

    public static Texture getTexture(Textures texture) {
        return manager.get(texture.name, Texture.class);
    }

    public static Music getMusic(Musics music) {
        return manager.get(music.name, Music.class);
    }

    public static Sound getSound(Sounds sound) {
        return manager.get(sound.name, Sound.class);
    }

    public static ShaderProgram getShader(ShaderPrograms shaderProgram) {
        ShaderProgram.pedantic = false;
        ShaderProgram retVal = new ShaderProgram(Gdx.files.internal(shaderProgram.vertexShader),
                                                 Gdx.files.internal(shaderProgram.fragmentShader));
        MainGame.log(retVal.isCompiled() ? shaderProgram.name() + " compiled." : retVal.getLog());
        return retVal;
    }

    public void dispose() {
        manager.clear();
    }

}
