package fr.factionbedrock.notsohardcore.client.registry;

import com.mojang.blaze3d.platform.InputConstants;
import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.client.gui.InfoScreen;
import fr.factionbedrock.notsohardcore.config.LoadedConfig;
import fr.factionbedrock.notsohardcore.registry.NSHKeyBinding;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class NSHKeyBinds
{
    public static final KeyMapping INFO_MENU_KEY = new KeyMapping(
            "key."+ NotSoHardcore.MOD_ID+".ability",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_H,
            NSHKeyBinding.NOT_SO_HARDCORE_CATEGORY
    );

    public static void registerKeybinds(IEventBus modEventBus)
    {
        modEventBus.addListener((RegisterKeyMappingsEvent event) -> event.register(INFO_MENU_KEY));
    }

    public static void registerPressedInteractions()
    {
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            if (event.phase != TickEvent.Phase.END) return;
            Minecraft client = Minecraft.getInstance();
            while (INFO_MENU_KEY.consumeClick()) {
                if (client.player != null)
                {
                    client.setScreen(new InfoScreen(client.player, LoadedConfig.Server.MAX_LIVES, LoadedConfig.Server.TIME_TO_REGAIN_LIFE, LoadedConfig.Server.USE_REALTIME_REGAIN));
                }
            }
        });
    }
}
