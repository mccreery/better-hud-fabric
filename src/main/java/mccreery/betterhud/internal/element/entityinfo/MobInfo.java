package mccreery.betterhud.internal.element.entityinfo;

import static jobicade.betterhud.BetterHud.MANAGER;
import static jobicade.betterhud.BetterHud.MC;
import static jobicade.betterhud.BetterHud.SPACER;

import com.mojang.realmsclient.gui.ChatFormatting;

import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.property.DoubleProperty;
import jobicade.betterhud.events.BillboardContext;
import jobicade.betterhud.geom.Direction;
import jobicade.betterhud.render.Color;
import jobicade.betterhud.util.GlUtil;
import jobicade.betterhud.util.bars.StatBarHealth;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class MobInfo extends BillboardElement {
    private final StatBarHealth bar = new StatBarHealth();
    private DoubleProperty compress;

    public MobInfo() {
        super("mobInfo");

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
        addSetting(compress);
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