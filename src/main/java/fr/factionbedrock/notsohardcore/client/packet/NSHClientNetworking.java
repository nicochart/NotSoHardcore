package fr.factionbedrock.notsohardcore.client.packet;

import fr.factionbedrock.notsohardcore.client.gui.InfoScreen;
import fr.factionbedrock.notsohardcore.packet.NSHNetworking;
import fr.factionbedrock.notsohardcore.packet.NSHOpenInfoScreenData;
import fr.factionbedrock.notsohardcore.packet.NSHS2CSynchData;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public class NSHClientNetworking
{
    public static void registerClientReceiver()
    {
        ClientPlayNetworking.registerGlobalReceiver(NSHOpenInfoScreenData.ID, (payload, context) ->
        {
            if (payload.name().equals(NSHNetworking.OPEN_INFO_SCREEN_PACKET.name()))
            {
                MinecraftClient client = MinecraftClient.getInstance();
                client.setScreen(new InfoScreen(client.player, payload.max_lives(), payload.time_to_regain_life()));
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(NSHS2CSynchData.ID, (payload, context) ->
        {
            if (payload.name().equals("sync_nsh_data"))
            {
                MinecraftClient client = MinecraftClient.getInstance();
                client.player.getDataTracker().set(NSHTrackedData.LIVES, payload.lives());
                client.player.getDataTracker().set(NSHTrackedData.LIFE_REGAIN_TIME_MARKER, payload.live_regain_time_marker());
            }
        });
    }
}
