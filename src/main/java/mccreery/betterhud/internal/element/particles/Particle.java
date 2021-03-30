package mccreery.betterhud.internal.element.particles;

import mccreery.betterhud.api.geometry.Rectangle;
import net.minecraft.util.Tickable;

public interface Particle extends Tickable {
    boolean isDead();
    boolean shouldRender();
    void render(float partialTicks);

    static Rectangle getScreen() {
        if(MANAGER != null) {
            Rectangle bounds = MANAGER.getScreen();
            if(bounds != null) return bounds;
        }
        return Rectangle.empty();
    }
}
