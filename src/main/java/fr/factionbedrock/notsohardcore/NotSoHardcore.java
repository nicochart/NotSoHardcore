package fr.factionbedrock.notsohardcore;

import fr.factionbedrock.notsohardcore.client.registry.NSHKeyBinds;
import fr.factionbedrock.notsohardcore.config.*;
import fr.factionbedrock.notsohardcore.packet.NSHData;
import fr.factionbedrock.notsohardcore.packet.NSHNetworking;
import fr.factionbedrock.notsohardcore.registry.NSHComponents;
import fr.factionbedrock.notsohardcore.registry.NSHItems;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotSoHardcore implements ModInitializer, ClientModInitializer
{
	public static final String MOD_ID = "notsohardcore";
	
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static NSHConfig CONFIG;
	public static int MAX_LIVES;
	public static int TIME_TO_REGAIN_LIVE;

	@Override public void onInitialize()
	{
		CONFIG = NSHConfigLoader.loadConfig();
		MAX_LIVES = CONFIG.maxLives;
		TIME_TO_REGAIN_LIVE = CONFIG.timeToRegainLive;

		NSHItems.load();
		NSHComponents.load();
		NSHTrackedData.load();
		PayloadTypeRegistry.playC2S().register(NSHData.ID, NSHData.CODEC);
		NSHNetworking.registerServerReceiver();
	}

	@Override public void onInitializeClient()
	{
		NSHKeyBinds.registerKeybinds();
		NSHKeyBinds.registerPressedInteractions();
		PayloadTypeRegistry.playS2C().register(NSHData.ID, NSHData.CODEC);
		NSHNetworking.registerClientReceiver();
	}

	public static Identifier id(String path) {return Identifier.of(MOD_ID, path);}
}