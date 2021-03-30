package mccreery.betterhud.internal.element;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.property.BooleanProperty;
import mccreery.betterhud.internal.render.Color;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class ExperienceInfo extends HudElement {
    private final BooleanProperty total;
    private final BooleanProperty lifetime;

    public ExperienceInfo() {
        total = new BooleanProperty("showTotalExp", false);
        addProperty(total);

        lifetime = new BooleanProperty("showLifetimeExp", false);
        addProperty(lifetime);
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        return MC.playerController.gameIsSurvivalOrAdventure();
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        int fullBar = getExperienceWithinLevel(MC.player.experienceLevel);

        int has = (int)(MC.player.experience * fullBar);
        int needed = fullBar - has;

        String hasDisplay = String.valueOf(has);
        String neededDisplay = String.valueOf(needed);

        Rectangle bar = OverlayElements.EXPERIENCE.getLastBounds();

        Point text = new Rectangle(GlUtil.getStringSize(hasDisplay).sub(0, 2)).anchor(bar, Direction.WEST).getPosition();
        GlUtil.drawBorderedString(hasDisplay, text.getX(), text.getY(), Color.WHITE);

        text = new Rectangle(GlUtil.getStringSize(neededDisplay).sub(0, 2)).anchor(bar, Direction.EAST).getPosition();
        GlUtil.drawBorderedString(neededDisplay, text.getX(), text.getY(), Color.WHITE);

        return super.render(context);
    }

    /** @param level The player's current level
     * @return The total amount of experience in the current experience bar
     * @see <a href="https://minecraft.gamepedia.com/Experience#Leveling_up">Levelling Up</a> */
    private static int getExperienceWithinLevel(int level) {
        if (level >= 31) {
            return 9 * level - 158;
        } else if (level >= 16) {
            return 5 * level - 38;
        } else {
            return 2 * level + 7;
        }
    }

    /** @return The total amount of experience required to reach {@code level}
     * @see <a href="https://minecraft.gamepedia.com/Experience#Leveling_up">Levelling Up</a> */
    private static int getExperienceToLevel(int level) {
        /* Result is always integer despite real coefficients
         * because level and level^2 are either both odd or both even */

        if(level >= 32) {
            return (int)((4.5 * level - 162.5) * level) + 2220;
        } else if(level >= 17) {
            return (int)((2.5 * level - 40.5) * level) + 360;
        } else {
            return (level + 6) * level;
        }
    }

    @Override
    protected List<String> getText() {
        List<String> parts = new ArrayList<String>(2);

        if(total.get()) {
            int totalDisplay = getExperienceToLevel(MC.player.experienceLevel);
            totalDisplay += MC.player.experience * getExperienceWithinLevel(MC.player.experienceLevel);

            parts.add(total.getLocalizedName() + ": "+ totalDisplay);
        }
        if(lifetime.get()) {
            parts.add(lifetime.getLocalizedName() + ": " + MC.player.experienceTotal);
        }
        return parts;
    }
}
