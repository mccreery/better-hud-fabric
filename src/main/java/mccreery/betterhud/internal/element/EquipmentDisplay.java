package mccreery.betterhud.internal.element;

import java.util.ArrayList;

import mccreery.betterhud.api.property.BooleanProperty;
import mccreery.betterhud.api.property.EnumProperty;
import jobicade.betterhud.element.settings.SettingWarnings;
import jobicade.betterhud.geom.Direction;
import jobicade.betterhud.util.MathUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public abstract class EquipmentDisplay extends OverlayElement {
    private BooleanProperty showName;
    private BooleanProperty showDurability;
    private SettingWarnings warnings;
    private EnumProperty durabilityMode;
    private BooleanProperty showUndamaged;

    public EquipmentDisplay(String name) {
        super(name);

        showName = new BooleanProperty("showName");
        addSetting(showName);

        showDurability = new BooleanProperty("showDurability");
        showDurability.setAlignment(Direction.WEST);
        addSetting(showDurability);

        durabilityMode = new EnumProperty("durabilityFormat", "points", "percentage");
        durabilityMode.setAlignment(Direction.EAST);
        durabilityMode.setEnableOn(showDurability::get);
        addSetting(durabilityMode);

        showUndamaged = new BooleanProperty("showUndamaged");
        showUndamaged.setEnableOn(showDurability::get);
        showUndamaged.setValuePrefix("betterHud.value.visible");
        addSetting(showUndamaged);

        warnings = new SettingWarnings("damageWarning", 3);
        addSetting(warnings);
    }

    protected boolean hasText() {
        return showName.get() || showDurability.get();
    }

    protected boolean showDurability(ItemStack stack) {
        return showDurability.get() && (showUndamaged.get() ? stack.isDamageable() : stack.isDamaged());
    }

    protected String getText(ItemStack stack) {
        if(!hasText() || stack.isEmpty()) return null;
        ArrayList<String> parts = new ArrayList<String>();

        if(this.showName.get()) {
            parts.add(stack.getDisplayName().getFormattedText());
        }

        int maxDurability = stack.getMaxDamage();
        int durability = maxDurability - stack.getDamage();

        float value = (float)durability / (float)maxDurability;

        if(showDurability(stack)) {
            if(durabilityMode.getIndex() == 1) {
                parts.add(MathUtil.formatToPlaces(value * 100, 1) + "%");
            } else {
                parts.add(durability + "/" + maxDurability);
            }
        }

        String text = String.join(" - ", parts);

        if(stack.isDamageable()) {
            int count = warnings.getWarning(value);
            if(count > 0) text += ' ' + I18n.format("betterHud.setting.warning." + count);
        }
        return text;
    }
}
