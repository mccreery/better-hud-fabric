package mccreery.betterhud.api.property;

import jobicade.betterhud.geom.Direction;
import net.minecraft.client.resources.I18n;

public class SettingWarnings extends ParentSetting {
    private final DoubleProperty[] sliders;

    public SettingWarnings(String name, int warnings) {
        super(name);

        addChild(new Legend("damageWarning"));
        sliders = new DoubleProperty[warnings];
        populateSliders();
    }

    private void populateSliders() {
        for(int i = 0; i < sliders.length; i++) {
            DoubleProperty slider = new WarningSlider(i);

            if ((i & 1) == 1) {
                slider.setAlignment(Direction.EAST);
            } else if (i == sliders.length - 1) {
                slider.setAlignment(Direction.CENTER);
            } else {
                slider.setAlignment(Direction.WEST);
            }
            sliders[i] = slider;
            addChild(slider);
        }
    }

    public float[] get() {
        float[] values = new float[sliders.length];

        for(int i = 0; i < sliders.length; i++) {
            values[i] = sliders[i].getValue();
        }
        return values;
    }

    public void set(float... values) {
        for(int i = 0; i < sliders.length; i++) {
            if(values[i] >= 0) {
                sliders[i].setValue(values[i]);
            }
        }
    }

    public int getWarning(float value) {
        for(int i = sliders.length - 1; i >= 0; i--) {
            if(value <= sliders[i].getValue()) return i + 1;
        }
        return 0;
    }

    private final class WarningSlider extends DoubleProperty {
        private final int index;

        private WarningSlider(int index) {
            super("warning." + (index + 1), 0, 1);
            this.index = index;

            setDisplayPercent();
        }

        @Override
        public String getDisplayValue(double value) {
            DoubleProperty next = next();

            if(next == null || next.getValue() < getValue()) {
                return super.getDisplayValue(value);
            } else {
                return I18n.format("betterHud.value.disabled");
            }
        }

        @Override
        public void setValue(float value) {
            DoubleProperty next = next();
            super.setValue(next != null ? Math.max(value, next.getValue()) : value);

            for(int i = index - 1; i >= 0; i--) {
                DoubleProperty slider = sliders[i];
                if(slider != null) slider.setValue(Math.max(slider.getValue(), getValue()));
            }
        }

        private DoubleProperty next() {
            return index == sliders.length - 1 ? null : sliders[index + 1];
        }

        @Override
        public void updateGuiParts() {
            //guiSlider.updateDisplayString(); // TODO
        }
    }
}
