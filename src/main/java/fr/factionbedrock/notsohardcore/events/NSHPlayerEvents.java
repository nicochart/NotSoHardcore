package fr.factionbedrock.notsohardcore.events;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.packet.NSHS2CSynchData;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

public class NSHPlayerEvents
{
    public static void registerPlayerEvents()
    {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
        {
            ServerPlayerEntity player = handler.getPlayer();
            ServerPlayNetworking.send(player, new NSHS2CSynchData("sync_nsh_data", NotSoHardcore.MAX_LIVES, NotSoHardcore.TIME_TO_REGAIN_LIFE, NotSoHardcore.CREATIVE_RESETS_LIFE_COUNT, player.getDataTracker().get(NSHTrackedData.LIVES), player.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_TICK_MARKER), NotSoHardcore.USE_REALTIME_REGAIN, player.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER)));
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) ->
        {
            ServerPlayNetworking.send(newPlayer, new NSHS2CSynchData("sync_nsh_data", NotSoHardcore.MAX_LIVES, NotSoHardcore.TIME_TO_REGAIN_LIFE, NotSoHardcore.CREATIVE_RESETS_LIFE_COUNT, newPlayer.getDataTracker().get(NSHTrackedData.LIVES), newPlayer.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_TICK_MARKER), NotSoHardcore.USE_REALTIME_REGAIN, newPlayer.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER)));

            // Force Survival on the next tick if the player has lives (avoids ordering with hardcore spectator enforcement)
            int lives = newPlayer.getDataTracker().get(NSHTrackedData.LIVES);
            if (lives > 0 && !newPlayer.isCreative())
            {
                newPlayer.server.execute(() -> newPlayer.changeGameMode(GameMode.SURVIVAL));
            }
        });
    }
}
