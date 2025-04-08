package fr.factionbedrock.notsohardcore.packet;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class NSHNetworking
{
    public static final NSHData OPEN_INFO_SCREEN_REQUEST_PACKET = new NSHData("open_info_screen_request", 0);
    public static final NSHOpenInfoScreenData OPEN_INFO_SCREEN_PACKET = new NSHOpenInfoScreenData("open_info_screen", NotSoHardcore.MAX_LIVES, NotSoHardcore.TIME_TO_REGAIN_LIVE);

    public static void registerData()
    {
        PayloadTypeRegistry.playC2S().register(NSHData.ID, NSHData.CODEC);
        PayloadTypeRegistry.playC2S().register(NSHOpenInfoScreenData.ID, NSHOpenInfoScreenData.CODEC);
        PayloadTypeRegistry.playS2C().register(NSHData.ID, NSHData.CODEC);
        PayloadTypeRegistry.playS2C().register(NSHOpenInfoScreenData.ID, NSHOpenInfoScreenData.CODEC);
    }

    public static void registerServerReceiver()
    {
        ServerPlayNetworking.registerGlobalReceiver(NSHData.ID, (payload, context) ->
        {
            if (payload.name().equals(OPEN_INFO_SCREEN_REQUEST_PACKET.name()))
            {
                ServerPlayNetworking.send(context.player(), OPEN_INFO_SCREEN_PACKET);
            }
        });
    }
}
