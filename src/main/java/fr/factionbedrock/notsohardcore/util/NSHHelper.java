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
    public static void forceRespawnPlayer(ServerPlayerEntity player)
    {
        int lives = player.getDataTracker().get(NSHTrackedData.LIVES);
        if (lives <= 0) {player.getDataTracker().set(NSHTrackedData.LIVES, 1);}
        respawnPlayer(player);
    }

    public static void respawnPlayer(ServerPlayerEntity player)
    {
        BlockPos spawnPos = player.getSpawnPointPosition();
        ServerWorld serverWorld = player.getServerWorld();
        if (spawnPos == null) {spawnPos = serverWorld.getSpawnPos();}
        if (player.getSpawnPointDimension() != null) {serverWorld = player.server.getWorld(player.getSpawnPointDimension());}

        player.teleport(serverWorld, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), Set.of(), player.getYaw(), player.getPitch());
        player.changeGameMode(GameMode.SURVIVAL);
    }

    public static long getCurrentTime(PlayerEntity player, boolean useRealTime)
    {
        return useRealTime ? System.currentTimeMillis() / 50L : player.getEntityWorld().getTime();
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
