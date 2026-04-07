package fr.factionbedrock.notsohardcore.packet;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record NSHS2CSynchData(String name, int max_lives, int time_to_regain_life, boolean creative_resets_life_count, int lives, long live_regain_time_marker, boolean use_realtime, long live_regain_realtime_time_marker, boolean always_render_hardcore_hearts) implements CustomPacketPayload
{
    public static final Type<NSHS2CSynchData> ID = new Type<>(NotSoHardcore.id("s2c_sync_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, NSHS2CSynchData> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, NSHS2CSynchData::name,
            ByteBufCodecs.VAR_INT, NSHS2CSynchData::max_lives,
            ByteBufCodecs.VAR_INT, NSHS2CSynchData::time_to_regain_life,
            ByteBufCodecs.BOOL, NSHS2CSynchData::creative_resets_life_count,
            ByteBufCodecs.VAR_INT, NSHS2CSynchData::lives,
            ByteBufCodecs.VAR_LONG, NSHS2CSynchData::live_regain_time_marker,
            ByteBufCodecs.BOOL, NSHS2CSynchData::use_realtime,
            ByteBufCodecs.VAR_LONG, NSHS2CSynchData::live_regain_realtime_time_marker,
            ByteBufCodecs.BOOL, NSHS2CSynchData::always_render_hardcore_hearts,
            NSHS2CSynchData::new
    );

    @Override public Type<? extends CustomPacketPayload> type() {return ID;}
}
