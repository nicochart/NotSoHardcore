package fr.factionbedrock.notsohardcore.packet;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.config.LoadedConfig;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;

public class NSHNetworking
{
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(NSHS2CSynchData.ID, () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    //Set client-side by NSHClientNetworking.registerClientReceiver
    private static BiConsumer<NSHS2CSynchData, NetworkEvent.Context> clientReceiver = (payload, context) -> {};

    public static void registerData()
    {
        CHANNEL.messageBuilder(NSHS2CSynchData.class, 0, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(NSHS2CSynchData::encode)
                .decoder(NSHS2CSynchData::decode)
                .consumerMainThread((payload, context) -> clientReceiver.accept(payload, context.get()))
                .add();
    }

    public static void registerClientReceiver(BiConsumer<NSHS2CSynchData, NetworkEvent.Context> receiver) {clientReceiver = receiver;}

    public static void sendS2CSync(@Nullable MinecraftServer server)
    {
        if (server == null)
        {
            NotSoHardcore.LOGGER.error("NSH ERROR - Tried to send S2C Sync from null Minecraft Server !");
        }
        else {sendS2CSync(server.getPlayerList().getPlayers());}
    }

    public static void sendS2CSync(List<ServerPlayer> players)
    {
        for (ServerPlayer player : players) {sendS2CSync(player);}
    }

    public static void sendS2CSync(ServerPlayer player)
    {
        //The packet is sent from server, so calling LoadedConfig.Local or LoadedConfig.Server is the same.
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new NSHS2CSynchData("sync_nsh_data", LoadedConfig.Local.MAX_LIVES, LoadedConfig.Local.TIME_TO_REGAIN_LIFE, LoadedConfig.Local.CREATIVE_RESETS_LIFE_COUNT, player.getEntityData().get(NSHTrackedData.LIVES), player.getEntityData().get(NSHTrackedData.LIFE_REGAIN_TICK_MARKER), LoadedConfig.Local.USE_REALTIME_REGAIN, player.getEntityData().get(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER), LoadedConfig.Local.ALWAYS_RENDER_HARDCORE_HEARTS));
    }

    public static void registerServerReceiver()
    {
        //CHANNEL.messageBuilder(CustomData.class, 1, NetworkDirection.PLAY_TO_SERVER)...consumerMainThread((payload, context) ->
        //{
        //
        //}).add();
    }
}
