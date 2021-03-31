package mccreery.betterhud.internal.element.text;

import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.property.BooleanProperty;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Collections;
import java.util.List;

public class FullInvIndicator extends TextElement {
    private final BooleanProperty offHand;

    public FullInvIndicator() {
        offHand = new BooleanProperty("offhand", false);
        addProperty(offHand);
    }

    @Override
    protected List<Text> getText(HudRenderContext context) {
        PlayerEntity player = context.getClient().player;

        if (player.inventory.getEmptySlot() == -1 &&
                (!offHand.get() || !player.getEquippedStack(EquipmentSlot.OFFHAND).isEmpty())) {
            return Collections.singletonList(new TranslatableText("betterhud.inventoryFull"));
        } else {
            return Collections.emptyList();
        }
    }
}
