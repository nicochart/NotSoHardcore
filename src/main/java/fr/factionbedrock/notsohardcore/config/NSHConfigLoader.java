package fr.factionbedrock.notsohardcore.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import fr.factionbedrock.notsohardcore.NotSoHardcore;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class NSHConfigLoader
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    protected static final Path CONFIG_FOLDER = FabricLoader.getInstance().getConfigDir().resolve(NotSoHardcore.MOD_ID);
    protected static final Path CONFIG_PATH = CONFIG_FOLDER.resolve("config.json");

    public static NSHConfig loadConfig()
    {
        if (!Files.exists(CONFIG_PATH))
        {
            NSHConfig defaultConfig = new NSHConfig();
            NSHConfigSaver.saveConfig(defaultConfig);
            return defaultConfig;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {return GSON.fromJson(reader, NSHConfig.class);}
        catch (IOException | JsonParseException e)
        {
            e.printStackTrace();
            NotSoHardcore.LOGGER.error("Failed to load Not So Hardcore config file, using default configuration instead");
            return new NSHConfig();
        }
    }

    //Sending a NSHS2CSynchData packet will be necessary after calling this (see NSHNetworking "sendS2CSync" methods)
    @Deprecated public static void initLocalAndServerConfig()
    {
        LoadedConfig.Local.initLocalLoadedConfig();
        LoadedConfig.Server.storeParams(LoadedConfig.Local.MAX_LIVES, LoadedConfig.Local.TIME_TO_REGAIN_LIFE, LoadedConfig.Local.CREATIVE_RESETS_LIFE_COUNT, LoadedConfig.Local.USE_REALTIME_REGAIN, LoadedConfig.Local.ALWAYS_RENDER_HARDCORE_HEARTS);
    }
}
