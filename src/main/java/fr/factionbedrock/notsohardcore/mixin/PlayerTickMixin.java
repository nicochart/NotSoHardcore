package fr.factionbedrock.notsohardcore.mixin;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.config.ServerLoadedConfig;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ServerPlayerEntity.class)
public class PlayerTickMixin
{
    private static final Identifier CLICK_SPEED_MODIFIER = NotSoHardcore.id("click_speed_modifier");
    private static final Identifier CLICK_SCALE_MODIFIER = NotSoHardcore.id("click_scale_modifier");
    private static final Identifier CLICK_JUMP_MODIFIER = NotSoHardcore.id("click_jump_modifier");
    private static final Identifier CLICK_STEP_HEIGHT_MODIFIER = NotSoHardcore.id("click_step_height_modifier");

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
                    BlockPos spawnPos = player.getServerWorld().getSpawnPos();
                    player.teleport(player.getServerWorld(), spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), Set.of(), player.getYaw(), player.getPitch(), true);
                    player.changeGameMode(GameMode.SURVIVAL);
                }
                player.getDataTracker().set(NSHTrackedData.LIVES, Math.min(ServerLoadedConfig.MAX_LIVES, lives + livePlayerCanRegain));
                player.getDataTracker().set(NSHTrackedData.LIFE_REGAIN_TIME_MARKER, currentTime);
            }
        }
    }
}
