package fr.factionbedrock.notsohardcore.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin {

    @Inject(method = "init", at = @At("TAIL"))

    private void nsh$addRespawnButton(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.player == null) return;

        int lives = mc.player.getDataTracker().get(NSHTrackedData.LIVES);


        boolean hardcore = mc.world.getLevelProperties().isHardcore();

        // Only add in hardcore, and only if the player still hass lives

        if (hardcore && lives > 0)
        {
            int x = mc.getWindow().getScaledWidth() / 2 - 100;
            int y = mc.getWindow().getScaledHeight() / 4 + 120;

            ButtonWidget respawn = ButtonWidget.builder(
                Text.translatable("deathScreen.respawn"),
                btn -> mc.player.requestRespawn()
            ).dimensions(x, y, 200, 20).build();

            ((Screen)(Object)this).addDrawableChild(respawn);
        }

        
    }
}
