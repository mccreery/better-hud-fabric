package mccreery.betterhud.internal.layout;

import com.mojang.blaze3d.systems.RenderSystem;
import mccreery.betterhud.api.geometry.Anchor;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.internal.BetterHud;
import mccreery.betterhud.internal.Bitwise;
import mccreery.betterhud.internal.render.Color;
import mccreery.betterhud.internal.render.DrawingContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class LayoutScreen extends Screen {
    private final HudLayout layout;

    public LayoutScreen(HudLayout layout) {
        super(new TranslatableText("options.hudLayout"));
        this.layout = layout;
    }

    private HudElementTree selectedTree;
    private Anchor selectedAnchor;
    /**
     * The offset from the minimum corner of the selected tree's bounds
     */
    private Point selectionOffset;

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (hoveredTree != null) {
            HandleType handleType = null;
            if (hoveredAnchor != null) {
                handleType = getHandleType(hoveredTree, hoveredAnchor);
            }

            if (handleType == HandleType.ANCHOR || handleType == HandleType.ANCHOR_SELECTED) {
                setParentAndAnchor();
            } else {
                Point cursor = new Point((int) mouseX, (int) mouseY);
                selectTreeAndAnchor(cursor);
            }
            return true;
        } else {
            selectedTree = null;
            selectedAnchor = null;
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    /**
     * Links the selected anchor to the hovered anchor without effectively moving the selected element.
     */
    private void setParentAndAnchor() {
        selectedTree.setParent(hoveredTree);
        layout.getRoots().remove(selectedTree);

        RelativePosition position = selectedTree.getPosition();
        position.setParentAnchor(hoveredAnchor);
        position.setAnchor(selectedAnchor);

        Rectangle bounds = selectedTree.getBoundsLastFrame();
        Point anchorPoint = bounds.getAnchorPoint(selectedAnchor);
        Rectangle parentBounds = hoveredTree.getBoundsLastFrame();
        Point parentAnchorPoint = Anchor.getAnchorPoint(parentBounds, hoveredAnchor);

        position.setOffset(anchorPoint.subtract(parentAnchorPoint));
    }

    /**
     * Selects the tree and anchor under the cursor and prepares for dragging.
     */
    private void selectTreeAndAnchor(Point cursor) {
        if (hoveredAnchor != null) {
            selectedAnchor = hoveredAnchor;
        } else if (selectedTree != hoveredTree) {
            // Selected anchor always defaults to linked anchor
            selectedAnchor = hoveredTree.getPosition().getAnchor();
        }
        selectedTree = hoveredTree;

        // Keep relative position of cursor on element
        Rectangle bounds = selectedTree.getBoundsLastFrame();
        selectionOffset = cursor.subtract(bounds.getPosition());

        // Automatically reset on mouse up
        setDragging(true);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        Point cursor = new Point((int)mouseX, (int)mouseY);
        updateHovered(cursor);

        // Move dragged element to cursor
        if (isDragging() && selectedTree != null) {
            RelativePosition position = selectedTree.getPosition();
            Rectangle bounds = selectedTree.getBoundsLastFrame();

            Point desiredPosition = cursor.subtract(selectionOffset);
            Point desiredAnchorPosition = desiredPosition.add(Anchor.getAnchorPoint(bounds.getSize(), position.getAnchor()));
            Point parentAnchorPoint = Anchor.getAnchorPoint(getParentBounds(selectedTree), position.getParentAnchor());

            position.setOffset(desiredAnchorPosition.subtract(parentAnchorPoint));
        }
    }

    private Rectangle getParentBounds(HudElementTree tree) {
        // Walk up the tree to find a currently rendering element
        // This parallels the logic of HudLayout.render and renderTree,
        // where the element draws relative to its closest rendering ancestor
        while (tree.getParent() != null) {
            Rectangle bounds = tree.getParent().getBoundsLastFrame();
            if (bounds != null) {
                return bounds;
            }
            tree = tree.getParent();
        }
        return new Rectangle(0, 0, width, height);
    }

    private static final int HANDLE_RANGE = 3;

    /**
     * The tree that is hovered directly or via one of its anchors. {@code null} if nothing is hovered.
     */
    private HudElementTree hoveredTree;
    /**
     * The anchor that is hovered. {@code null} if nothing is hovered or a tree is hovered directly.
     */
    private Anchor hoveredAnchor;
    /**
     * The squared distance of the anchor, if any, to the cursor.
     */
    private double distanceSquaredToCursor;

    /**
     * Searches all trees in the layout for the closest anchor in range.
     */
    private void updateHovered(Point cursor) {
        // Start with no hover and max range
        hoveredTree = null;
        hoveredAnchor = null;
        distanceSquaredToCursor = HANDLE_RANGE * HANDLE_RANGE;

        for (HudElementTree root : layout.getRoots()) {
            for (HudElementTree tree : root.breadthFirst()) {
                updateHovered(tree, cursor);
            }
        }
    }

    /**
     * Checks a single node in the tree for closer anchors.
     */
    private void updateHovered(HudElementTree tree, Point cursor) {
        Rectangle bounds = tree.getBoundsLastFrame();

        // Look for an anchor in range and closer than any previous
        for (Anchor anchor : Anchor.values()) {
            Point anchorPoint = Anchor.getAnchorPoint(bounds, anchor);
            double distanceSquared = anchorPoint.distanceSquared(cursor);

            // Both tree and anchor are set if the anchor is hovered
            if (distanceSquared < distanceSquaredToCursor) {
                hoveredTree = tree;
                hoveredAnchor = anchor;
                distanceSquaredToCursor = distanceSquared;
            }
        }

        // If no anchor is found yet, fall back to tree hover only
        if (hoveredTree == null && bounds.contains(cursor)) {
            hoveredTree = tree;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        DrawingContext context = new DrawingContext(matrixStack, Tessellator.getInstance().getBuffer());

        renderPrompt(context);
        renderSelectionBoxes(context);
        renderHandles(context);
    }

    private static final Color TRANSLUCENT = new Color(0, 0, 0, 127);

    private void renderPrompt(DrawingContext context) {
        RenderSystem.enableBlend();

        Point screenSize = new Point(width, height);
        Point screenCenter = Anchor.getAnchorPoint(screenSize, Anchor.CENTER);
        Rectangle dialogBounds = Anchor.getAlignedRectangle(screenCenter, Anchor.CENTER, new Point(200, textRenderer.fontHeight * 2 + 6));

        context.drawFilledRectangle(dialogBounds, TRANSLUCENT);

        Text leftText = new TranslatableText("hudLayout.prompt.left", new TranslatableText("key.mouse.left"));
        Text rightText = new TranslatableText("hudLayout.prompt.right", new TranslatableText("key.mouse.right"));

        Point anchorPoint = Anchor.getAnchorPoint(dialogBounds, Anchor.TOP_CENTER);

        drawCenteredText(context.getMatrixStack(), textRenderer, leftText, (int)anchorPoint.getX(), (int)anchorPoint.getY() + 2, 0xffffffff);
        drawCenteredText(context.getMatrixStack(), textRenderer, rightText, (int)anchorPoint.getX(), (int)anchorPoint.getY() + textRenderer.fontHeight + 4, 0xffffffff);
        RenderSystem.enableBlend();
    }

    private static final Color DASH_COLOR = new Color(0, 113, 188, 127);

    private void renderSelectionBoxes(DrawingContext context) {
        if (selectedTree != null) {
            Rectangle bounds = selectedTree.getBoundsLastFrame();
            RelativePosition position = selectedTree.getPosition();
            Rectangle parentBounds = getParentBounds(selectedTree);

            drawDashedRectangle(context, bounds, DASH_COLOR);
            drawDashedLine(context, bounds.getAnchorPoint(position.getAnchor()),
                    parentBounds.getAnchorPoint(position.getParentAnchor()), DASH_COLOR);
        }
        if (hoveredTree != null) {
            drawDashedRectangle(context, hoveredTree.getBoundsLastFrame(), DASH_COLOR);
        }
    }

    private static final Identifier LAYOUT_WIDGETS = new Identifier(BetterHud.ID, "textures/layout_widgets.png");
    private static final Point LAYOUT_WIDGETS_SIZE = new Point(32, 16);

    private void renderHandles(DrawingContext context) {
        client.getTextureManager().bindTexture(LAYOUT_WIDGETS);

        // Draw handles on all trees
        for (HudElementTree root : layout.getRoots()) {
            for (HudElementTree tree : root.breadthFirst()) {
                renderHandles(context, tree);
            }
        }
    }

    private void renderHandles(DrawingContext context, HudElementTree tree) {
        Rectangle bounds = tree.getBoundsLastFrame();

        for (Anchor anchor : Anchor.values()) {
            Point anchorPoint = Anchor.getAnchorPoint(bounds, anchor);

            Rectangle handleTexture = getHandleType(tree, anchor).getTexture();

            context.drawTexturedRectangle(
                    Anchor.getAlignedRectangle(anchorPoint, Anchor.CENTER, handleTexture.getSize()),
                    handleTexture, LAYOUT_WIDGETS_SIZE);
        }
    }

    /**
     * Determines the click action and icon corresponding to a handle.
     */
    private HandleType getHandleType(HudElementTree tree, Anchor anchor) {
        boolean isParentAnchor;
        boolean isChildAnchor;

        if (selectedTree != null) {
            RelativePosition position = selectedTree.getPosition();
            isParentAnchor = tree == selectedTree.getParent() && anchor == position.getParentAnchor();
            isChildAnchor = tree == selectedTree && anchor == position.getAnchor();
        } else {
            isParentAnchor = false;
            isChildAnchor = false;
        }

        boolean anchoring = hasShiftDown();

        boolean hovered = tree == hoveredTree && anchor == hoveredAnchor;
        boolean selected = tree == selectedTree && anchor == selectedAnchor;

        boolean otherTreeSelected = selectedTree != null && selectedAnchor != null && tree != selectedTree;
        boolean otherTreeHovered = hoveredTree != null && hoveredAnchor != null && tree != hoveredTree;

        if (isParentAnchor || anchoring && hovered && otherTreeSelected) {
            return hovered || selected ? HandleType.ANCHOR_SELECTED : HandleType.ANCHOR;
        } else if (isChildAnchor || anchoring && selected && otherTreeHovered) {
            return hovered || selected ? HandleType.LINK_SELECTED : HandleType.LINK;
        } else {
            return hovered || selected ? HandleType.SELECTED : HandleType.NORMAL;
        }
    }

    private enum HandleType {
        NORMAL(new Rectangle(14, 0, 4, 4)),
        SELECTED(new Rectangle(14, 4, 4, 4)),
        ANCHOR(new Rectangle(0, 0, 7, 7)),
        ANCHOR_SELECTED(new Rectangle(0, 7, 7, 7)),
        LINK(new Rectangle(7, 0, 7, 7)),
        LINK_SELECTED(new Rectangle(7, 7, 7, 7));

        private final Rectangle texture;

        HandleType(Rectangle texture) {
            this.texture = texture;
        }

        public Rectangle getTexture() {
            return texture;
        }
    }

    private void drawDashedRectangle(DrawingContext context, Rectangle rectangle, Color color) {
        // Inset rectangle by 0.5 to account for half line outside
        rectangle = new Rectangle(rectangle.getX() + 0.5, rectangle.getY() + 0.5,
                rectangle.getWidth() - 1.0, rectangle.getHeight() - 1.0);

        beginDashed();
        context.drawBorderRectangle(rectangle, color);
        endDashed();
    }

    private void drawDashedLine(DrawingContext context, Point pointA, Point pointB, Color color) {
        beginDashed();
        context.drawLine(pointA, pointB, color);
        endDashed();
    }

    private void beginDashed() {
        int scale = (int)client.getWindow().getScaleFactor();
        int dashOffset = (int)(System.currentTimeMillis() % 2000) * 16 / 2000;

        GL11.glLineStipple(scale, Bitwise.rotateRight((short)0xf0f0, dashOffset));
        GL11.glLineWidth(scale);
        GL11.glEnable(GL11.GL_LINE_STIPPLE);
    }

    private void endDashed() {
        GL11.glDisable(GL11.GL_LINE_STIPPLE);
        GL11.glLineWidth(1.0f);
    }
}
