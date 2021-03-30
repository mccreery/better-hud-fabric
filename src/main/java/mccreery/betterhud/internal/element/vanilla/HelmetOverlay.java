package mccreery.betterhud.internal.element.vanilla;

import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.internal.render.Color;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;

import java.lang.annotation.ElementType;

public class HelmetOverlay extends OverlayElement {
    private static final ResourceLocation PUMPKIN_BLUR_TEX_PATH = new ResourceLocation("textures/misc/pumpkinblur.png");

    public HelmetOverlay() {
        super("helmetOverlay");
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        return ForgeIngameGui.renderHelmet
            && !OverlayHook.pre(context.getEvent(), ElementType.HELMET)
            && MC.gameSettings.thirdPersonView == 0
            && !MC.player.inventory.armorItemInSlot(3).isEmpty();
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        ItemStack stack = MC.player.inventory.armorItemInSlot(3);

        if (MC.gameSettings.thirdPersonView == 0 && !stack.isEmpty()) {
            if (stack.getItem() == Blocks.CARVED_PUMPKIN.asItem()) {
                MC.getTextureManager().bindTexture(PUMPKIN_BLUR_TEX_PATH);
                GlUtil.drawRect(MANAGER.getScreen(), new Rectangle(256, 256), Color.RED);
                MC.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
            } else {
                stack.getItem().renderHelmetOverlay(stack, MC.player, MC.getMainWindow().getScaledWidth(), MC.getMainWindow().getScaledHeight(), context.getPartialTicks());
            }
        }

        OverlayHook.post(context.getEvent(), ElementType.HELMET);
        return MANAGER.getScreen();
    }
}
