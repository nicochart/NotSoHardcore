package fr.factionbedrock.notsohardcore.mixin;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import fr.factionbedrock.notsohardcore.util.NSHHelper;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
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
        if (lives > NotSoHardcore.MAX_LIVES)
        {
            player.getDataTracker().set(NSHTrackedData.LIVES, NotSoHardcore.MAX_LIVES);
            lives = NotSoHardcore.MAX_LIVES;
        }
        long liveRegainTimeMarker = player.getDataTracker().get(NSHTrackedData.LIVE_REGAIN_TIME_MARKER);
        if (player.isCreative() && NotSoHardcore.CREATIVE_RESETS_LIVE_COUNT)
        {
            if (lives != NotSoHardcore.MAX_LIVES)
            {
                player.getDataTracker().set(NSHTrackedData.LIVES, NotSoHardcore.MAX_LIVES);
            }
        }
        else if (lives != NotSoHardcore.MAX_LIVES)
        {
            long currentTime = player.getServerWorld().getTime();
            if (currentTime - liveRegainTimeMarker < NotSoHardcore.TIME_TO_REGAIN_LIVE)
            {
                if (lives == 0 && !player.isSpectator() && !player.isCreative())
                {
                    player.changeGameMode(GameMode.SPECTATOR);
                }
            }
            else
            {
                int livePlayerCanRegain = (int) ((currentTime - liveRegainTimeMarker) / NotSoHardcore.TIME_TO_REGAIN_LIVE);
                if (lives == 0)
                {
                    BlockPos spawnPos = player.getServerWorld().getSpawnPos();
                    player.teleport(player.getServerWorld(), spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), Set.of(), player.getYaw(), player.getPitch(), true);
                    player.changeGameMode(GameMode.SURVIVAL);
                }
                player.getDataTracker().set(NSHTrackedData.LIVES, Math.min(NotSoHardcore.MAX_LIVES, lives + livePlayerCanRegain));
                player.getDataTracker().set(NSHTrackedData.LIVE_REGAIN_TIME_MARKER, currentTime);
            }
        }
    }
}
