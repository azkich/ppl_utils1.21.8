package com.bleudev.ppl_utils.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ChatHud.class)
public abstract class ChatHudMixin {
    //    @Unique
//    private List<ChatHudLine.Visible> getVisibleMessages(List<ChatHudLine.Visible> old) {
//        return List.of();
//        return old.stream().filter(it -> {
//            var content = it.content().toString();
//            System.out.println(content);
//
//            return !content.startsWith("[+]");
//        }).toList();
//    }

//    @ModifyReceiver(method = "forEachVisibleLine", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"))
//    private List<ChatHudLine.Visible> changeVisibleMessagesSize(List<ChatHudLine.Visible> instance) {
//        return getVisibleMessages(instance);
//    }
//    @ModifyReceiver(method = "forEachVisibleLine", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
//    private List<ChatHudLine.Visible> changeVisibleMessagesGet(List<ChatHudLine.Visible> instance, int i) {
//        return getVisibleMessages(instance);
//    }

    @Shadow
    public abstract void addMessage(Text message);

    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(method = "addVisibleMessage", at = @At("HEAD"), cancellable = true)
    private void addVisibleMessage(ChatHudLine message, CallbackInfo ci) {
        var mes = message.content().getString();
        final var i = mes.indexOf(">");
        if (i != -1)
            mes = mes.substring(i+2);

        var client = MinecraftClient.getInstance();
        if (client != null && client.player != null)
            client.player.sendMessage(Text.of(mes), true);

        if (mes.startsWith("[+]")) ci.cancel();
    }
}
