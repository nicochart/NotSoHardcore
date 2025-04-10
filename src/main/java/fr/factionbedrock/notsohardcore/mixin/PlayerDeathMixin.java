package fr.factionbedrock.notsohardcore.mixin;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class PlayerDeathMixin
{
    @Inject(at = @At("HEAD"), method = "onDeath")
    private void applyOnDeath(DamageSource damageSource, CallbackInfo info)
    {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        int previousLives = player.getDataTracker().get(NSHTrackedData.LIVES);
        if (previousLives > 0 && !player.isSpectator() && !(player.isCreative() && NotSoHardcore.CREATIVE_RESETS_LIFE_COUNT))
        {
            if (previousLives == NotSoHardcore.MAX_LIVES)
            {
                player.getDataTracker().set(NSHTrackedData.LIFE_REGAIN_TIME_MARKER, player.getServerWorld().getTime());
            }
            player.getDataTracker().set(NSHTrackedData.LIVES, previousLives - 1);
        }
    }

    @Inject(at = @At("HEAD"), method = "copyFrom")
    private void applyOnCopyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info)
    {
        ServerPlayerEntity newPlayer = (ServerPlayerEntity) (Object) this;

        int lives = oldPlayer.getDataTracker().get(NSHTrackedData.LIVES);
        newPlayer.getDataTracker().set(NSHTrackedData.LIVES, lives);
        long live_regain_timer = oldPlayer.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_TIME_MARKER);
        newPlayer.getDataTracker().set(NSHTrackedData.LIFE_REGAIN_TIME_MARKER, live_regain_timer);
    }
}