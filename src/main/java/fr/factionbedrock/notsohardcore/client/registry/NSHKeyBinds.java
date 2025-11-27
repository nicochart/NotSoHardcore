package fr.factionbedrock.notsohardcore.client.registry;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.client.gui.InfoScreen;
import fr.factionbedrock.notsohardcore.config.LoadedConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class NSHKeyBinds
{
    public static final KeyBinding TEST_ABILITY_KEY = new KeyBinding(
            "key."+ NotSoHardcore.MOD_ID+".ability",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            KeyBinding.Category.MISC
    );

    public static void registerKeybinds()
    {
        KeyBindingHelper.registerKeyBinding(TEST_ABILITY_KEY);
    }

    public static void registerPressedInteractions()
    {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TEST_ABILITY_KEY.wasPressed()) {
                if (client.player != null)
                {
                    client.setScreen(new InfoScreen(client.player, LoadedConfig.Server.MAX_LIVES, LoadedConfig.Server.TIME_TO_REGAIN_LIFE, LoadedConfig.Server.USE_REALTIME_REGAIN));
                }
            }
        });
    }
}
