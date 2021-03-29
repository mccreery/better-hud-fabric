package mccreery.betterhud.internal.element.particles;

import mccreery.betterhud.api.geometry.Rectangle;
import jobicade.betterhud.util.Tickable;

import static jobicade.betterhud.BetterHud.MANAGER;

public interface Particle extends Tickable {
    public boolean isDead();
    public boolean shouldRender();
    public void render(float partialTicks);

    public static Rectangle getScreen() {
        if(MANAGER != null) {
            Rectangle bounds = MANAGER.getScreen();
            if(bounds != null) return bounds;
        }
        return Rectangle.empty();
    }
}
