package fr.factionbedrock.notsohardcore.client.registry;

import com.mojang.blaze3d.platform.InputConstants;
import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.client.gui.InfoScreen;
import fr.factionbedrock.notsohardcore.config.LoadedConfig;
import fr.factionbedrock.notsohardcore.registry.NSHKeyBinding;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class NSHKeyBinds
{
    public static final KeyMapping TEST_ABILITY_KEY = new KeyMapping(
            "key."+ NotSoHardcore.MOD_ID+".ability",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            NSHKeyBinding.NOT_SO_HARDCORE_CATEGORY
    );

    public static void registerKeybinds()
    {
        KeyMappingHelper.registerKeyMapping(TEST_ABILITY_KEY);
    }

    public static void registerPressedInteractions()
    {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TEST_ABILITY_KEY.consumeClick()) {
                if (client.player != null)
                {
                    client.setScreen(new InfoScreen(client.player, LoadedConfig.Server.MAX_LIVES, LoadedConfig.Server.TIME_TO_REGAIN_LIFE, LoadedConfig.Server.USE_REALTIME_REGAIN));
                }
            }
        });
    }
}
