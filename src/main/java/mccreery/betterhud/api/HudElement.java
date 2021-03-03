package mccreery.betterhud.api;

import com.mojang.serialization.Lifecycle;
import mccreery.betterhud.BetterHud;
import mccreery.betterhud.api.layout.Point;
import mccreery.betterhud.api.layout.Rectangle;
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

    public HudElementCategory getCategory() {
        return category;
    }

    protected void setCategory(HudElementCategory category) {
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

    private final RelativePosition position = new RelativePosition();

    public RelativePosition getPosition() {
        if (fixedPosition) {
            throw new IllegalStateException("Element has fixed position");
        }
        return position;
    }

    private transient boolean fixedPosition;

    public boolean getFixedPosition() {
        return fixedPosition;
    }

    /**
     * Indicates that the element has a fixed position and cannot be parented or positioned relatively. Elements with
     * this property set to true do not serialize or deserialize
     */
    protected void setFixedPosition(boolean fixedPosition) {
        this.fixedPosition = fixedPosition;
    }

    private transient Rectangle bounds;

    /**
     * Bounds is {@code null} if the element renders fullscreen or otherwise cannot be parented to.
     */
    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    /**
     * Calculates bounds by applying the anchor points of the current relative position to a rectangle of the given size
     * and the parent's bounds.
     */
    protected Rectangle calculateBounds(Point size) {
        return position.apply(parent.bounds, size);
    }

    /**
     * Renders and updates bounds.
     *
     * <p>Implementers should call {@link #setBounds(Rectangle)} with the result of {@link #calculateBounds(Point)} or
     * their fixed bounds, unless the bounds do not need to be updated (for example, if constant and set in the
     * constructor).
     */
    public abstract void render(HudRenderContext context);
}
