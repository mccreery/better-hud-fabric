package mccreery.betterhud.internal.element.particles;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.property.EnumProperty;
import mccreery.betterhud.internal.BetterHud;
import net.minecraft.util.Tickable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ParticleOverlay extends HudElement implements Tickable {
    protected EnumProperty<ParticleDensity> density;
    protected final List<Particle> particles = new CopyOnWriteArrayList<>();

    public ParticleOverlay() {
        density = new EnumProperty<>("density", ParticleDensity.NORMAL);
        addProperty(density);
    }

    /** Called each tick while enabled to spawn new particles.
     * Default implementation kills dead particles */
    protected void updateParticles() {
        particles.removeIf(Particle::isDead);
    }

    @Override
    public void tick() {
        if (BetterHud.getConfigManager().getModSettings().getEnabled().contains(this)
                && !MC.isGamePaused()) {
            particles.forEach(Particle::tick);
            updateParticles();
        }
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        for(Particle particle : particles) {
            particle.render(context.getPartialTicks());
        }
        return MANAGER.getScreen();
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        return !particles.isEmpty();
    }

    public enum ParticleDensity {
        SPARSE,
        NORMAL,
        DENSE,
        DENSER
    }
}
