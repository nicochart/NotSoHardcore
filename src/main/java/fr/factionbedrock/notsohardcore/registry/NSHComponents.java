package fr.factionbedrock.notsohardcore.registry;

import com.mojang.serialization.Codec;
import fr.factionbedrock.notsohardcore.NotSoHardcore;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class NSHComponents
{
    public static final ComponentType<Integer> CLICK_COUNT_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            NotSoHardcore.id("click_count"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static final ComponentType<Integer> TOTAL_CLICK_COUNT_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            NotSoHardcore.id("total_click_count"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static void load() {}
}
