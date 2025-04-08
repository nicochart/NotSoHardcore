package fr.factionbedrock.notsohardcore.packet;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record NSHOpenInfoScreenData(String name, int max_lives, int time_to_regain_life) implements CustomPayload
{
    public static final Id<NSHOpenInfoScreenData> ID = new Id<>(NotSoHardcore.id("info_screen_data"));

    public static final PacketCodec<RegistryByteBuf, NSHOpenInfoScreenData> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, NSHOpenInfoScreenData::name,
            PacketCodecs.VAR_INT, NSHOpenInfoScreenData::max_lives,
            PacketCodecs.VAR_INT, NSHOpenInfoScreenData::time_to_regain_life,
            NSHOpenInfoScreenData::new);

    @Override public Id<? extends CustomPayload> getId() {return ID;}
}