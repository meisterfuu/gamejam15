package de.dogedevs.photoria.model.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;
import de.dogedevs.photoria.Statics;
import de.dogedevs.photoria.content.MobStats;
import de.dogedevs.photoria.content.mob.MobTemplate;
import de.dogedevs.photoria.content.weapons.*;
import de.dogedevs.photoria.model.entity.components.*;
import de.dogedevs.photoria.model.entity.components.rendering.AnimationComponent;
import de.dogedevs.photoria.model.entity.components.rendering.RenderAsTileComponent;
import de.dogedevs.photoria.model.entity.components.rendering.SpriteComponent;
import de.dogedevs.photoria.model.entity.components.stats.ElementsComponent;
import de.dogedevs.photoria.model.entity.components.stats.HealthComponent;
import de.dogedevs.photoria.model.map.ChunkBuffer;
import de.dogedevs.photoria.model.map.ChunkCell;
import de.dogedevs.photoria.rendering.tiles.Tile;
import de.dogedevs.photoria.rendering.tiles.TileCollisionMapper;
import de.dogedevs.photoria.rendering.tiles.TileMapper;
import de.dogedevs.photoria.utils.assets.enums.Textures;

/**
 * Created by Furuha on 02.01.2016.
 */
public class EntityLoader {

    PooledEngine ashley = Statics.ashley;

    public void createChunkEntities(int chunkX, int chunkY, long seed, ChunkBuffer buffer){
//        long start = System.currentTimeMillis();
        int numEntities = 100;
        ashley = Statics.ashley;

        for (int i = 0; i < numEntities; i++) {
            createRandomEntity(MathUtils.random(chunkX * 64 * 32, chunkX * 64 * 32 + 2048), MathUtils.random(chunkY * 64 * 32, chunkY * 64 * 32 + 2048), buffer);
        }

        RandomXS128 rnd = new RandomXS128(seed+chunkX+chunkY);
        for (int i = 0; i < 50; i++) {
            float x = ((chunkX * 64 * 32) + (int)(rnd.nextFloat()*64)*32);
            float y = ((chunkY * 64 * 32) + (int)(rnd.nextFloat()*64)*32);
            createRandomDecoEntity(x, y, buffer);
        }
//        MainGame.log("ec time: "+(System.currentTimeMillis()-start));
    }

    private void createRandomEntity(float x, float y, ChunkBuffer buffer){
        ChunkCell biomCell = buffer.getCellLazy((int)x/32, (int)y/32, ChunkBuffer.BIOME);
        ChunkCell collisionCell = buffer.getCellLazy((int)x/32, (int)y/32, ChunkBuffer.COLLISION);
        if(biomCell == null){
            return;
        }
        if(collisionCell.value == TileCollisionMapper.GROUND || collisionCell.value == TileCollisionMapper.HIGH_GROUND) {
//                createSlime(Textures.SLIME_BLUE, x, y);
            MobTemplate mob = Statics.mob.getRandomTemplateForBiome(biomCell.value);
            createMob(mob, x, y);
        }
//        if(biomCell.value == ChunkBuffer.BLUE_BIOM){
//            if(collisionCell.value == TileCollisionMapper.GROUND || collisionCell.value == TileCollisionMapper.HIGH_GROUND) {
////                createSlime(Textures.SLIME_BLUE, x, y);
//                MobTemplate mob = Statics.mob.getRandomTemplateForBiome(biomCell.value);
//                createMob(mob, x, y);
//            }
//        } else if(biomCell.value == ChunkBuffer.GREEN_BIOM){
//            MobTemplate mob = Statics.mob.getRandomTemplateForBiome(biomCell.value);
//            createMob(mob, x, y);
////            createSlime(Textures.SLIME_GREEN, x, y);
//        } else if(biomCell.value == ChunkBuffer.PURPLE_BIOM){
////            createSlime(Textures.SLIME_PURPLE, x,y);
//            MobTemplate mob = Statics.mob.getRandomTemplateForBiome(biomCell.value);
//            createMob(mob, x, y);
//        } else if(biomCell.value == ChunkBuffer.RED_BIOM){
//            if(collisionCell.value == TileCollisionMapper.HIGH_GROUND_FLUID) {
////                createSlime(Textures.SLIME_RED, x, y);
//                MobTemplate mob = Statics.mob.getRandomTemplateForBiome(biomCell.value);
//                createMob(mob, x, y);
//            }
//        } else if(biomCell.value == ChunkBuffer.YELLOW_BIOM){
//            createEyeball(x,y);
//        }
//        if(biomCell.value == ChunkBuffer.GREEN_BIOM){
//            createSlime(x,y);
//        } else if(biomCell.value == ChunkBuffer.RED_BIOM){
//            createEyeball(x,y);
//        } else if(biomCell.value == TileCollisionMapper.HIGH_GROUND_FLUID){
//
//        } else if(biomCell.value == TileCollisionMapper.FLUID){
//
//        }
    }

