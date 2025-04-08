package fr.factionbedrock.notsohardcore.client.packet;

import fr.factionbedrock.notsohardcore.client.gui.InfoScreen;
import fr.factionbedrock.notsohardcore.packet.NSHNetworking;
import fr.factionbedrock.notsohardcore.packet.NSHOpenInfoScreenData;
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
    }
}
