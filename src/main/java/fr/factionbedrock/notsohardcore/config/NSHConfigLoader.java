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
    private static final Path CONFIG_FOLDER = FabricLoader.getInstance().getConfigDir().resolve(NotSoHardcore.MOD_ID);
    private static final Path CONFIG_PATH = CONFIG_FOLDER.resolve("config.json");

    public static NSHConfig loadConfig()
    {
        if (!Files.exists(CONFIG_PATH))
        {
            NSHConfig defaultConfig = new NSHConfig();
            saveConfig(defaultConfig);
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

    public static void saveConfig(NSHConfig config)
    {
        try
        {
            Files.createDirectories(CONFIG_FOLDER);
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {GSON.toJson(config, writer);}
        }
        catch (IOException e) {e.printStackTrace();}
    }
}
