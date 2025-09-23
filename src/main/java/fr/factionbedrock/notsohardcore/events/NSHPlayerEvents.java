package fr.factionbedrock.notsohardcore.events;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.networking.NSHS2CSynchData;
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
            ServerPlayNetworking.send(player, new NSHS2CSynchData("sync_nsh_data", NotSoHardcore.MAX_LIVES, NotSoHardcore.TIME_TO_REGAIN_LIFE, NotSoHardcore.CREATIVE_RESETS_LIFE_COUNT, player.getDataTracker().get(NSHTrackedData.LIVES), player.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_TIME_MARKER)));
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) ->
        {
            ServerPlayNetworking.send(newPlayer, new NSHS2CSynchData("sync_nsh_data", NotSoHardcore.MAX_LIVES, NotSoHardcore.TIME_TO_REGAIN_LIFE, NotSoHardcore.CREATIVE_RESETS_LIFE_COUNT, newPlayer.getDataTracker().get(NSHTrackedData.LIVES), newPlayer.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_TIME_MARKER)));
        });
    }
}
