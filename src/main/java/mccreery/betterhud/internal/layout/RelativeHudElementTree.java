package mccreery.betterhud.internal.layout;

import mccreery.betterhud.api.HudElement;

public class RelativeHudElementTree extends HudElementTree {
    protected RelativeHudElementTree(HudElement element) {
        super(element, false);
    }

    private transient HudElementTree parent;

    @Override
    public HudElementTree getParent() {
        return parent;
    }

    @Override
    public void setParent(HudElementTree parent) {
        // Prevent remove-add if no change is needed
        if (parent == this.parent) {
            return;
        }
        if (this.parent != null) {
            this.parent.children.remove(this);
        }
        if (parent != null) {
            parent.children.add(this);
        }
        this.parent = parent;
    }

    private RelativePosition position = new RelativePosition();

    @Override
    public RelativePosition getPosition() {
        return position;
    }

    @Override
    public void setPosition(RelativePosition position) {
        this.position = position;
    }
}
