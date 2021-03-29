package mccreery.betterhud.internal.element.text;

import static jobicade.betterhud.BetterHud.MANAGER;
import static jobicade.betterhud.BetterHud.MC;
import static jobicade.betterhud.BetterHud.SPACER;

import java.util.Arrays;
import java.util.List;

import jobicade.betterhud.element.settings.Legend;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.property.EnumProperty;
import mccreery.betterhud.api.HudRenderContext;
import jobicade.betterhud.geom.Direction;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockRayTraceResult;

public class Distance extends TextElement {
    private EnumProperty mode;

    public Distance() {
        super("distance");

        addSetting(new Legend("misc"));
        mode = new EnumProperty("mode", 3);
        addSetting(mode);
    }

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
