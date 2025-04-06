package fr.factionbedrock.notsohardcore.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class NSHNetworking
{
    public static final NSHData USE_ABILITY_PACKET = new NSHData("use_ability", 0);
    public static final NSHData RECEIVED_PACKET = new NSHData("received", 0);

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
            if (payload.name().equals(USE_ABILITY_PACKET.name()))
            {
                context.player().sendMessage(Text.literal("Activated ability !"), false);
                sendPacketFromServer(context.player(), RECEIVED_PACKET);
            }
        });
    }

    public static void registerClientReceiver()
    {
        ClientPlayNetworking.registerGlobalReceiver(NSHData.ID, (payload, context) ->
        {
            if (payload.name().equals(RECEIVED_PACKET.name()))
            {
                context.player().sendMessage(Text.literal("Received packet from server !"), false);
            }
        });
    }
}
