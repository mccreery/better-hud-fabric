package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Rectangle;

import java.util.Arrays;
import java.util.List;

public class Distance extends TextElement {
    @Override
    protected Rectangle moveRect(Rectangle bounds) {
        if(position.isDirection(Direction.CENTER)) {
            return bounds.align(MANAGER.getScreen().getAnchor(Direction.CENTER).sub(SPACER, SPACER), Direction.SOUTH_EAST);
        } else {
            return super.moveRect(bounds);
        }
    }

    @Override
    protected Rectangle getPadding() {
        return Rectangle.createPadding(border ? 2 : 0);
    }

    @Override
    protected Rectangle render(HudRenderContext context, List<String> text) {
        border = mode.getIndex() == 2;
        return super.render(context, text);
    }

    @Override
    protected List<String> getText() {
        BlockRayTraceResult trace = (BlockRayTraceResult)MC.getRenderViewEntity().pick(200, 1.0F, false);

        if(trace != null) {
            long distance = Math.round(Math.sqrt(trace.getPos().distanceSq(MC.player.getPosX(), MC.player.getPosY(), MC.player.getPosZ(), true)));

            if(mode.getIndex() == 2) {
                return Arrays.asList(String.valueOf(distance));
            } else {
                return Arrays.asList(I18n.format("betterHud.hud.distance." + mode.getIndex(), String.valueOf(distance)));
            }
        } else {
            return null;
        }
    }
}
