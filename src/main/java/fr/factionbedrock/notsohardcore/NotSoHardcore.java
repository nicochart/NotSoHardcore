package fr.factionbedrock.notsohardcore;

import fr.factionbedrock.notsohardcore.client.packet.NSHClientNetworking;
import fr.factionbedrock.notsohardcore.commands.NSHCommands;
import fr.factionbedrock.notsohardcore.client.registry.NSHKeyBinds;
import fr.factionbedrock.notsohardcore.config.*;
import fr.factionbedrock.notsohardcore.events.NSHPlayerEvents;
import fr.factionbedrock.notsohardcore.packet.NSHNetworking;
import fr.factionbedrock.notsohardcore.registry.NSHItems;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotSoHardcore implements ModInitializer, ClientModInitializer
{
    public static final String MOD_ID = "notsohardcore";
    
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override public void onInitialize()
    {
        NSHConfigLoader.initLocalAndServerConfig();

		NSHItems.load();
		NSHTrackedData.load();
        NSHNetworking.registerData();
        NSHNetworking.registerServerReceiver();
        NSHPlayerEvents.registerPlayerEvents();
        NSHCommands.register();
    }

	@Override public void onInitializeClient()
	{
		NSHKeyBinds.registerKeybinds();
		NSHKeyBinds.registerPressedInteractions();
		NSHClientNetworking.registerClientReceiver();
	}

    public static void computeValuesFromConfig()
    {
        LoadedConfig.Local.MAX_LIVES = Math.max(LoadedConfig.Local.CONFIG.maxLives, 1);
        LoadedConfig.Local.TIME_TO_REGAIN_LIFE = LoadedConfig.Local.CONFIG.timeToRegainLife >= 0 ? LoadedConfig.Local.CONFIG.timeToRegainLife : Integer.MAX_VALUE;
        LoadedConfig.Local.CREATIVE_RESETS_LIFE_COUNT = LoadedConfig.Local.CONFIG.creativeResetsLifeCount;
        LoadedConfig.Local.USE_REALTIME_REGAIN = LoadedConfig.Local.CONFIG.useRealtimeRegain;
    }

	public static Identifier id(String path) {return Identifier.of(MOD_ID, path);}
}
