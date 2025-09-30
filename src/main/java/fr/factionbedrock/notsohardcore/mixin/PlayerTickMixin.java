package fr.factionbedrock.notsohardcore.mixin;

import fr.factionbedrock.notsohardcore.config.LoadedConfig;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import fr.factionbedrock.notsohardcore.util.NSHHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerTickMixin
{
    @Inject(at = @At("RETURN"), method = "tick")
    private void onTick(CallbackInfo info)
    {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        int lives = player.getDataTracker().get(NSHTrackedData.LIVES);
        if (lives > LoadedConfig.Server.MAX_LIVES)
        {
            player.getDataTracker().set(NSHTrackedData.LIVES, LoadedConfig.Server.MAX_LIVES);
            lives = LoadedConfig.Server.MAX_LIVES;
        }

        if (player.isCreative() && LoadedConfig.Server.CREATIVE_RESETS_LIFE_COUNT)
        {
            if (lives != LoadedConfig.Server.MAX_LIVES)
            {
                player.getDataTracker().set(NSHTrackedData.LIVES, LoadedConfig.Server.MAX_LIVES);
            }
        }
        else if (lives != LoadedConfig.Server.MAX_LIVES)
        {
            //dividing by 50 turns milliseconds into ticks
            long currentTime = NSHHelper.getCurrentTime(player, LoadedConfig.Server.USE_REALTIME_REGAIN);
            long liveRegainTimeMarker = NSHHelper.getLiveRegainTimeMarker(player, LoadedConfig.Server.USE_REALTIME_REGAIN);

            if (currentTime - liveRegainTimeMarker < LoadedConfig.Server.TIME_TO_REGAIN_LIFE)
            {
                if (lives == 0 && !player.isSpectator() && !player.isCreative())
                {
                    player.changeGameMode(GameMode.SPECTATOR);
                }
            }
            else
            {
                int livePlayerCanRegain = (int) ((currentTime - liveRegainTimeMarker) / LoadedConfig.Server.TIME_TO_REGAIN_LIFE);
                if (lives == 0)
                {
                    NSHHelper.respawnPlayer(player);
                }
                player.getDataTracker().set(NSHTrackedData.LIVES, Math.min(LoadedConfig.Server.MAX_LIVES, lives + livePlayerCanRegain));
                player.getDataTracker().set(NSHTrackedData.LIFE_REGAIN_TICK_MARKER, NSHHelper.getCurrentTime(player, false));
                player.getDataTracker().set(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER, System.currentTimeMillis());
            }
        }
    }
}
