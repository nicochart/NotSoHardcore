package fr.factionbedrock.notsohardcore.packet;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record NSHS2CSynchData(String name, int max_lives, int time_to_regain_life, boolean creative_resets_life_count, int lives, long live_regain_time_marker, boolean use_realtime, long live_regain_realtime_time_marker, boolean always_render_hardcore_hearts) implements CustomPayload
{
    public static final Id<NSHS2CSynchData> ID = new Id<>(NotSoHardcore.id("s2c_sync_data"));

    public static final PacketCodec<RegistryByteBuf, NSHS2CSynchData> CODEC = PacketCodec.of((data, buf) ->
    {
        PacketCodecs.STRING.encode(buf, data.name());
        PacketCodecs.VAR_INT.encode(buf, data.max_lives());
        PacketCodecs.VAR_INT.encode(buf, data.time_to_regain_life());
        PacketCodecs.BOOL.encode(buf, data.creative_resets_life_count());
        PacketCodecs.VAR_INT.encode(buf, data.lives());
        PacketCodecs.VAR_LONG.encode(buf, data.live_regain_time_marker());
        PacketCodecs.BOOL.encode(buf, data.use_realtime());
        PacketCodecs.VAR_LONG.encode(buf, data.live_regain_realtime_time_marker());
        PacketCodecs.BOOL.encode(buf, data.always_render_hardcore_hearts());
    },
    buf -> new NSHS2CSynchData(buf.readString(), buf.readVarInt(), buf.readVarInt(), buf.readBoolean(), buf.readVarInt(), buf.readVarLong(), buf.readBoolean(), buf.readVarLong(), buf.readBoolean()));

    @Override public Id<? extends CustomPayload> getId() {return ID;}
}
