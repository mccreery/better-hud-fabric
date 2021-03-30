package mccreery.betterhud.internal.element.entityinfo;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.property.DoubleProperty;
import mccreery.betterhud.internal.render.Color;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public class MobInfo extends HudElement {
    private final StatBarHealth bar = new StatBarHealth();
    private final DoubleProperty compress;

    public MobInfo() {
        setRenderPhase(HudRenderContext.Phase.LIVING_ENTITY_BILLBOARD);

        compress = new DoubleProperty("compress", 0, 200) {
            @Override
            public String getDisplayValue(double value) {
                if(value == 0) {
                    return I18n.format("betterHud.value.never");
                } else {
                    return super.getDisplayValue(value);
                }
            }
        };
        addProperty(compress);
        compress.setInterval(20);
    }

    @Override
    public Rectangle render(BillboardContext context) {
        LivingEntity entity = context.getPointedEntity();
        bar.setHost(entity);
        bar.setCompressThreshold((int)compress.getValue());

        int health = MathHelper.ceil(entity.getHealth());
        int maxHealth = MathHelper.ceil(entity.getMaxHealth());

        String text = String.format("%s %s(%d/%d)", entity.getName(), ChatFormatting.GRAY, health, maxHealth);

        Point size = GlUtil.getStringSize(text);
        Point barSize = bar.getPreferredSize();

        if(barSize.getX() > size.getX()) {
            size = new Point(barSize.getX(), size.getY() + barSize.getY());
        } else {
            size = size.add(0, barSize.getY());
        }

        Rectangle bounds = MANAGER.position(Direction.SOUTH, new Rectangle(size).grow(SPACER));
        GlUtil.drawRect(bounds, Color.TRANSLUCENT);
        bounds = bounds.grow(-SPACER);

        GlUtil.drawString(text, bounds.getPosition(), Direction.NORTH_WEST, Color.WHITE);
        Rectangle barRect = new Rectangle(barSize).anchor(bounds, Direction.SOUTH_WEST);

        MC.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
        bar.setBounds(barRect).render();
        return null;
    }
}
