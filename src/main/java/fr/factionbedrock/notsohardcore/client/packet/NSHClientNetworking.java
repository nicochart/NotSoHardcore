package fr.factionbedrock.notsohardcore.client.packet;

import fr.factionbedrock.notsohardcore.config.LoadedConfig;
import fr.factionbedrock.notsohardcore.packet.NSHNetworking;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.client.Minecraft;

public class NSHClientNetworking
{
    public static void registerClientReceiver()
    {
        NSHNetworking.registerClientReceiver((payload, context) ->
        {
            if (payload.name().equals("sync_nsh_data"))
            {
                Minecraft client = Minecraft.getInstance();
                client.player.getEntityData().set(NSHTrackedData.LIVES, payload.lives());
                client.player.getEntityData().set(NSHTrackedData.LIFE_REGAIN_TICK_MARKER, payload.live_regain_time_marker());
                client.player.getEntityData().set(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER, payload.live_regain_realtime_time_marker());
                //If on integrated server (single-player), the local config is used. If on dedicated server, the server config is used.
                LoadedConfig.overrideServerParams(payload.max_lives(), payload.time_to_regain_life(), payload.creative_resets_life_count(), payload.use_realtime(), payload.always_render_hardcore_hearts());
            }
        });
    }
}
