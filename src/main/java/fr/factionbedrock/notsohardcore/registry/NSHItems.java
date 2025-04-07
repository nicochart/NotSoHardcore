package fr.factionbedrock.notsohardcore.registry;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.item.*;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class NSHItems
{
    public static final Item SHARD_OF_REVIVING = register(Keys.SHARD_OF_REVIVING.getValue().getPath(), new ShardOfRevivingItem(new Item.Settings().registryKey(Keys.SHARD_OF_REVIVING).food(FoodComponents.GOLDEN_APPLE)));

    public static class Keys
    {
        public static final RegistryKey<Item> SHARD_OF_REVIVING = createKey("shard_of_reviving");

        private static RegistryKey<Item> createKey(String name)
        {
            return RegistryKey.of(RegistryKeys.ITEM, NotSoHardcore.id(name));
        }
    }

    public static <T extends Item> T register(String name, T item) {return Registry.register(Registries.ITEM, NotSoHardcore.id(name), item);}

    public static void load() {}
}
