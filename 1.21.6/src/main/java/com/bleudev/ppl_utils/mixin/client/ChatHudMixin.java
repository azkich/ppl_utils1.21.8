package com.bleudev.ppl_utils.mixin.client;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.ppl_utils.ClientCallbacks.shouldRenderChatMessage;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Inject(method = "addVisibleMessage", at = @At("HEAD"), cancellable = true)
    private void cancelRenderingOfSomeMessages(ChatHudLine message, CallbackInfo ci) {
        if (!shouldRenderChatMessage(message)) ci.cancel();
    }
    
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    private void cancelRenderingOfMessages(Text message, CallbackInfo ci) {
        // Check if message should be filtered using ChatFilter directly
        if (!com.bleudev.ppl_utils.feature.chatfilter.ChatFilter.shouldDisplayMessage(message)) {
            ci.cancel();
        }
    }
}
