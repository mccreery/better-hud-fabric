package mccreery.betterhud.internal.element;

import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.property.BooleanProperty;
import mccreery.betterhud.internal.render.Color;
import net.minecraft.item.ItemStack;

public class HandBar extends EquipmentDisplay {
    private final BooleanProperty showItem;
    private final BooleanProperty offHand;
    private final BooleanProperty showBars;
    private final BooleanProperty showNonTools;

    public HandBar() {
        showItem = new BooleanProperty("showItem", true);
        addProperty(showItem);

        showBars = new BooleanProperty("bars", true);
        addProperty(showBars);

        offHand = new BooleanProperty("offhand", false);
        addProperty(offHand);

        showNonTools = new BooleanProperty("showNonTools", false);
        addProperty(showNonTools);
    }

    public void renderBar(ItemStack stack, int x, int y) {
        boolean isTool = stack.isDamageable();
        if(stack == null || !showNonTools.get() && !isTool) return;

        String text = getText(stack);

        int width = 0;
        if(showItem.get()) width += 21;

        if(text != null) {
            width += MC.fontRenderer.getStringWidth(text);
        }

        if(showItem.get()) {
            MC.getProfiler().startSection("items");
            GlUtil.renderSingleItem(stack, x + 90 - width / 2, y);
            MC.getProfiler().endSection();
        }

        if(text != null) {
            MC.getProfiler().startSection("text");
            GlUtil.drawString(text, new Point(x + 90 - width / 2 + (showItem.get() ? 21 : 0), y + 4), Direction.NORTH_WEST, Color.WHITE);
            MC.getProfiler().endSection();
        }

        if(isTool && showBars.get()) {
            MC.getProfiler().startSection("bars");
            GlUtil.drawDamageBar(new Rectangle(x, y + 16, 180, 2), stack, false);
            MC.getProfiler().endSection();
        }
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        Rectangle bounds = position.applyTo(new Rectangle(180, offHand.get() ? 41 : 18));
        renderBar(MC.player.getHeldItemMainhand(), bounds.getX(), bounds.getBottom() - 18);

        if(offHand.get()) {
            renderBar(MC.player.getHeldItemOffhand(), bounds.getX(), bounds.getY());
        }
        return bounds;
    }
}
