package fr.factionbedrock.notsohardcore.mixin;

import fr.factionbedrock.notsohardcore.config.ServerLoadedConfig;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerTickMixin
{
    @Shadow public abstract void readCustomDataFromNbt(NbtCompound nbt);

    @Inject(at = @At("RETURN"), method = "tick")
    private void onTick(CallbackInfo info)
    {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        int lives = player.getDataTracker().get(NSHTrackedData.LIVES);
        if (lives > ServerLoadedConfig.MAX_LIVES)
        {
            player.getDataTracker().set(NSHTrackedData.LIVES, ServerLoadedConfig.MAX_LIVES);
            lives = ServerLoadedConfig.MAX_LIVES;
        }
        long liveRegainTimeMarker = player.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_TIME_MARKER);
        if (player.isCreative() && ServerLoadedConfig.CREATIVE_RESETS_LIFE_COUNT)
        {
            if (lives != ServerLoadedConfig.MAX_LIVES)
            {
                player.getDataTracker().set(NSHTrackedData.LIVES, ServerLoadedConfig.MAX_LIVES);
            }
        }
        else if (lives != ServerLoadedConfig.MAX_LIVES)
        {
            long currentTime = player.getServerWorld().getTime();
            if (currentTime - liveRegainTimeMarker < ServerLoadedConfig.TIME_TO_REGAIN_LIFE)
            {
                if (lives == 0 && !player.isSpectator() && !player.isCreative())
                {
                    player.changeGameMode(GameMode.SPECTATOR);
                }
            }
            else
            {
                int livePlayerCanRegain = (int) ((currentTime - liveRegainTimeMarker) / ServerLoadedConfig.TIME_TO_REGAIN_LIFE);
                if (lives == 0)
                {
                    ServerPlayerEntity.Respawn respawn = player.getRespawn();
                    ServerWorld serverWorld = respawn != null ? player.server.getWorld(ServerPlayerEntity.Respawn.getDimension(respawn)) : player.getServerWorld();
                    BlockPos spawnPos = respawn != null ? respawn.pos() : serverWorld.getSpawnPos();

                    player.teleport(serverWorld, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), Set.of(), player.getYaw(), player.getPitch(), true);
                    player.changeGameMode(GameMode.SURVIVAL);
                }
                player.getDataTracker().set(NSHTrackedData.LIVES, Math.min(ServerLoadedConfig.MAX_LIVES, lives + livePlayerCanRegain));
                player.getDataTracker().set(NSHTrackedData.LIFE_REGAIN_TIME_MARKER, currentTime);
            }
        }
    }
}
