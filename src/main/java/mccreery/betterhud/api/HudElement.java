package mccreery.betterhud.api;

import mccreery.betterhud.BetterHud;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

public abstract class HudElement {
    public static final Registry<HudElement> REGISTRY = FabricRegistryBuilder
            .createSimple(HudElement.class, new Identifier(BetterHud.ID, "element"))
            .buildAndRegister();

    public MutableText getName() {
        return new TranslatableText(getTranslationKey());
    }

    private String translationKey;

    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.createTranslationKey("hudElement", REGISTRY.getId(this));
        }

        return translationKey;
    }

    private HudElementCategory category = HudElementCategory.MISC;

    public final HudElementCategory getCategory() {
        return category;
    }

    public final void setCategory(HudElementCategory category) {
        this.category = category;
    }

    public abstract void render(HudRenderContext context);
}
