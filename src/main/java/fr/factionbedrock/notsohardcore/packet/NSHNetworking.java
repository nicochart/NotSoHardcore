package fr.factionbedrock.notsohardcore.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class NSHNetworking
{
    public static final NSHData OPEN_INFO_SCREEN_PACKET = new NSHData("open_info_screen", 0);

    public static void sendPacketFromClient(NSHData payload)
    {
        ClientPlayNetworking.send(payload);
    }

    public static void sendPacketFromServer(ServerPlayerEntity serverPlayer, NSHData payload)
    {
        ServerPlayNetworking.send(serverPlayer, payload);
    }

    public static void registerServerReceiver()
    {
        ServerPlayNetworking.registerGlobalReceiver(NSHData.ID, (payload, context) ->
        {
            if (payload.name().equals(OPEN_INFO_SCREEN_PACKET.name()))
            {
                //context.player().sendMessage(Text.literal("Displaying info menu !"), false);
            }
        });
    }

    public static void registerClientReceiver()
    {
        ClientPlayNetworking.registerGlobalReceiver(NSHData.ID, (payload, context) ->
        {
        });
    }
}
