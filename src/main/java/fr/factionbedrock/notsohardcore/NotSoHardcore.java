package fr.factionbedrock.notsohardcore;

import fr.factionbedrock.notsohardcore.client.networking.NSHClientNetworking;
import fr.factionbedrock.notsohardcore.client.registry.NSHKeyBinds;
import fr.factionbedrock.notsohardcore.config.*;
import fr.factionbedrock.notsohardcore.events.NSHPlayerEvents;
import fr.factionbedrock.notsohardcore.networking.NSHNetworking;
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

	public static NSHConfig CONFIG;
	public static int MAX_LIVES;
	public static int TIME_TO_REGAIN_LIFE;
	public static Boolean CREATIVE_RESETS_LIFE_COUNT;

	@Override public void onInitialize()
	{
		CONFIG = NSHConfigLoader.loadConfig();
		MAX_LIVES = Math.max(CONFIG.maxLives, 1);
		TIME_TO_REGAIN_LIFE = CONFIG.timeToRegainLife >= 0 ? CONFIG.timeToRegainLife : Integer.MAX_VALUE;
		CREATIVE_RESETS_LIFE_COUNT = CONFIG.creativeResetsLifeCount;
		ServerLoadedConfig.storeServerParams(MAX_LIVES, TIME_TO_REGAIN_LIFE, CREATIVE_RESETS_LIFE_COUNT);

		NSHItems.load();
		NSHTrackedData.load();
		NSHNetworking.registerData();
		NSHNetworking.registerServerReceiver();
		NSHPlayerEvents.registerPlayerEvents();
	}

	@Override public void onInitializeClient()
	{
		NSHKeyBinds.registerKeybinds();
		NSHKeyBinds.registerPressedInteractions();
		NSHClientNetworking.registerClientReceiver();
	}

	public static Identifier id(String path) {return Identifier.of(MOD_ID, path);}
}