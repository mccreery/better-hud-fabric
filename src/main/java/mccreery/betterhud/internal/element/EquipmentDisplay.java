package mccreery.betterhud.internal.element;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.property.BooleanProperty;
import mccreery.betterhud.api.property.EnumProperty;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public abstract class EquipmentDisplay extends HudElement {
    private final BooleanProperty showName;
    private final BooleanProperty showDurability;
    private final EnumProperty<DurabilityMode> durabilityMode;
    private final BooleanProperty showUndamaged;

    public EquipmentDisplay() {
        showName = new BooleanProperty("showName", true);
        addProperty(showName);

        showDurability = new BooleanProperty("showDurability", true);
        addProperty(showDurability);

        durabilityMode = new EnumProperty<>("durabilityFormat", DurabilityMode.POINTS);
        durabilityMode.setEnableCondition(showDurability::get);
        addProperty(durabilityMode);

        showUndamaged = new BooleanProperty("showUndamaged", true);
        showUndamaged.setEnableCondition(showDurability::get);
        addProperty(showUndamaged);
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

    public enum DurabilityMode {
        POINTS,
        PERCENTAGE
    }
}
