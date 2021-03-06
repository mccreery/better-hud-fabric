package mccreery.betterhud.internal;

import mccreery.betterhud.internal.layout.LayoutScreen;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TranslatableText;
import org.apache.logging.log4j.LogManager;

import java.util.List;

public class LayoutButtonHandler implements ScreenEvents.AfterInit {
    @Override
    public void afterInit(MinecraftClient client, Screen screen, int scaledWidth, int scaledHeight) {
        // Only apply to "Options"
        if (!(screen instanceof OptionsScreen)) {
            return;
        }

        List<AbstractButtonWidget> buttons = Screens.getButtons(screen);

        // Searching for left/right column buttons
        int leftExpectedX = scaledWidth / 2 - 155;
        int rightExpectedX = scaledWidth / 2 + 5;

        // Find lowest rightmost button
        AbstractButtonWidget lastButton = null;

        for (AbstractButtonWidget button : buttons) {
            if (button.getWidth() == 150 && button.getHeight() == 20 &&
                    (button.x == leftExpectedX || button.x == rightExpectedX) &&
                    (lastButton == null || button.y > lastButton.y || button.x > lastButton.x)) {
                lastButton = button;
            }
        }

        // Choose position for new button
        int x;
        int y;
        if (lastButton != null) {
            if (lastButton.x == leftExpectedX) {
                x = rightExpectedX;
                y = lastButton.y;
            } else {
                x = leftExpectedX;
                y = lastButton.y + 24;

                // Move done button down since a new row was created
                buttons.stream().filter(button -> button.getMessage() == ScreenTexts.DONE)
                        .findAny().ifPresent(button -> button.y += 24);
            }
        } else {
            LogManager.getLogger(BetterHud.ID).warn("Unable to place HUD Layout button. " +
                    "There may be another mod overhauling the menu. Placing in the bottom left corner");
            x = 5;
            y = scaledHeight - 25;
        }

        // Finally add button
        buttons.add(new ButtonWidget(x, y, 150, 20, new TranslatableText("options.hudLayout"),
                button -> client.openScreen(new LayoutScreen())));
    }
}
