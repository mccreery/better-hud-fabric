package mccreery.betterhud.internal.element;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.internal.render.Color;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;

public class ArrowCount extends HudElement {
    private static final ItemStack ARROW = new ItemStack(Items.ARROW, 1);

    /** Note this method only cares about arrows which can be shot by a vanilla bow
     * @return The number of arrows in the player's inventory
     * @see net.minecraft.item.ItemBow#isArrow(ItemStack) */
    private int arrowCount(PlayerEntity player) {
        int count = 0;

        for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);

            if(stack != null && stack.getItem() instanceof ArrowItem) {
                count += stack.getCount();
            }
        }
        return count;
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        ItemStack stack = MC.player.getHeldItemOffhand();
        boolean offhandHeld = stack != null && stack.getItem() == Items.BOW;

        if(overlay.get()) {
            if (OverlayHook.shouldRender(OverlayElements.OFFHAND, context) && offhandHeld) {
                return true;
            }

            if (OverlayHook.shouldRender(OverlayElements.HOTBAR, context)) {
                for(int i = 0; i < 9; i++) {
                    stack = MC.player.inventory.getStackInSlot(i);

                    if(stack != null && stack.getItem() == Items.BOW) {
                        return true;
                    }
                }
            }
            return false;
        } else if(offhandHeld) {
            return true;
        } else {
            stack = MC.player.getHeldItemMainhand();
            return stack != null && stack.getItem() == Items.BOW;
        }
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        int totalArrows = arrowCount(MC.player);

        if(overlay.get()) {
            Rectangle stackRect = new Rectangle(16, 16).anchor(OverlayElements.HOTBAR.getLastBounds().grow(-3), Direction.WEST);

            for(int i = 0; i < 9; i++) {
                ItemStack stack = MC.player.inventory.getStackInSlot(i);

                if(stack != null && stack.getItem() == Items.BOW) {
                    drawCounter(stackRect, totalArrows);
                }
                stackRect = stackRect.withX(stackRect.getX() + 20);
            }

            ItemStack stack = MC.player.inventory.getStackInSlot(40);

            if(stack != null && stack.getItem() == Items.BOW) {
                drawCounter(new Rectangle(OverlayElements.OFFHAND.getLastBounds().getPosition().add(3, 3), new Point(16, 16)), totalArrows);
            }
            return Rectangle.empty();
        } else {
            Rectangle bounds = position.applyTo(new Rectangle(16, 16));

            GlUtil.renderSingleItem(ARROW, bounds.getPosition());
            drawCounter(bounds, totalArrows);

            return bounds;
        }
    }

    private static void drawCounter(Rectangle stackRect, int count) {
        String countDisplay = String.valueOf(count);

        Rectangle text = new Rectangle(GlUtil.getStringSize(countDisplay)).align(stackRect.grow(1, 1, 1, 2).getAnchor(Direction.NORTH_EAST), Direction.NORTH_EAST);

        GlUtil.drawString(countDisplay, text.getPosition(), Direction.NORTH_WEST, Color.WHITE);
    }
}
