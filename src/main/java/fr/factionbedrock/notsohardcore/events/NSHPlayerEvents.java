package fr.factionbedrock.notsohardcore.events;

import fr.factionbedrock.notsohardcore.packet.NSHNetworking;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class NSHPlayerEvents
{
    public static void registerPlayerEvents()
    {
        MinecraftForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedInEvent event) ->
        {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            NSHNetworking.sendS2CSync(player);
        });

        /* Modifying player game mode after respawn is now done in HardcoreRespawnRedirectMixin
         *
         * ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) ->
         * {
         *     NSHNetworking.sendS2CSync(newPlayer);
         *
         *     // Force Survival on the next tick if the player has lives (avoids ordering with hardcore spectator enforcement)
         *     int lives = newPlayer.getEntityData().get(NSHTrackedData.LIVES);
         *     if (lives > 0 && !newPlayer.isCreative())
         *     {
         *         newPlayer.level().getServer().execute(() -> newPlayer.setGameMode(GameType.SURVIVAL));
         *     }
         * });
         */
    }
}
