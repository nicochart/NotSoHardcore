package fr.factionbedrock.notsohardcore.config;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

import static fr.factionbedrock.notsohardcore.config.NSHConfigLoader.CONFIG_FOLDER;
import static fr.factionbedrock.notsohardcore.config.NSHConfigLoader.CONFIG_PATH;
import static net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil.GSON;

public class NSHConfigSaver
{
    public static void saveConfig(NSHConfig config)
    {
        try
        {
            Files.createDirectories(CONFIG_FOLDER);
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {GSON.toJson(config, writer);}
        }
        catch (IOException e) {e.printStackTrace();}
    }

    //-----------------------------------------------------------------
    //Think about sending S2C sync after writing any config update !
    //On dedicated servers, after using any of these, the client's "LoadedConfig.Server" will be outdated. Need sync !
    public static void updateMaxLives(int maxLives)
    {
        updateConfig(maxLives, LoadedConfig.Local.TIME_TO_REGAIN_LIFE, LoadedConfig.Local.CREATIVE_RESETS_LIFE_COUNT, LoadedConfig.Local.USE_REALTIME_REGAIN, LoadedConfig.Local.ALWAYS_RENDER_HARDCORE_HEARTS);
    }

    public static void updateTimeToRegainLife(int timeToRegainLife)
    {
        updateConfig(LoadedConfig.Local.MAX_LIVES, timeToRegainLife, LoadedConfig.Local.CREATIVE_RESETS_LIFE_COUNT, LoadedConfig.Local.USE_REALTIME_REGAIN, LoadedConfig.Local.ALWAYS_RENDER_HARDCORE_HEARTS);
    }

    public static void updateCreativeResetsLifeCount(boolean creativeResetsLifeCount)
    {
        updateConfig(LoadedConfig.Local.MAX_LIVES, LoadedConfig.Local.TIME_TO_REGAIN_LIFE, creativeResetsLifeCount, LoadedConfig.Local.USE_REALTIME_REGAIN, LoadedConfig.Local.ALWAYS_RENDER_HARDCORE_HEARTS);
    }

    public static void updateUseRealtimeRegain(boolean useRealtimeRegain)
    {
        updateConfig(LoadedConfig.Local.MAX_LIVES, LoadedConfig.Local.TIME_TO_REGAIN_LIFE, LoadedConfig.Local.CREATIVE_RESETS_LIFE_COUNT, useRealtimeRegain, LoadedConfig.Local.ALWAYS_RENDER_HARDCORE_HEARTS);
    }

    public static void updateAlwaysRenderHardcoreHearts(boolean alwaysRenderHardcoreHearts)
    {
        updateConfig(LoadedConfig.Local.MAX_LIVES, LoadedConfig.Local.TIME_TO_REGAIN_LIFE, LoadedConfig.Local.CREATIVE_RESETS_LIFE_COUNT, LoadedConfig.Local.USE_REALTIME_REGAIN, alwaysRenderHardcoreHearts);
    }

    public static void updateConfig(int maxLives, int timeToRegainLife, boolean creativeResetsLifeCount, boolean useRealtimeRegain, boolean alwaysRenderHardcoreHearts)
    {
        NSHConfig newConfig = new NSHConfig(maxLives, timeToRegainLife, creativeResetsLifeCount, useRealtimeRegain, alwaysRenderHardcoreHearts);
        saveConfig(newConfig);
        NSHConfigLoader.initLocalAndServerConfig();
    }
    //Think about sending S2C sync after writing any config update !
    //-----------------------------------------------------------------
}
