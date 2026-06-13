package fr.factionbedrock.notsohardcore.mixin;

import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerGamePacketListenerImpl.class)
public class HardcoreRespawnRedirectMixin
{
    @Redirect(method = "handleClientCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;isHardcore()Z"))
    private boolean overrideHardcore(MinecraftServer server)
    {
        ServerPlayer player = ((ServerGamePacketListenerImpl)(Object)this).player;
        return player.getEntityData().get(NSHTrackedData.LIVES) <= 0;
    }
}
