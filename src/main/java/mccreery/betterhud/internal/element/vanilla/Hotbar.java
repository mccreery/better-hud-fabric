package mccreery.betterhud.internal.element.vanilla;

import static jobicade.betterhud.BetterHud.MC;

import com.mojang.blaze3d.systems.RenderSystem;

import jobicade.betterhud.element.OverlayElement;
import jobicade.betterhud.element.settings.DirectionOptions;
import jobicade.betterhud.element.settings.SettingPosition;
import mccreery.betterhud.api.HudRenderContext;
import jobicade.betterhud.events.OverlayHook;
import jobicade.betterhud.geom.Direction;
import mccreery.betterhud.api.geometry.Rectangle;
import jobicade.betterhud.util.GlUtil;
import jobicade.betterhud.util.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.SpectatorGui;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class Hotbar extends OverlayElement {
    private SettingPosition position;

    public Hotbar() {
        super("hotbar");

        position = new SettingPosition("position");
        position.setDirectionOptions(DirectionOptions.TOP_BOTTOM);
        position.setContentOptions(DirectionOptions.NONE);
        position.setEdge(true);
        position.setPostSpacer(2);
        addSetting(position);
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        final Minecraft mc = MC;

        return ForgeIngameGui.renderHotbar
            && !OverlayHook.pre(context.getEvent(), ElementType.HOTBAR)
            && (
                !mc.playerController.isSpectatorMode()
                || mc.ingameGUI.getSpectatorGui().isMenuActive()
            );
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        final Minecraft mc = MC;

        if (MC.playerController.isSpectatorMode()) {
            SpectatorGui spectator = mc.ingameGUI.getSpectatorGui();

            spectator.renderTooltip(context.getPartialTicks());
            spectator.renderSelectedItem();

            return new Rectangle(182, 22).anchor(context.getLayoutManager().getScreen(), Direction.SOUTH);
        } else {
            return renderHotbar(context, mc);
        }
    }

    private Rectangle renderHotbar(HudRenderContext context, Minecraft mc) {
        Rectangle barTexture = new Rectangle(182, 22);
        Rectangle bounds = position.applyTo(new Rectangle(barTexture));

        MC.getTextureManager().bindTexture(Textures.WIDGETS);
        GlUtil.drawRect(bounds, barTexture);

        Rectangle slot = bounds.grow(-3).withWidth(16);

        float partialTicks = context.getPartialTicks();
        for(int i = 0; i < 9; i++, slot = slot.translate(Direction.EAST.scale(20))) {
            if(i == MC.player.inventory.currentItem) {
                MC.getTextureManager().bindTexture(Textures.WIDGETS);
                GlUtil.drawRect(slot.grow(4), new Rectangle(0, 22, 24, 24));
            }

            GlUtil.renderHotbarItem(slot, MC.player.inventory.mainInventory.get(i), partialTicks);
            RenderSystem.enableBlend();
        }

        MC.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
        OverlayHook.post(context.getEvent(), ElementType.HOTBAR);
        return bounds;
    }
}
