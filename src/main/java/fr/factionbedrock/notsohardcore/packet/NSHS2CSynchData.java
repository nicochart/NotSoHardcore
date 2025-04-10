package fr.factionbedrock.notsohardcore.packet;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record NSHS2CSynchData(String name, int lives, long live_regain_time_marker) implements CustomPayload
{
    public static final Id<NSHS2CSynchData> ID = new Id<>(NotSoHardcore.id("s2c_sync_data"));

    public static final PacketCodec<RegistryByteBuf, NSHS2CSynchData> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, NSHS2CSynchData::name,
            PacketCodecs.VAR_INT, NSHS2CSynchData::lives,
            PacketCodecs.VAR_LONG, NSHS2CSynchData::live_regain_time_marker,
            NSHS2CSynchData::new);

    @Override public Id<? extends CustomPayload> getId() {return ID;}
}