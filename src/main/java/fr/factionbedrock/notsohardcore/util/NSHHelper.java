package fr.factionbedrock.notsohardcore.util;

import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public class NSHHelper
{
    public static void forceRespawnPlayer(ServerPlayer player)
    {
        int lives = player.getEntityData().get(NSHTrackedData.LIVES);
        if (lives <= 0) {player.getEntityData().set(NSHTrackedData.LIVES, 1);}
        respawnPlayer(player);
    }

    public static void respawnPlayer(ServerPlayer player)
    {
        ServerPlayer.RespawnConfig respawn = player.getRespawnConfig();
        ServerLevel serverWorld = respawn != null ? player.level().getServer().getLevel(ServerPlayer.RespawnConfig.getDimensionOrDefault(respawn)) : player.level();
        BlockPos spawnPos = respawn != null ? respawn.respawnData().pos() : serverWorld.getRespawnData().pos();

        player.teleportTo(serverWorld, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), Set.of(), player.getYRot(), player.getXRot(), true);
        player.setGameMode(GameType.SURVIVAL);
    }

    public static long getCurrentTime(Player player, boolean useRealTime)
    {
        return useRealTime ? System.currentTimeMillis() / 50L : player.level().getGameTime();
    }

    public static long getLiveRegainTimeMarker(Player player, boolean useRealTime)
    {
        return useRealTime ? player.getEntityData().get(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER) / 50L : player.getEntityData().get(NSHTrackedData.LIFE_REGAIN_TICK_MARKER);
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
