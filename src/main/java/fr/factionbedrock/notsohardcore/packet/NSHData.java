package fr.factionbedrock.notsohardcore.packet;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record NSHData(String name, int age) implements CustomPayload
{
    public static final CustomPayload.Id<NSHData> ID = new CustomPayload.Id<>(NotSoHardcore.id("data"));

    public static final PacketCodec<RegistryByteBuf, NSHData> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, NSHData::name,
            PacketCodecs.VAR_INT, NSHData::age,
            NSHData::new);

    @Override public Id<? extends CustomPayload> getId() {return ID;}
}