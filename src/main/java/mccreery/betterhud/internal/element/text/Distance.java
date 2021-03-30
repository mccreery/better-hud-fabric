package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.HudRenderContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;

import java.util.Collections;
import java.util.List;

public class Distance extends TextElement {
    @Override
    protected List<String> getText(HudRenderContext context) {
        Entity cameraEntity = context.getClient().getCameraEntity();
        HitResult trace = cameraEntity.raycast(200, 1.0F, false);

        if (trace.getType() != HitResult.Type.MISS) {
            double distance = Math.sqrt(trace.squaredDistanceTo(cameraEntity));
            return Collections.singletonList(String.format("%.1f", distance));
        } else {
            return Collections.emptyList();
        }
    }
}
