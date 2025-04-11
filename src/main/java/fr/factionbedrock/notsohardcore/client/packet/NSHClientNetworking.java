package fr.factionbedrock.notsohardcore.client.packet;

import fr.factionbedrock.notsohardcore.config.ServerLoadedConfig;
import fr.factionbedrock.notsohardcore.packet.NSHS2CSynchData;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public class NSHClientNetworking
{
    public static void registerClientReceiver()
    {
        ClientPlayNetworking.registerGlobalReceiver(NSHS2CSynchData.ID, (payload, context) ->
        {
            if (payload.name().equals("sync_nsh_data"))
            {
                MinecraftClient client = MinecraftClient.getInstance();
                client.player.getDataTracker().set(NSHTrackedData.LIVES, payload.lives());
                client.player.getDataTracker().set(NSHTrackedData.LIFE_REGAIN_TIME_MARKER, payload.live_regain_time_marker());
                ServerLoadedConfig.storeServerParams(payload.max_lives(), payload.time_to_regain_life(), payload.creative_resets_life_count());
            }
        });
    }
}
