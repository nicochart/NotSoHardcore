package fr.factionbedrock.notsohardcore.packet;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.CustomPayload.Id;

public record NSHS2CSynchData(String name, int max_lives, int time_to_regain_life, boolean creative_resets_life_count, int lives, long live_regain_time_marker, boolean use_realtime, long live_regain_realtime_time_marker) implements CustomPayload
{
    public static final Id<NSHS2CSynchData> ID = new Id<>(NotSoHardcore.id("s2c_sync_data"));

    public static final PacketCodec<RegistryByteBuf, NSHS2CSynchData> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, NSHS2CSynchData::name,
            PacketCodecs.VAR_INT, NSHS2CSynchData::max_lives,
            PacketCodecs.VAR_INT, NSHS2CSynchData::time_to_regain_life,
            PacketCodecs.BOOLEAN, NSHS2CSynchData::creative_resets_life_count,
            PacketCodecs.VAR_INT, NSHS2CSynchData::lives,
            PacketCodecs.VAR_LONG, NSHS2CSynchData::live_regain_time_marker,
            PacketCodecs.BOOLEAN, NSHS2CSynchData::use_realtime,
            PacketCodecs.VAR_LONG, NSHS2CSynchData::live_regain_realtime_time_marker,
            NSHS2CSynchData::new
    );

    @Override public Id<? extends CustomPayload> getId() {return ID;}
}
