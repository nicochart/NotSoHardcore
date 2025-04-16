package fr.factionbedrock.notsohardcore.registry;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.item.*;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class NSHItems
{
    public static final Item SHARD_OF_REVIVING = register("shard_of_reviving", new ShardOfRevivingItem(new Item.Settings().food(FoodComponents.GOLDEN_APPLE)));

    public static <T extends Item> T register(String name, T item) {return Registry.register(Registries.ITEM, NotSoHardcore.id(name), item);}

    public static void load() {}
}
