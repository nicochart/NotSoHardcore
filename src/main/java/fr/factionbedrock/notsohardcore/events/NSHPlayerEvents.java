package fr.factionbedrock.notsohardcore.events;

import fr.factionbedrock.notsohardcore.packet.NSHNetworking;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

public class NSHPlayerEvents
{
    public static void registerPlayerEvents()
    {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
        {
            ServerPlayerEntity player = handler.getPlayer();
            NSHNetworking.sendS2CSync(player);
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) ->
        {
            NSHNetworking.sendS2CSync(newPlayer);

            // Force Survival on the next tick if the player has lives (avoids ordering with hardcore spectator enforcement)
            int lives = newPlayer.getDataTracker().get(NSHTrackedData.LIVES);
            if (lives > 0 && !newPlayer.isCreative())
            {
                newPlayer.getEntityWorld().getServer().execute(() -> newPlayer.changeGameMode(GameMode.SURVIVAL));
            }
        });
    }
}
