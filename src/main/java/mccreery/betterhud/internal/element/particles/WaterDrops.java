package mccreery.betterhud.internal.element.particles;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class WaterDrops extends ParticleOverlay {
    private boolean wasUnderwaterLastTick = false;

    @Override
    protected void updateParticles() {
        boolean isUnderwater = MC.player.isInWater();

        if(isUnderwater) {
            particles.clear();
        } else {
            super.updateParticles();
            Collection<Particle> toSpawn = new ArrayList<>();

            if(wasUnderwaterLastTick) {
                int count = getParticleCount();

                for(int i = 0; i < count; i++) {
                    toSpawn.add(ParticleWater.createRandom());
                }
            }

            BlockPos camera = new BlockPos(MC.player.getEyePosition(1));
            if (MC.world.isRainingAt(camera)
                    && new RandomWrapper(new Random()).nextTrial(getParticleChance())) {
                toSpawn.add(ParticleWater.createRandom());
            }

            // Atomic operation means underlying CopyOnWriteArrayList only copies once
            particles.addAll(toSpawn);
        }
        wasUnderwaterLastTick = isUnderwater;
    }

    private float getParticleChance() {
        return 0.2f + density.getIndex() * 0.15f;
    }

    private int getParticleCount() {
        return new Random().nextInt((density.getIndex() + 1) * 20);
    }
}
