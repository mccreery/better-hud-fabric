package mccreery.betterhud;

import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;

public final class HudElementCategory {
    public static final HudElementCategory VANILLA = new HudElementCategory("vanilla");
    public static final HudElementCategory TEXT = new HudElementCategory("text");
    public static final HudElementCategory BILLBOARD = new HudElementCategory("billboard");
    public static final HudElementCategory MISC = new HudElementCategory("misc");

    private final String translationKey;

    private HudElementCategory(String name) {
        translationKey = "hudElementCategory." + name;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public MutableText getName() {
        return new TranslatableText(translationKey);
    }
}
