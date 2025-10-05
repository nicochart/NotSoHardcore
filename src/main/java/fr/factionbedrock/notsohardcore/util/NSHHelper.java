package fr.factionbedrock.notsohardcore.util;

import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

import java.util.Set;

public class NSHHelper
{
    public static void respawnPlayer(ServerPlayerEntity player)
    {
        ServerPlayerEntity.Respawn respawn = player.getRespawn();
        ServerWorld serverWorld = respawn != null ? player.getServer().getWorld(ServerPlayerEntity.Respawn.getDimension(respawn)) : player.getWorld();
        BlockPos spawnPos = respawn != null ? respawn.pos() : serverWorld.getSpawnPos();

        player.teleport(serverWorld, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), Set.of(), player.getYaw(), player.getPitch(), true);
        player.changeGameMode(GameMode.SURVIVAL);
    }

    public static long getCurrentTime(PlayerEntity player, boolean useRealTime)
    {
        return useRealTime ? System.currentTimeMillis() / 50L : player.getWorld().getTime();
    }

    public static long getLiveRegainTimeMarker(PlayerEntity player, boolean useRealTime)
    {
        return useRealTime ? player.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER) / 50L : player.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_TICK_MARKER);
    }

    public static String getTimeStringFromTicks(long ticksToRegain)
    {
        int secondsToRegain = (int) (ticksToRegain / 20);
        return getTimeStringFromSeconds(secondsToRegain);
    }

    public static String getTimeStringFromSeconds(long secondsToRegain)
    {
        long days = secondsToRegain / 86400;
        long hours = (secondsToRegain % 86400) / 3600;
        long minutes = (secondsToRegain % 3600) / 60;
        long seconds = secondsToRegain % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0 || days > 0) sb.append(hours).append("h ");
        if (minutes > 0 || hours > 0 || days > 0) sb.append(minutes).append("m ");
        sb.append(seconds).append("s");

        return sb.toString().trim();
    }
}