    private void createRandomDecoEntity(float x, float y, ChunkBuffer buffer){
        ChunkCell cell = buffer.getCellLazy((int)x/32, (int)y/32, ChunkBuffer.COLLISION);
        if(cell == null){
            return;
        }
        if(cell.value == TileCollisionMapper.HIGH_GROUND){
            createLavaDeco(x, y, buffer);
        } else if(cell.value == TileCollisionMapper.GROUND){

        } else if(cell.value == TileCollisionMapper.HIGH_GROUND_FLUID){

        } else if(cell.value == TileCollisionMapper.FLUID){

        }
    }

    public void createTerraFormingRamp(float x, float y, ChunkBuffer buffer) {
        x = x-(x%32);
        y = y-(y%32);

        //Check valid position here
        boolean valid = false;
        for(;y > y-(32*3); y = y-32){
            int value = buffer.getCellLazy((int) x / 32, (int) y / 32, ChunkBuffer.DECO1).value;
            if(value == TileMapper.LAVA_STONE_BOTTOM_MIDDLE_2){
                int value2 = buffer.getCellLazy(((int) x / 32) - 1, (int) y / 32, ChunkBuffer.DECO1).value;
                int value3 = buffer.getCellLazy(((int) x / 32) + 1, (int) y / 32, ChunkBuffer.DECO1).value;
                int value4 = buffer.getCellLazy(((int) x / 32) - 1, (int) y / 32, ChunkBuffer.COLLISION).value;
                int value5 = buffer.getCellLazy(((int) x / 32) + 1, (int) y / 32, ChunkBuffer.COLLISION).value;
                if(value2 == TileMapper.LAVA_STONE_BOTTOM_MIDDLE_2 && value3 == TileMapper.LAVA_STONE_BOTTOM_MIDDLE_2 && value4 == TileCollisionMapper.HIGH_GROUND_BORDER && value5 == TileCollisionMapper.HIGH_GROUND_BORDER){
                    valid = true;
                    break;
                }
            }
        }
        if(!valid){
            return;
        }


        //MID BOT
        Entity entity = ashley.createEntity();
        PositionComponent pc = ashley.createComponent(PositionComponent.class);
        pc.x = x;
        pc.y = y;
        entity.add(pc);
        SpriteComponent sc = ashley.createComponent(SpriteComponent.class);
        sc.region = Tile.getTileForBiome(TileMapper.TERRA_FORMING_MID_BOT, buffer.getCellLazy((int)pc.x/32, (int)pc.y/32, ChunkBuffer.BIOME).value).getTextureRegion();
        entity.add(sc);
        MapCollisionComponent mc = ashley.createComponent(MapCollisionComponent.class);
        mc.value = TileCollisionMapper.HIGH_GROUND;
        entity.add(mc);
        entity.add(ashley.createComponent(RenderAsTileComponent.class));
        entity.add(ashley.createComponent(AvoidGcComponent.class));
        ashley.addEntity(entity);

        //MID MID
        entity = ashley.createEntity();
        pc = ashley.createComponent(PositionComponent.class);
        pc.x = x;
        pc.y = y+32;
        entity.add(pc);
        sc = ashley.createComponent(SpriteComponent.class);
        sc.region = Tile.getTileForBiome(TileMapper.TERRA_FORMING_MID, buffer.getCellLazy((int)pc.x/32, (int)pc.y/32, ChunkBuffer.BIOME).value).getTextureRegion();
        entity.add(sc);
        mc = ashley.createComponent(MapCollisionComponent.class);
        mc.value = TileCollisionMapper.HIGH_GROUND;
        entity.add(mc);
        entity.add(ashley.createComponent(RenderAsTileComponent.class));
        entity.add(ashley.createComponent(AvoidGcComponent.class));
        ashley.addEntity(entity);

        //MID TOP
        entity = ashley.createEntity();
        pc = ashley.createComponent(PositionComponent.class);
        pc.x = x;
        pc.y = y+64;
        entity.add(pc);
        sc = ashley.createComponent(SpriteComponent.class);
        sc.region = Tile.getTileForBiome(TileMapper.TERRA_FORMING_MID_TOP, buffer.getCellLazy((int)pc.x/32, (int)pc.y/32, ChunkBuffer.BIOME).value).getTextureRegion();
        entity.add(sc);
        mc = ashley.createComponent(MapCollisionComponent.class);
        mc.value = TileCollisionMapper.HIGH_GROUND;
        entity.add(mc);
        entity.add(ashley.createComponent(RenderAsTileComponent.class));
        entity.add(ashley.createComponent(AvoidGcComponent.class));
        ashley.addEntity(entity);

        //LEFT
        float xLeft = x-32;
        //LEFT BOT
        entity = ashley.createEntity();
        pc = ashley.createComponent(PositionComponent.class);
        pc.x = xLeft;
        pc.y = y;
        entity.add(pc);
        sc = ashley.createComponent(SpriteComponent.class);
        sc.region = Tile.getTileForBiome(TileMapper.TERRA_FORMING_LEFT_BOT, buffer.getCellLazy((int)pc.x/32, (int)pc.y/32, ChunkBuffer.BIOME).value).getTextureRegion();
        entity.add(sc);
        mc = ashley.createComponent(MapCollisionComponent.class);
        mc.value = TileCollisionMapper.HIGH_GROUND_BORDER;
        entity.add(mc);
        entity.add(ashley.createComponent(RenderAsTileComponent.class));
        entity.add(ashley.createComponent(AvoidGcComponent.class));
        ashley.addEntity(entity);

        //LEFT MID
        entity = ashley.createEntity();
        pc = ashley.createComponent(PositionComponent.class);
        pc.x = xLeft;
        pc.y = y+32;
        entity.add(pc);
        sc = ashley.createComponent(SpriteComponent.class);
        sc.region = Tile.getTileForBiome(TileMapper.TERRA_FORMING_LEFT, buffer.getCellLazy((int)pc.x/32, (int)pc.y/32, ChunkBuffer.BIOME).value).getTextureRegion();
        entity.add(sc);
        mc = ashley.createComponent(MapCollisionComponent.class);
        mc.value = TileCollisionMapper.HIGH_GROUND_BORDER;
        entity.add(mc);
        entity.add(ashley.createComponent(RenderAsTileComponent.class));
        entity.add(ashley.createComponent(AvoidGcComponent.class));
        ashley.addEntity(entity);

        //LEFT TOP
        entity = ashley.createEntity();
        pc = ashley.createComponent(PositionComponent.class);
        pc.x = xLeft;
        pc.y = y+64;
        entity.add(pc);
        sc = ashley.createComponent(SpriteComponent.class);
        sc.region = Tile.getTileForBiome(TileMapper.TERRA_FORMING_LEFT_TOP, buffer.getCellLazy((int)pc.x/32, (int)pc.y/32, ChunkBuffer.BIOME).value).getTextureRegion();
        entity.add(sc);
        mc = ashley.createComponent(MapCollisionComponent.class);
        mc.value = TileCollisionMapper.HIGH_GROUND_BORDER;
        entity.add(mc);
        entity.add(ashley.createComponent(RenderAsTileComponent.class));
        entity.add(ashley.createComponent(AvoidGcComponent.class));
        ashley.addEntity(entity);

        //RIGHT
        float xRight = x+32;
        //RIGHT BOT
        entity = ashley.createEntity();
        pc = ashley.createComponent(PositionComponent.class);
        pc.x = xRight;
        pc.y = y;
        entity.add(pc);
        sc = ashley.createComponent(SpriteComponent.class);
        sc.region = Tile.getTileForBiome(TileMapper.TERRA_FORMING_RIGHT_BOT, buffer.getCellLazy((int)pc.x/32, (int)pc.y/32, ChunkBuffer.BIOME).value).getTextureRegion();
        entity.add(sc);
        mc = ashley.createComponent(MapCollisionComponent.class);
        mc.value = TileCollisionMapper.HIGH_GROUND_BORDER;
        entity.add(mc);
        entity.add(ashley.createComponent(RenderAsTileComponent.class));
        entity.add(ashley.createComponent(AvoidGcComponent.class));
        ashley.addEntity(entity);

        //RIGHT MID
        entity = ashley.createEntity();
        pc = ashley.createComponent(PositionComponent.class);
        pc.x = xRight;
        pc.y = y+32;
        entity.add(pc);
        sc = ashley.createComponent(SpriteComponent.class);
        sc.region = Tile.getTileForBiome(TileMapper.TERRA_FORMING_RIGHT, buffer.getCellLazy((int)pc.x/32, (int)pc.y/32, ChunkBuffer.BIOME).value).getTextureRegion();
        entity.add(sc);
        mc = ashley.createComponent(MapCollisionComponent.class);
        mc.value = TileCollisionMapper.HIGH_GROUND_BORDER;
        entity.add(mc);
        entity.add(ashley.createComponent(RenderAsTileComponent.class));
        entity.add(ashley.createComponent(AvoidGcComponent.class));
        ashley.addEntity(entity);

        //RIGHT TOP
        entity = ashley.createEntity();
        pc = ashley.createComponent(PositionComponent.class);
        pc.x = xRight;
        pc.y = y+64;
        entity.add(pc);
        sc = ashley.createComponent(SpriteComponent.class);
        sc.region = Tile.getTileForBiome(TileMapper.TERRA_FORMING_RIGHT_TOP, buffer.getCellLazy((int)pc.x/32, (int)pc.y/32, ChunkBuffer.BIOME).value).getTextureRegion();
        entity.add(sc);
        mc = ashley.createComponent(MapCollisionComponent.class);
        mc.value = TileCollisionMapper.HIGH_GROUND_BORDER;
        entity.add(mc);
        entity.add(ashley.createComponent(RenderAsTileComponent.class));
        entity.add(ashley.createComponent(AvoidGcComponent.class));
        ashley.addEntity(entity);
    }

