package fr.factionbedrock.notsohardcore.mixin;

import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ClientPacketListener.class)
public abstract class ModifyDeathScreenClientNetworkArgsMixin
{
    @ModifyArgs(method = "handlePlayerCombatKill", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/DeathScreen;<init>(Lnet/minecraft/network/chat/Component;ZLnet/minecraft/client/player/LocalPlayer;)V"))
    private void modifyDeathScreenArgsInOnDeathMessage(Args args)
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null) return;
        int lives = mc.player.getEntityData().get(NSHTrackedData.LIVES);

        boolean shouldDisplayVanillaHardcoreDeathScreen = lives <= 1; //if the player dies with 1 life, he is now at 0 life and should spectate
        //Changing "isHardcore" constructor parameter
        args.set(1, shouldDisplayVanillaHardcoreDeathScreen);
    }
}
