package mccreery.betterhud.internal.element;

import mccreery.betterhud.api.property.BooleanProperty;
import mccreery.betterhud.api.property.DoubleProperty;
import mccreery.betterhud.api.geometry.Rectangle;

public class GlobalSettings extends HudElement<Object> {
    private DoubleProperty billboardScale;
    private DoubleProperty billboardDistance;
    private BooleanProperty hideOnDebug;
    private BooleanProperty debugMode;

    public GlobalSettings() {
        super("global");

        billboardScale = new DoubleProperty("billboardScale", 0, 1);
        billboardScale.setDisplayPercent();
        addSetting(billboardScale);

        billboardDistance = new DoubleProperty("rayDistance", 5, 200);
        billboardDistance.setUnlocalizedValue("betterHud.hud.meters");
        addSetting(billboardDistance);

        hideOnDebug = new BooleanProperty("hideOnDebug");
        addSetting(hideOnDebug);

        debugMode = new BooleanProperty("debugMode");
        addSetting(debugMode);
    }

    public float getBillboardScale() {
        return billboardScale.getValue();
    }

    public float getBillboardDistance() {
        return billboardDistance.getValue();
    }

    public boolean hideOnDebug() {
        return hideOnDebug.get();
    }

    public boolean isDebugMode() {
        return debugMode.get();
    }

    @Override
    public Rectangle render(Object context) {
        return null;
    }
}
