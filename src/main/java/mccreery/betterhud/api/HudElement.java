package mccreery.betterhud.api;

import com.mojang.serialization.Lifecycle;
import mccreery.betterhud.BetterHud;
import mccreery.betterhud.api.layout.RelativePosition;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class HudElement {
    // Cannot use FabricRegistryBuilder because it doesn't support generics
    public static final Registry<Class<? extends HudElement>> REGISTRY = new SimpleRegistry<>(
            RegistryKey.ofRegistry(new Identifier(BetterHud.ID, "element")),
            Lifecycle.stable());

    public MutableText getName() {
        return new TranslatableText(getTranslationKey());
    }

    private transient String translationKey;

    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.createTranslationKey("hudElement", REGISTRY.getId(getClass()));
        }

        return translationKey;
    }

    private transient HudElementCategory category = HudElementCategory.MISC;

    public final HudElementCategory getCategory() {
        return category;
    }

    public final void setCategory(HudElementCategory category) {
        this.category = category;
    }

    transient HudElement parent;

    public HudElement getParent() {
        return parent;
    }

    public void setParent(HudElement parent) {
        if (this.parent != null) {
            this.parent.children.remove(this);
        }
        if (parent != null) {
            parent.children.add(this);
        }
        this.parent = parent;
    }

    Set<HudElement> children = new HashSet<>();

    public Set<HudElement> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    private RelativePosition position = new RelativePosition();

    public RelativePosition getPosition() {
        return position;
    }

    public abstract void render(HudRenderContext context);
}
