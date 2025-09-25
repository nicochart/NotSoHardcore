package fr.factionbedrock.notsohardcore.packet;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.config.LoadedConfig;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NSHNetworking
{
    public static void registerData()
    {
        PayloadTypeRegistry.playC2S().register(NSHS2CSynchData.ID, NSHS2CSynchData.CODEC);
        PayloadTypeRegistry.playS2C().register(NSHS2CSynchData.ID, NSHS2CSynchData.CODEC);
    }

    public static void sendS2CSync(@Nullable MinecraftServer server)
    {
        if (server == null)
        {
            NotSoHardcore.LOGGER.error("NSH ERROR - Tried to send S2C Sync from null Minecraft Server !");
        }
        else {sendS2CSync(server.getPlayerManager().getPlayerList());}
    }

    public static void sendS2CSync(List<ServerPlayerEntity> players)
    {
        for (ServerPlayerEntity player : players) {sendS2CSync(player);}
    }

    public static void sendS2CSync(ServerPlayerEntity player)
    {
        //The packet is sent from server, so calling LoadedConfig.Local or LoadedConfig.Server is the same.
        ServerPlayNetworking.send(player, new NSHS2CSynchData("sync_nsh_data", LoadedConfig.Local.MAX_LIVES, LoadedConfig.Local.TIME_TO_REGAIN_LIFE, LoadedConfig.Local.CREATIVE_RESETS_LIFE_COUNT, player.getDataTracker().get(NSHTrackedData.LIVES), player.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_TICK_MARKER), LoadedConfig.Local.USE_REALTIME_REGAIN, player.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER)));
    }

    public static void registerServerReceiver()
    {
        //ServerPlayNetworking.registerGlobalReceiver(CustomData.ID, (payload, context) ->
        //{
        //
        //});
    }
}
