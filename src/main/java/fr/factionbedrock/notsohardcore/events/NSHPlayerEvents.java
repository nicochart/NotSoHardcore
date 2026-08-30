package fr.factionbedrock.notsohardcore.events;

import fr.factionbedrock.notsohardcore.packet.NSHNetworking;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

public class NSHPlayerEvents
{
    public static void registerPlayerEvents()
    {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
        {
            ServerPlayer player = handler.getPlayer();
            NSHNetworking.sendS2CSync(player);
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) ->
        {
            NSHNetworking.sendS2CSync(newPlayer);

            // Force Survival on the next tick if the player has lives (avoids ordering with hardcore spectator enforcement)
            int lives = newPlayer.getEntityData().get(NSHTrackedData.LIVES);
            if (lives > 0 && !newPlayer.isCreative())
            {
                newPlayer.level().getServer().execute(() -> newPlayer.setGameMode(GameType.SURVIVAL));
            }
        });
    }
}
