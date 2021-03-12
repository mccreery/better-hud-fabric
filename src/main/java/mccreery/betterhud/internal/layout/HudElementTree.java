package mccreery.betterhud.internal.layout;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.internal.tree.DefaultTreeIterator;
import mccreery.betterhud.internal.tree.Tree;
import mccreery.betterhud.internal.tree.TreeIterator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HudElementTree implements Tree<HudElementTree> {
    protected HudElementTree(HudElement element, boolean fixed) {
        this.element = element;
        this.fixed = fixed;
    }

    private final HudElement element;

    public HudElement getElement() {
        return element;
    }

    private transient Rectangle boundsLastFrame;

    public Rectangle getBoundsLastFrame() {
        return boundsLastFrame;
    }

    public void setBoundsLastFrame(Rectangle boundsLastFrame) {
        this.boundsLastFrame = boundsLastFrame;
    }

    protected final Set<HudElementTree> children = new HashSet<>();

    @Override
    public Set<HudElementTree> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    private final transient boolean fixed;

    public boolean isFixed() {
        return fixed;
    }

    /**
     * @throws UnsupportedOperationException if the tree is fixed
     * @see #isFixed()
     */
    public HudElementTree getParent() {
        throw new UnsupportedOperationException("Element is fixed");
    }

    /**
     * Reparents the tree or removes its parent. This method ensures that this tree is exclusively present in its new
     * parent's child collection. It has no effect if the argument is equal to the current parent.
     *
     * @param parent {@code null} to remove this tree's parent.
     * @throws UnsupportedOperationException if the tree is fixed
     * @see #getChildren()
     * @see #isFixed()
     */
    public void setParent(HudElementTree parent) {
        throw new UnsupportedOperationException("Element is fixed");
    }

    /**
     * @throws UnsupportedOperationException if the tree is fixed
     * @see #isFixed()
     */
    public RelativePosition getPosition() {
        throw new UnsupportedOperationException("Element is fixed");
    }

    /**
     * @throws UnsupportedOperationException if the tree is fixed
     * @see #isFixed()
     */
    public void setPosition(RelativePosition position) {
        throw new UnsupportedOperationException("Element is fixed");
    }

    public static HudElementTree create(HudElement element) {
        if (element.isFixed()) {
            return new HudElementTree(element, true);
        } else {
            return new RelativeHudElementTree(element);
        }
    }

    @Override
    public TreeIterator<HudElementTree> iterator() {
        return new DefaultTreeIterator<>(this);
    }
}
