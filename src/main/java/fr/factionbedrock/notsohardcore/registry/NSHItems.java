package fr.factionbedrock.notsohardcore.registry;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class NSHItems
{
    public static class Keys
    {
        private static RegistryKey<Item> createKey(String name)
        {
            return RegistryKey.of(RegistryKeys.ITEM, NotSoHardcore.id(name));
        }
    }

    public static <T extends Item> T register(String name, T item) {return Registry.register(Registries.ITEM, NotSoHardcore.id(name), item);}

    public static void load() {}
}
