package fr.factionbedrock.notsohardcore.packet;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record NSHS2CSynchData(String name, int max_lives, int time_to_regain_life, boolean creative_resets_life_count, int lives, long live_regain_time_marker, boolean use_realtime, long live_regain_realtime_time_marker, boolean always_render_hardcore_hearts)
{
    public static final ResourceLocation ID = NotSoHardcore.id("s2c_sync_data");

    public void encode(FriendlyByteBuf buf)
    {
        buf.writeUtf(this.name);
        buf.writeVarInt(this.max_lives);
        buf.writeVarInt(this.time_to_regain_life);
        buf.writeBoolean(this.creative_resets_life_count);
        buf.writeVarInt(this.lives);
        buf.writeVarLong(this.live_regain_time_marker);
        buf.writeBoolean(this.use_realtime);
        buf.writeVarLong(this.live_regain_realtime_time_marker);
        buf.writeBoolean(this.always_render_hardcore_hearts);
    }

    public static NSHS2CSynchData decode(FriendlyByteBuf buf)
    {
        return new NSHS2CSynchData(
                buf.readUtf(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readBoolean(),
                buf.readVarInt(),
                buf.readVarLong(),
                buf.readBoolean(),
                buf.readVarLong(),
                buf.readBoolean()
        );
    }
}
