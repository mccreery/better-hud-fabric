package mccreery.betterhud.internal.element.vanilla;

import com.mojang.blaze3d.systems.RenderSystem;
import mccreery.betterhud.api.geometry.Rectangle;

import java.lang.annotation.ElementType;

public class Chat extends OverlayElement {
    private final SettingPosition position;

    public Chat() {
        super("chat");

        position = new SettingPosition("position");
        position.setDirectionOptions(DirectionOptions.CORNERS);
        position.setContentOptions(DirectionOptions.NONE);
        addSetting(position);
    }

    private Rectangle bounds;

    @Override
    public boolean shouldRender(HudRenderContext context) {
        final int fontHeight = MC.fontRenderer.FONT_HEIGHT;
        bounds = position.applyTo(new Rectangle(getChatSize(getChatGui())));

        RenderGameOverlayEvent.Chat chatEvent = new RenderGameOverlayEvent.Chat(
            context.getEvent(),
            bounds.getX(),
            bounds.getBottom() - fontHeight
        );

        boolean canceled = MinecraftForge.EVENT_BUS.post(chatEvent);
        bounds = new Rectangle(
            chatEvent.getPosX(),
            chatEvent.getPosY() + fontHeight - bounds.getHeight(),
            bounds.getWidth(),
            bounds.getHeight()
        );
        return !canceled;
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        Minecraft mc = MC;

        RenderSystem.pushMatrix();
        RenderSystem.translatef(bounds.getX(), bounds.getBottom() - mc.fontRenderer.FONT_HEIGHT, 0);

        getChatGui().render(mc.ingameGUI.getTicks());

        RenderSystem.popMatrix();

        MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Post(context.getEvent(), ElementType.CHAT));
        return bounds;
    }

    private Size getChatSize(NewChatGui guiChat) {
        return new Size(guiChat.getChatWidth() + 6, guiChat.getChatHeight());
    }

    private NewChatGui getChatGui() {
        return MC.ingameGUI.getChatGUI();
    }
}
