package mccreery.betterhud.api.property;

import mccreery.betterhud.api.geometry.Anchor;

public class AnchorProperty extends EnumProperty<Anchor> {
    public AnchorProperty(String name, Anchor value) {
        super(name, value);
    }

    @Override
    public PropertyWidget getWidget() {
        return null; // TODO 3x3 anchor grid widget
    }
}