    private void createLavaDeco(float x, float y, ChunkBuffer buffer) {
        Entity entity = ashley.createEntity();
        PositionComponent pc = ashley.createComponent(PositionComponent.class);
        pc.x = x;
        pc.y = y;
        entity.add(pc);
        SpriteComponent sc = ashley.createComponent(SpriteComponent.class);
//        sc.region = Tile.LAVA_DECO_1.getTextureRegion();
        sc.region = Tile.getTileForBiome(TileMapper.LAVA_DECO_1, buffer.getCellLazy((int)x/32, (int)y/32, ChunkBuffer.BIOME).value).getTextureRegion();
        entity.add(sc);
        MapCollisionComponent mc = ashley.createComponent(MapCollisionComponent.class);
        mc.value = TileCollisionMapper.ENTITY;
        entity.add(mc);
        ashley.addEntity(entity);
    }

    private void createEyeball(float x, float y){
        Animation[] animations = Statics.animation.getMovementAnimations(Textures.EYE, true, 4, 3);
        Animation walkAnimationU = animations[0];
        Animation walkAnimationD = animations[1];
        Animation walkAnimationL = animations[2];
        Animation walkAnimationR = animations[3];

        Entity entity = ashley.createEntity();
        PositionComponent pc = ashley.createComponent(PositionComponent.class);
        pc.x = x;
        pc.y = y;
        entity.add(pc);
        AnimationComponent ac = ashley.createComponent(AnimationComponent.class);
        ac.idleAnimation = walkAnimationD;
        ac.leftAnimation = walkAnimationL;
        ac.rightAnimation = walkAnimationR;
        ac.upAnimation = walkAnimationU;
        ac.downAnimation = walkAnimationD;
        entity.add(ac);
        CollisionComponent cc = ashley.createComponent(CollisionComponent.class);
        cc.groundCollision = TileCollisionMapper.normalBorderCollision;
        entity.add(cc);
        ElementsComponent ec = ashley.createComponent(ElementsComponent.class);
        ec.blue = MathUtils.random(-2f, 2f);
        ec.red = MathUtils.random(-2f, 2f);
        ec.green = MathUtils.random(-2f, 2f);
        ec.purple = MathUtils.random(-2f, 2f);
        ec.yellow = MathUtils.random(-2f, 2f);
        entity.add(ec);
        HealthComponent hc = ashley.createComponent(HealthComponent.class);
        hc.maxHealth = MobStats.EYE_HEALTH;
        hc.health = MobStats.EYE_HEALTH;
        entity.add(hc);
        AiComponent aiComponent = ashley.createComponent(AiComponent.class);
        aiComponent.ai = MobStats.EYE_AI;
        entity.add(aiComponent);
        VelocityComponent vc = ashley.createComponent(VelocityComponent.class);
        vc.direction = MathUtils.random(0, 7);
        vc.speed = 20;
        entity.add(vc);
        ashley.addEntity(entity);
    }

