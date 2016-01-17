package de.dogedevs.photoria.model.entity.components.stats;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Furuha on 27.12.2015.
 */
public class HealthComponent implements Component, Pool.Poolable {

    public float health;
    public float maxHealth; // use only for template
    public float maxHealthUse; // use this

    public float immuneTime;
    public float maxImmuneTime;

    public HealthComponent() {
        health = 100;
        maxHealth = 100;
        maxHealthUse = 100;
        immuneTime = 0;
        maxImmuneTime = 0;
    }

    public HealthComponent(float health, float maxHealth) {
        this.health = health;
        this.maxHealth = maxHealth;
        this.maxHealthUse = maxHealth;
        this.immuneTime = 0;
        this.maxImmuneTime = 0;
    }

    @Override
    public void reset() {
        health = 100;
        maxHealth = 100;
        immuneTime = 0;
        maxImmuneTime = 0;
    }
}
