package fr.factionbedrock.notsohardcore.registry;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.item.*;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NSHItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NotSoHardcore.MOD_ID);

    public static final RegistryObject<Item> SHARD_OF_REVIVING = register(Keys.SHARD_OF_REVIVING.location().getPath(), () -> new ShardOfRevivingItem(new Item.Properties().food(Foods.GOLDEN_APPLE)));

    public static class Keys
    {
        public static final ResourceKey<Item> SHARD_OF_REVIVING = createKey("shard_of_reviving");

        private static ResourceKey<Item> createKey(String name)
        {
            return ResourceKey.create(Registries.ITEM, NotSoHardcore.id(name));
        }
    }

    public static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item) {return ITEMS.register(name, item);}

    public static void load(IEventBus modEventBus) {ITEMS.register(modEventBus);}
}