    private void createSlime(Textures texture, float x, float y){
        Animation[] animations = Statics.animation.getMovementAnimations(texture, true, 4, 3);
        Animation walkAnimationU = animations[0];
        Animation walkAnimationD = animations[1];
        Animation walkAnimationL = animations[2];
        Animation walkAnimationR = animations[3];

        Entity entity = ashley.createEntity();
        PositionComponent pc = ashley.createComponent(PositionComponent.class);
        pc.x = x;
        pc.y = y;
        entity.add(pc);
        AnimationComponent ac = ashley.createComponent(AnimationComponent.class);
        ac.idleAnimation = walkAnimationD;
        ac.leftAnimation = walkAnimationL;
        ac.rightAnimation = walkAnimationR;
        ac.upAnimation = walkAnimationU;
        ac.downAnimation = walkAnimationD;
        entity.add(ac);
        CollisionComponent cc = ashley.createComponent(CollisionComponent.class);
        cc.groundCollision = TileCollisionMapper.normalBorderCollision;
        cc.collisionListener = new CollisionComponent.CollisionListener() {
            @Override
            public boolean onCollision(Entity other, Entity self) {
                if(ComponentMappers.player.has(other)){
                    HealthComponent hc = ComponentMappers.health.get(other);
                    if(hc.immuneTime == 0){
                        hc.health -= 5;
                        hc.health = MathUtils.clamp(hc.health, 0, hc.maxHealth);
                        hc.immuneTime = hc.maxImmuneTime;
                    }
                }
                return false;
            }
        };
        entity.add(cc);
        ElementsComponent ec = ashley.createComponent(ElementsComponent.class);
        ec.blue = MathUtils.random(-2f, 2f);
        ec.red = MathUtils.random(-2f, 2f);
        ec.green = MathUtils.random(-2f, 2f);
        ec.purple = MathUtils.random(-2f, 2f);
        ec.yellow = MathUtils.random(-2f, 2f);
        entity.add(ec);
        AiComponent aiComponent = ashley.createComponent(AiComponent.class);
        aiComponent.ai = MobStats.SLIME_AI;
        entity.add(aiComponent);
        InventoryComponent ic = ashley.createComponent(InventoryComponent.class);
        entity.add(ic);
        Statics.item.populateInventory(entity, 1);
        HealthComponent hc = ashley.createComponent(HealthComponent.class);
        hc.maxHealth = MobStats.SLIME_HEALTH;
        hc.health = MobStats.SLIME_HEALTH;
        entity.add(hc);
        VelocityComponent vc = ashley.createComponent(VelocityComponent.class);
        vc.direction = MathUtils.random(0, 7);
        vc.speed = 20;
        entity.add(vc);
        ashley.addEntity(entity);
    }

