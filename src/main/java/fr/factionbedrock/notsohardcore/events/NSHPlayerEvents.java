package fr.factionbedrock.notsohardcore.events;

import fr.factionbedrock.notsohardcore.packet.NSHS2CSynchData;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class NSHPlayerEvents
{
    public static void registerPlayerEvents()
    {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
        {
            ServerPlayerEntity player = handler.getPlayer();
            ServerPlayNetworking.send(player, new NSHS2CSynchData("sync_nsh_data", player.getDataTracker().get(NSHTrackedData.LIVES), player.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_TIME_MARKER)));
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) ->
        {
            ServerPlayNetworking.send(newPlayer, new NSHS2CSynchData("sync_nsh_data", newPlayer.getDataTracker().get(NSHTrackedData.LIVES), newPlayer.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_TIME_MARKER)));
        });
    }
}
