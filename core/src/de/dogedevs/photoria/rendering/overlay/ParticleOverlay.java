package de.dogedevs.photoria.rendering.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import de.dogedevs.photoria.utils.assets.ParticlePool;

/**
 * Created by elektropapst on 06.01.2016.
 */
public class ParticleOverlay extends AbstractOverlay {
    private OrthographicCamera camera;

    public ParticleOverlay(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render() {
        update();
        batch.begin();
        for(ParticleEffectPool.PooledEffect effect : ParticlePool.instance().getEffects()) {
//            effect.setPosition(0, 0);
            effect.draw(batch, Gdx.graphics.getDeltaTime());
            if(effect.isComplete()) {
                ParticlePool.instance().removeEffect(effect, true);
                effect.free();
            }
        }
        batch.end();
    }
}
