package mccreery.betterhud;

import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

public abstract class HudElement {
    public MutableText getName() {
        return new TranslatableText(getTranslationKey());
    }

    private String translationKey;

    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.createTranslationKey("hudElement", BetterHud.REGISTRY.getId(this));
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
