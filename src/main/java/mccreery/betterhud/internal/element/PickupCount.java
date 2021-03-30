package mccreery.betterhud.internal.element;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.layout.Label;
import mccreery.betterhud.api.property.DoubleProperty;
import mccreery.betterhud.internal.render.Color;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PickupCount extends HudElement {
    private final DoubleProperty maxStacks;
    private final DoubleProperty fadeAfter;
    public final List<StackNode> stacks = new ArrayList<>();

    public PickupCount() {
        fadeAfter = new DoubleProperty("fadeAfter", 5.0, 1.0, 30.0);
        addProperty(fadeAfter);

        maxStacks = new DoubleProperty("maxStacks", 5, 1, 11) {
            @Override
            public String getDisplayValue(double scaledValue) {
                return scaledValue == getMaximum() ? I18n.format("betterHud.value.unlimited") : super.getDisplayValue(scaledValue);
            }
        };
        maxStacks.setInterval(1);
        addProperty(maxStacks);
    }

    /**
     * Searches for and removes an equivalent stack. Stacks are considered
     * equivalent if their items are equivalent, ignoring max stack size.
     *
     * @param stack The item to search for.
     * @return The removed item stack, if any, or {@code null}.
     */
    private synchronized StackNode removeStack(ItemStack stack) {
        for(StackNode node : stacks) {
            if(ItemHandlerHelper.canItemStacksStack(stack, node.stack)) {
                stacks.remove(node);
                return node;
            }
        }
        return null;
    }

    /**
     * Brings a stack to the front of the list of recent stacks.
     *
     * @param stack The stack to find and refresh.
     */
    public synchronized void refreshStack(ItemStack stack) {
        StackNode node = removeStack(stack);

        if(node != null) {
            node.increaseStackSize(stack.getCount());
        } else {
            node = new StackNode(stack);
        }
        stacks.add(0, node);
    }

    /**
     * Returns the list of recently picked up stacks, newest first.
     * Expired stacks are removed, and the limit is enforced before returning.
     *
     * @return The list of recently picked up stacks.
     */
    private synchronized List<StackNode> getStacks() {
        stacks.removeIf(StackNode::isDead);

        int limit = (int)maxStacks.getValue();
        if(limit < 11 && limit < stacks.size()) {
            stacks.subList(limit, stacks.size()).clear();
        }
        return stacks;
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        List<StackNode> stacks = getStacks();
        Rectangle bounds;

        synchronized(this) {
            Grid<StackNode> grid = new Grid<>(new Point(1, stacks.size()), stacks)
                .setAlignment(position.getContentAlignment())
                .setCellAlignment(position.getContentAlignment());

            bounds = new Rectangle(grid.getPreferredSize());

            if(position.isDirection(Direction.CENTER)) {
                bounds = bounds.align(MANAGER.getScreen().getAnchor(Direction.CENTER).add(5, 5), Direction.NORTH_WEST);
            } else {
                bounds = position.applyTo(bounds);
            }
            grid.setBounds(bounds).render();
        }
        return bounds;
    }

    private class StackNode extends DefaultBoxed {
        private final ItemStack stack;
        private long updateCounter;

        public StackNode(ItemStack stack) {
            this.stack = stack;
            this.updateCounter = MC.ingameGUI.getTicks();
        }

        public void increaseStackSize(int size) {
            stack.setCount(stack.getCount() + size);
            this.updateCounter = MC.ingameGUI.getTicks();
        }

        private Label getLabel() {
            return new Label(stack.getCount() + " " + stack.getDisplayName())
                .setColor(Color.WHITE.withAlpha(Math.round(getOpacity() * 255)));
        }

        private float getOpacity() {
            return 1.0f - (MC.ingameGUI.getTicks() - updateCounter) / fadeAfter.getValue();
        }

        private boolean isDead() {
            return getOpacity() <= 0;
        }

        @Override
        public Size negotiateSize(Point size) {
            return getLabel().getPreferredSize().withHeight(16).add(21, 0);
        }

        @Override
        public void render() {
            Direction alignment = position.getContentAlignment().withRow(1);
            GlUtil.renderSingleItem(stack, new Rectangle(16, 16).anchor(bounds, alignment).getPosition());

            Label label = getLabel();
            label.setBounds(new Rectangle(label.getPreferredSize()).anchor(bounds, alignment.mirrorCol())).render();
        }
    }
}
