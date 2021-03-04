package mccreery.betterhud.internal.schema;

import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;

/**
 * Data object describing a property attached to a {@link mccreery.betterhud.api.HudElement HudElement}. Concrete
 * subclasses provide metadata relevant to their specific property type.
 *
 * <p>This data is used by the config GUI to create controls for a property.
 *
 * @see Schema
 */
public abstract class SchemaProperty {
    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Properties are organized by these categories in the GUI.
     */
    public static final class Category {
        public static final Category MISC = new Category("misc");

        private final String translationKey;

        public Category(String name) {
            translationKey = "betterhud.category." + name;
        }

        public String getTranslationKey() {
            return translationKey;
        }

        public MutableText getName() {
            return new TranslatableText(translationKey);
        }
    }
}
