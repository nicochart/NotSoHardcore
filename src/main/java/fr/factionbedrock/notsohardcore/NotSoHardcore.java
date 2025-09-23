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

    public static NSHConfig CONFIG;
    public static int MAX_LIVES;
    public static int TIME_TO_REGAIN_LIFE;
    public static Boolean CREATIVE_RESETS_LIFE_COUNT;
    // New realtime regain configuration (server-synced)
    public static Boolean USE_REALTIME_REGAIN;
    public static int TIME_TO_REGAIN_LIFE_SECONDS;

    @Override public void onInitialize()
    {
        CONFIG = NSHConfigLoader.loadConfig();
        MAX_LIVES = Math.max(CONFIG.maxLives, 1);
        TIME_TO_REGAIN_LIFE = CONFIG.timeToRegainLife >= 0 ? CONFIG.timeToRegainLife : Integer.MAX_VALUE;
        CREATIVE_RESETS_LIFE_COUNT = CONFIG.creativeResetsLifeCount;
        // Realtime regain params
        USE_REALTIME_REGAIN = CONFIG.useRealtimeRegain;
        TIME_TO_REGAIN_LIFE_SECONDS = CONFIG.timeToRegainLifeSeconds > 0 ? CONFIG.timeToRegainLifeSeconds : Integer.MAX_VALUE;

        // Publish server-side effective params (including realtime mode)
        ServerLoadedConfig.storeServerParams(
                MAX_LIVES,
                TIME_TO_REGAIN_LIFE,
                CREATIVE_RESETS_LIFE_COUNT,
                USE_REALTIME_REGAIN,
                TIME_TO_REGAIN_LIFE_SECONDS);

		NSHItems.load();
		NSHTrackedData.load();
        NSHNetworking.registerData();
        NSHNetworking.registerServerReceiver();
        NSHPlayerEvents.registerPlayerEvents();

        // Register in-game commands (singleplayer/integrated server compatible)
        NSHCommands.register();
    }

	@Override public void onInitializeClient()
	{
		NSHKeyBinds.registerKeybinds();
		NSHKeyBinds.registerPressedInteractions();
		NSHClientNetworking.registerClientReceiver();
	}

	public static Identifier id(String path) {return Identifier.of(MOD_ID, path);}
}
