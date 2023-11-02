package org.vivecraft.mod_compat_vr.modmenu.mixin;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vivecraft.client_vr.VRState;

@Mixin(PauseScreen.class)
public abstract class PauseScreenVRModMenuMixin extends Screen {
    protected PauseScreenVRModMenuMixin(Component component) {
        super(component);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void vivecraft$reduceModmenuButtonSize(CallbackInfo ci) {
        if (VRState.vrInitialized) {
            Button modmenuButton = null;
            Button commandsButton = null;
            Button reportBugsButton = null;
            for (GuiEventListener guiEventListener : children()) {
                if (guiEventListener instanceof Button button) {
                    if (button.getMessage().getContents() instanceof TranslatableContents contents
                        && "modmenu.title".equals(contents.getKey())) {
                        modmenuButton = button;
                    } else if (button.getMessage().getContents() instanceof TranslatableContents contents
                        && "vivecraft.gui.commands".equals(contents.getKey())) {
                        commandsButton = button;
                    } else if (button.getMessage().getContents() instanceof TranslatableContents contents
                        && "menu.reportBugs".equals(contents.getKey())) {
                        reportBugsButton = button;
                    }
                }
            }

            // make sure we found the buttons, and they are actually overlapping
            if (reportBugsButton == null && modmenuButton != null && commandsButton != null
                && modmenuButton.x == commandsButton.x
                && modmenuButton.y == commandsButton.y) {
                modmenuButton.setWidth(modmenuButton.getWidth() / 2 - 1);
                commandsButton.setWidth(commandsButton.getWidth() / 2);
                modmenuButton.x = commandsButton.x + commandsButton.getWidth() + 1;
            }
        }
    }
}
