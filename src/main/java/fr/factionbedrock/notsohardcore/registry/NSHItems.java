package fr.factionbedrock.notsohardcore.registry;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.item.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;

public class NSHItems
{
    public static final Item SHARD_OF_REVIVING = register(Keys.SHARD_OF_REVIVING.identifier().getPath(), new ShardOfRevivingItem(new Item.Properties().setId(Keys.SHARD_OF_REVIVING).food(Foods.GOLDEN_APPLE)));

    public static class Keys
    {
        public static final ResourceKey<Item> SHARD_OF_REVIVING = createKey("shard_of_reviving");

        private static ResourceKey<Item> createKey(String name)
        {
            return ResourceKey.create(Registries.ITEM, NotSoHardcore.id(name));
        }
    }

    public static <T extends Item> T register(String name, T item) {return Registry.register(BuiltInRegistries.ITEM, NotSoHardcore.id(name), item);}

    public static void load() {}
}
