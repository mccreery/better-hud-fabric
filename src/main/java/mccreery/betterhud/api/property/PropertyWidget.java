package mccreery.betterhud.api.property;

public class PropertyWidget {
    private final Property property;

    public PropertyWidget(Property property) {
        this.property = property;
    }

    protected boolean isEnabled() {
        return property.isEnabled();
    }
}