    private void createMob(final MobTemplate template, float x, float y){
        if(template == null){
            return;
        }
        Entity entity = ashley.createEntity();

        PositionComponent pc = ashley.createComponent(PositionComponent.class);
        pc.x = x;
        pc.y = y;
        entity.add(pc);

        Animation[] animations = Statics.animation.getMovementAnimations(template.texture, true, 4, 3);
        Animation walkAnimationU = animations[0];
        Animation walkAnimationD = animations[1];
        Animation walkAnimationL = animations[2];
        Animation walkAnimationR = animations[3];
        AnimationComponent ac = ashley.createComponent(AnimationComponent.class);
        ac.idleAnimation = walkAnimationD;
        ac.leftAnimation = walkAnimationL;
        ac.rightAnimation = walkAnimationR;
        ac.upAnimation = walkAnimationU;
        ac.downAnimation = walkAnimationD;
        entity.add(ac);

        CollisionComponent cc = ashley.createComponent(CollisionComponent.class);
        cc.groundCollision = TileCollisionMapper.normalBorderCollision;
        cc.collisionListener = new CollisionComponent.CollisionListener() {
            @Override
            public boolean onCollision(Entity other, Entity self) {
                if(ComponentMappers.player.has(other)){
                    HealthComponent hc = ComponentMappers.health.get(other);
                    if(hc.immuneTime == 0){
                        hc.health -= template.baseDamage;
                        hc.health = MathUtils.clamp(hc.health, 0, hc.maxHealth);
                        hc.immuneTime = hc.maxImmuneTime;
                    }
                }
                return false;
            }
        };
        entity.add(cc);

        ElementsComponent ec = ashley.createComponent(ElementsComponent.class);
        ec.blue = template.blue;
        ec.red = template.red;
        ec.green = template.green;
        ec.purple = template.purple;
        ec.yellow = template.yellow;
        entity.add(ec);

        AiComponent aiComponent = ashley.createComponent(AiComponent.class);
        switch (template.ai){
            case FOLLOW:
                aiComponent.ai = MobStats.SLIME_AI;
                break;
            case ESCAPE:
                aiComponent.ai = MobStats.EYE_AI;
                break;
        }
        entity.add(aiComponent);

        InventoryComponent ic = ashley.createComponent(InventoryComponent.class);
        entity.add(ic);
        Statics.item.populateInventory(entity, 0);

        HealthComponent hc = ashley.createComponent(HealthComponent.class);
        hc.maxHealth = template.maxHealth;
        hc.health = template.maxHealth;
        entity.add(hc);

        VelocityComponent vc = ashley.createComponent(VelocityComponent.class);
        vc.direction = MathUtils.random(0, 7);
        vc.speed = template.speed;
        entity.add(vc);

        TargetComponent target = Statics.ashley.createComponent(TargetComponent.class);
        entity.add(target);
        Weapon weapon;
        switch (template.weapon){
            case NEUTRAL:
                weapon = new Shooter();
                break;
            case LASER:
                weapon = new Laser();
                break;
            case FLAMETHROWER:
                weapon = new Flamethrower();
                break;
            case WATERTHROWER:
                weapon = new Watercannon();
                break;
            case SLIMEBALLS:
                weapon = new AcidShooter();
                break;
            case ENERGYGUN:
                weapon = new ParticleShooter();
                break;
            default:
                weapon = new Shooter();
                break;
        }
        weapon.setRange(template.range);
        Statics.attack.createAttack(entity, weapon);
        target.isShooting = false;

        ashley.addEntity(entity);
    }

}
