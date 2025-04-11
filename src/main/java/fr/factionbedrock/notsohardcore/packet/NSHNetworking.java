package fr.factionbedrock.notsohardcore.packet;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class NSHNetworking
{

    public static void registerData()
    {
        PayloadTypeRegistry.playC2S().register(NSHS2CSynchData.ID, NSHS2CSynchData.CODEC);
        PayloadTypeRegistry.playS2C().register(NSHS2CSynchData.ID, NSHS2CSynchData.CODEC);
    }

    public static void registerServerReceiver()
    {
        //ServerPlayNetworking.registerGlobalReceiver(CustomData.ID, (payload, context) ->
        //{
        //
        //});
    }
}
