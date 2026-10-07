package fr.factionbedrock.notsohardcore;

import fr.factionbedrock.notsohardcore.client.packet.NSHClientNetworking;
import fr.factionbedrock.notsohardcore.commands.NSHCommands;
import fr.factionbedrock.notsohardcore.client.registry.NSHKeyBinds;
import fr.factionbedrock.notsohardcore.config.*;
import fr.factionbedrock.notsohardcore.events.NSHPlayerEvents;
import fr.factionbedrock.notsohardcore.packet.NSHNetworking;
import fr.factionbedrock.notsohardcore.registry.NSHItems;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(NotSoHardcore.MOD_ID)
public class NotSoHardcore
{
    public static final String MOD_ID = "notsohardcore";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public NotSoHardcore()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        this.onInitialize(modEventBus);
        if (FMLEnvironment.dist.isClient()) {this.onInitializeClient(modEventBus);}
    }

    public void onInitialize(IEventBus modEventBus)
    {
        NSHConfigLoader.initLocalAndServerConfig();

		NSHItems.load(modEventBus);
		NSHTrackedData.load();
        NSHNetworking.registerData();
        NSHNetworking.registerServerReceiver();
        NSHPlayerEvents.registerPlayerEvents();
        NSHCommands.register();
    }

	public void onInitializeClient(IEventBus modEventBus)
	{
		NSHKeyBinds.registerKeybinds(modEventBus);
		NSHKeyBinds.registerPressedInteractions();
		NSHClientNetworking.registerClientReceiver();
	}

	public static ResourceLocation id(String path) {return new ResourceLocation(MOD_ID, path);}
}
