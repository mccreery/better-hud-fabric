package mccreery.betterhud.internal.layout;

import com.mojang.blaze3d.systems.RenderSystem;
import mccreery.betterhud.api.geometry.Anchor;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.internal.BetterHud;
import mccreery.betterhud.internal.Bitwise;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
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
            HandleType handleType = getHandleType(hoveredTree, hoveredAnchor);

            if (handleType == HandleType.ANCHOR) {
                setParentAndAnchor();
            } else {
                Point cursor = new Point((int) mouseX, (int) mouseY);
                selectTree(cursor);

                if (handleType == HandleType.SELECTED_HANDLE) {
                    selectedAnchor = hoveredAnchor;
                }
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

        RelativePosition position = selectedTree.getPosition();
        position.setParentAnchor(hoveredAnchor);
        position.setAnchor(selectedAnchor);

        Rectangle bounds = layout.getBoundsLastFrame().get(selectedTree.getElement());
        Point anchorPoint = Anchor.getAnchorPoint(bounds, selectedAnchor);
        Rectangle parentBounds = layout.getBoundsLastFrame().get(hoveredTree.getElement());
        Point parentAnchorPoint = Anchor.getAnchorPoint(parentBounds, hoveredAnchor);

        position.setOffset(anchorPoint.subtract(parentAnchorPoint));
    }

    /**
     * Selects the tree and anchor under the cursor and prepares for dragging.
     */
    private void selectTree(Point cursor) {
        selectedTree = hoveredTree;

        // Keep relative position of cursor on element
        Rectangle bounds = layout.getBoundsLastFrame().get(selectedTree.getElement());
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
            Rectangle bounds = layout.getBoundsLastFrame().get(selectedTree.getElement());

            Point desiredPosition = cursor.subtract(selectionOffset);
            Point desiredAnchorPosition = desiredPosition.add(Anchor.getAnchorPoint(bounds.getSize(), position.getAnchor()));
            Point parentAnchorPoint = Anchor.getAnchorPoint(getParentBounds(selectedTree), position.getParentAnchor());

            position.setOffset(desiredAnchorPosition.subtract(parentAnchorPoint));
        }
    }

    private Rectangle getParentBounds(HudElementTree tree) {
        if (tree.getParent() != null) {
            return layout.getBoundsLastFrame().get(tree.getParent().getElement());
        } else {
            return new Rectangle(0, 0, width, height);
        }
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
    private int distanceSquaredToCursor;

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
        Rectangle bounds = layout.getBoundsLastFrame().get(tree.getElement());

        // Look for an anchor in range and closer than any previous
        for (Anchor anchor : Anchor.values()) {
            Point anchorPoint = Anchor.getAnchorPoint(bounds, anchor);
            int distanceSquared = anchorPoint.distanceSquared(cursor);

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

    private static final Identifier LAYOUT_WIDGETS = new Identifier(BetterHud.ID, "textures/layout_widgets.png");

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderPrompt(matrices);

        if (selectedTree != null) {
            renderSelectionBox(matrices, selectedTree);
        }
        if (hoveredTree != null && hoveredTree != selectedTree) {
            renderSelectionBox(matrices, hoveredTree);
        }

        client.getTextureManager().bindTexture(LAYOUT_WIDGETS);

        // Draw handles on all trees
        for (HudElementTree root : layout.getRoots()) {
            for (HudElementTree tree : root.breadthFirst()) {
                renderHandles(matrices, tree);
            }
        }
    }

    private void renderPrompt(MatrixStack matrices) {
        RenderSystem.enableBlend();

        Point screenSize = new Point(width, height);
        Point screenCenter = Anchor.getAnchorPoint(screenSize, Anchor.CENTER);
        Rectangle dialogBounds = Anchor.getAlignedRectangle(screenCenter, Anchor.CENTER, new Point(200, textRenderer.fontHeight * 2 + 6));

        fill(matrices, dialogBounds.getX(), dialogBounds.getY(), dialogBounds.getMaxX(), dialogBounds.getMaxY(), 0x3f000000);

        Text leftText = new TranslatableText("hudLayout.prompt.left", new TranslatableText("key.mouse.left"));
        Text rightText = new TranslatableText("hudLayout.prompt.right", new TranslatableText("key.mouse.right"));

        Point anchorPoint = Anchor.getAnchorPoint(dialogBounds, Anchor.TOP_CENTER);

        drawCenteredText(matrices, textRenderer, leftText, anchorPoint.getX(), anchorPoint.getY() + 2, 0xffffffff);
        drawCenteredText(matrices, textRenderer, rightText, anchorPoint.getX(), anchorPoint.getY() + textRenderer.fontHeight + 4, 0xffffffff);
        RenderSystem.enableBlend();
    }

    private void renderSelectionBox(MatrixStack matrices, HudElementTree tree) {
        Rectangle bounds = layout.getBoundsLastFrame().get(tree.getElement());
        drawDashedRectangle(matrices.peek().getModel(), bounds, 0x7f0071bc);
    }

    private void renderHandles(MatrixStack matrices, HudElementTree tree) {
        Rectangle bounds = layout.getBoundsLastFrame().get(tree.getElement());

        for (Anchor anchor : Anchor.values()) {
            Point anchorPoint = Anchor.getAnchorPoint(bounds, anchor);

            switch (getHandleType(tree, anchor)) {
                case HANDLE:
                    drawTexture(matrices, anchorPoint.getX() - 2, anchorPoint.getY() - 2, 7, 0, 4, 4, 16, 16);
                    break;
                case SELECTED_HANDLE:
                    drawTexture(matrices, anchorPoint.getX() - 2, anchorPoint.getY() - 2, 7, 4, 4, 4, 16, 16);
                    break;
                case ANCHOR:
                    drawTexture(matrices, anchorPoint.getX() - 3, anchorPoint.getY() - 3, 0, 0, 7, 7, 16, 16);
                    break;
            }
        }
    }

    /**
     * Determines the click action and icon corresponding to a handle.
     */
    private HandleType getHandleType(HudElementTree tree, Anchor anchor) {
        boolean hovered = tree == hoveredTree && anchor == hoveredAnchor;
        boolean treeSelected = tree == selectedTree;
        boolean selected = treeSelected && anchor == selectedAnchor;
        boolean anchoring = selectedAnchor != null && hasShiftDown();

        if (hovered && !treeSelected && anchoring) {
            return HandleType.ANCHOR;
        } else if (selected || hovered && !anchoring) {
            return HandleType.SELECTED_HANDLE;
        } else {
            return HandleType.HANDLE;
        }
    }

    private enum HandleType {
        HANDLE,
        SELECTED_HANDLE,
        ANCHOR
    }

    private void drawDashedRectangle(Matrix4f matrix, Rectangle rectangle, int color) {
        // UV moves left with time = texture appears to move right (clockwise)
        int scale = (int)client.getWindow().getScaleFactor();
        int textureOffset = (int)(System.currentTimeMillis() % 2000) * 16 / 2000;

        GL11.glLineStipple(scale, Bitwise.rotateRight((short)0xf0f0, textureOffset));

        GL11.glEnable(GL11.GL_LINE_STIPPLE);
        drawRectangle(matrix, rectangle, color, scale, -0.5f);
        GL11.glDisable(GL11.GL_LINE_STIPPLE);
    }

    /**
     * Draws a rectangular border with the specified color. Textures are temporarily disabled and enabled again before
     * returning. Line width is restored to 1.0.
     */
    private static void drawRectangle(Matrix4f matrix, Rectangle rectangle, int color, float lineWidth, float offset) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();

        int a = color >>> 24;
        int r = color >>> 16 & 0xff;
        int g = color >>> 8 & 0xff;
        int b = color & 0xff;

        bufferBuilder.begin(GL11.GL_LINE_LOOP, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, rectangle.getX() - offset, rectangle.getMaxY() + offset, 0.0f).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, rectangle.getMaxX() + offset, rectangle.getMaxY() + offset, 0.0f).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, rectangle.getMaxX() + offset, rectangle.getY() - offset, 0.0f).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, rectangle.getX() - offset, rectangle.getY() - offset, 0.0f).color(r, g, b, a).next();
        bufferBuilder.end();

        RenderSystem.lineWidth(lineWidth);
        RenderSystem.disableTexture();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.enableTexture();
        RenderSystem.lineWidth(1.0f);
    }
}
