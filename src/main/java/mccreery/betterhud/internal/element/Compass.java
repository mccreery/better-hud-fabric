package mccreery.betterhud.internal.element;

import com.mojang.blaze3d.systems.RenderSystem;
import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.property.EnumProperty;
import mccreery.betterhud.internal.render.Color;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;

public class Compass extends HudElement {
    private static final String[] DIRECTIONS = { "S", "E", "N", "W" };

    private final EnumProperty<RequiredItemSlot> requireItem;

    private static final int[] notchX = new int[9];

    static {
        int x = 0;

        for(double i = 0.1; i <= 0.9; i += 0.1, x++) {
            notchX[x] = (int) (Math.asin(i) / Math.PI * 180);
        }
    }

    public Compass() {
        requireItem = new EnumProperty<>("requireItem", RequiredItemSlot.NONE);
        addProperty(requireItem);
    }

    private void drawBackground(Rectangle bounds) {
        GlUtil.drawRect(bounds, new Color(170, 0, 0, 0));
        GlUtil.drawRect(bounds.grow(-50, 0, -50, 0), new Color(85, 85, 85, 85));

        Direction alignment = position.getContentAlignment();

        Rectangle smallRect = bounds.grow(2);
        Rectangle largeNotch = new Rectangle(1, 7);

        Rectangle smallNotch = new Rectangle(1, 6);
        Rectangle largeRect = bounds.grow(0, 3, 0, 3);

        if(showNotches.get()) {
            for(int loc : notchX) {
                Rectangle notchTemp = smallNotch.anchor(smallRect, alignment);
                GlUtil.drawRect(notchTemp.translate(loc, 0), Color.WHITE);
                GlUtil.drawRect(notchTemp.translate(-loc, 0), Color.WHITE);
            }
        }

        GlUtil.drawRect(largeNotch.anchor(largeRect, alignment.withCol(0)), Color.RED);
        GlUtil.drawRect(largeNotch.anchor(largeRect, alignment.withCol(1)), Color.RED);
        GlUtil.drawRect(largeNotch.anchor(largeRect, alignment.withCol(2)), Color.RED);
    }

    private void drawDirections(Rectangle bounds) {
        float angle = (float)Math.toRadians(MC.player.rotationYaw);

        float radius = bounds.getWidth() / 2 + SPACER;
        boolean bottom = position.getContentAlignment() == Direction.SOUTH;

        Point origin = bounds.grow(-2).getAnchor(position.getContentAlignment());

        for(int i = 0; i < 4; i++, angle += Math.PI / 2) {
            double cos = Math.cos(angle);

            Point letter = origin.add(-(int)(Math.sin(angle) * radius), 0);
            double scale = 1 + directionScaling.getValue() * cos * 2;

            RenderSystem.pushMatrix();

            RenderSystem.translatef(letter.getX(), letter.getY(), 0);
            GlUtil.scale((float)scale);

            Color color = i == 0 ? Color.BLUE : i == 2 ? Color.RED : Color.WHITE;
            color = color.withAlpha((int)(((cos + 1) / 2) * 255));

            // Super low alphas can render opaque for some reason
            if(color.getAlpha() > 3) {
                GlUtil.drawString(DIRECTIONS[i], Point.zero(), bottom ? Direction.SOUTH : Direction.NORTH, color);
            }

            RenderSystem.popMatrix();
        }
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        switch(requireItem.getIndex()) {
            case 1:
                return MC.player.inventory.hasItemStack(new ItemStack(Items.COMPASS));
            case 2:
                return MC.player.getHeldItemMainhand().getItem() == Items.COMPASS
                    || MC.player.getHeldItemOffhand().getItem() == Items.COMPASS;
        }
        return true;
    }

    public String getText() {
        net.minecraft.util.Direction enumfacing = MC.player.getHorizontalFacing();

        String coord;
        Direction direction;

        switch(enumfacing) {
            case NORTH: coord = "-Z"; direction = Direction.NORTH; break;
            case SOUTH: coord = "+Z"; direction = Direction.SOUTH; break;
            case WEST: coord = "-X"; direction = Direction.WEST; break;
            case EAST: coord = "+X"; direction = Direction.EAST; break;
            default: return "?";
        }
        return I18n.format("betterHud.hud.facing", SettingDirection.localizeDirection(direction), coord);
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        Rectangle bounds;

        if(mode.getIndex() == 0) {
            bounds = position.applyTo(new Rectangle(180, 12));

            MC.getProfiler().startSection("background");
            drawBackground(bounds);
            MC.getProfiler().endStartSection("text");
            drawDirections(bounds);
            MC.getProfiler().endSection();
        } else {
            String text = getText();
            bounds = position.applyTo(new Rectangle(GlUtil.getStringSize(text)));

            MC.getProfiler().startSection("text");
            GlUtil.drawString(text, bounds.getPosition(), Direction.NORTH_WEST, Color.WHITE);
            MC.getProfiler().endSection();
        }

        return bounds;
    }

    public enum RequiredItemSlot {
        NONE,
        INVENTORY,
        HAND
    }
}
