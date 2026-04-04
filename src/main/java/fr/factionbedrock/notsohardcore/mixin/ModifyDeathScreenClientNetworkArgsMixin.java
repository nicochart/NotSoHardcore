package fr.factionbedrock.notsohardcore.mixin;

import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ModifyDeathScreenClientNetworkArgsMixin
{
    @ModifyArgs(method = "onDeathMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/DeathScreen;<init>(Lnet/minecraft/text/Text;Z)V"))
    private void modifyDeathScreenArgsInOnDeathMessage(Args args)
    {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.player == null) return;
        int lives = mc.player.getDataTracker().get(NSHTrackedData.LIVES);

        boolean shouldDisplayVanillaHardcoreDeathScreen = lives <= 1; //if the player dies with 1 life, he is now at 0 life and should spectate
        //Changing "isHardcore" constructor parameter
        args.set(1, shouldDisplayVanillaHardcoreDeathScreen);
    }
}
