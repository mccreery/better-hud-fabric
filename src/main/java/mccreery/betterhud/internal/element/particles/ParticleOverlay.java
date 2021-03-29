package mccreery.betterhud.internal.element.particles;

import static jobicade.betterhud.BetterHud.MANAGER;
import static jobicade.betterhud.BetterHud.MC;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import jobicade.betterhud.BetterHud;
import jobicade.betterhud.element.OverlayElement;
import mccreery.betterhud.api.property.EnumProperty;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Rectangle;
import jobicade.betterhud.util.Tickable;

public abstract class ParticleOverlay extends OverlayElement implements Tickable {
    protected EnumProperty density;
    protected final List<Particle> particles = new CopyOnWriteArrayList<Particle>();

    public ParticleOverlay(String name) {
        super(name);
        density = new EnumProperty("density", "sparse", "normal", "dense", "denser");
        addSetting(density);
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
}
