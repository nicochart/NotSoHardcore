package fr.factionbedrock.notsohardcore.mixin;

import fr.factionbedrock.notsohardcore.config.LoadedConfig;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class PlayerDeathMixin
{
    @Inject(at = @At("HEAD"), method = "die")
    private void applyOnDeath(DamageSource damageSource, CallbackInfo info)
    {
        ServerPlayer player = (ServerPlayer) (Object) this;

        int previousLives = player.getEntityData().get(NSHTrackedData.LIVES);
        if (previousLives > 0 && !player.isSpectator() && !(player.isCreative() && LoadedConfig.Server.CREATIVE_RESETS_LIFE_COUNT))
        {
            if (previousLives == LoadedConfig.Server.MAX_LIVES)
            {
                player.getEntityData().set(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER, System.currentTimeMillis());
                player.getEntityData().set(NSHTrackedData.LIFE_REGAIN_TICK_MARKER, player.level().getGameTime());
            }
            player.getEntityData().set(NSHTrackedData.LIVES, previousLives - 1);
        }
    }

    @Inject(at = @At("HEAD"), method = "restoreFrom")
    private void applyOnCopyFrom(ServerPlayer oldPlayer, boolean alive, CallbackInfo info)
    {
        ServerPlayer newPlayer = (ServerPlayer) (Object) this;

        long realtimeMarker = oldPlayer.getEntityData().get(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER);
        newPlayer.getEntityData().set(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER, realtimeMarker);

        int lives = oldPlayer.getEntityData().get(NSHTrackedData.LIVES);
        newPlayer.getEntityData().set(NSHTrackedData.LIVES, lives);
        long live_regain_timer = oldPlayer.getEntityData().get(NSHTrackedData.LIFE_REGAIN_TICK_MARKER);
        newPlayer.getEntityData().set(NSHTrackedData.LIFE_REGAIN_TICK_MARKER, live_regain_timer);
    }

}
