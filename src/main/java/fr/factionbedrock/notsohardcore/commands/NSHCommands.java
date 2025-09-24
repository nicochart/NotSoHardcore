package fr.factionbedrock.notsohardcore.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.config.NSHConfigLoader;
import fr.factionbedrock.notsohardcore.config.ServerLoadedConfig;
import fr.factionbedrock.notsohardcore.packet.NSHS2CSynchData;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import fr.factionbedrock.notsohardcore.util.NSHHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class NSHCommands
{
    private NSHCommands() {}

    public static void register() {CommandRegistrationCallback.EVENT.register(NSHCommands::registerImpl);}

    private static void registerImpl(CommandDispatcher<ServerCommandSource> dispatcher, net.minecraft.registry.RegistryWrapper.WrapperLookup registryAccess, CommandManager.RegistrationEnvironment env) {
        dispatcher.register(
                literal("nsh").requires(src -> src.hasPermissionLevel(2))
                        .then(literal("useRealtime")
                                .then(argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            boolean value = BoolArgumentType.getBool(ctx, "value");
                                            NotSoHardcore.CONFIG.useRealtimeRegain = value;
                                            applyAndSave(ctx.getSource());
                                            ctx.getSource().sendFeedback(() -> Text.literal("useRealtimeRegain = " + value), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(literal("maxLives")
                                .then(argument("value", IntegerArgumentType.integer(1, 1000))
                                        .executes(ctx -> {
                                            int value = IntegerArgumentType.getInteger(ctx, "value");
                                            NotSoHardcore.CONFIG.maxLives = value;
                                            applyAndSave(ctx.getSource());
                                            clampAllPlayersLives(ctx.getSource(), value);
                                            ctx.getSource().sendFeedback(() -> Text.literal("maxLives = " + value), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(literal("cooldown")
                                .then(cooldownArgument("ticks", 1))
                                .then(cooldownArgument("seconds", 20))
                                .then(cooldownArgument("minutes", 1200))
                                .then(cooldownArgument("hours", 72000))
                                .then(cooldownArgument("days", 1728000))
                        )
                        .then(literal("show").executes(ctx -> {
                            ctx.getSource().sendFeedback(() -> Text.literal("maxLives = " + NotSoHardcore.MAX_LIVES), false);
                            ctx.getSource().sendFeedback(() -> Text.literal("useRealtimeRegain = " + ServerLoadedConfig.USE_REALTIME_REGAIN), false);
                            ctx.getSource().sendFeedback(() -> Text.literal("timeToRegainLife = " + ServerLoadedConfig.TIME_TO_REGAIN_LIFE + " ticks = " + NSHHelper.getTimeStringFromTicks(ServerLoadedConfig.TIME_TO_REGAIN_LIFE)), false);
                            return 1;
                        }))
        );
    }

    private static ArgumentBuilder<ServerCommandSource, ?> cooldownArgument(String timeUnit, int multiplierToTick)
    {
        return literal(timeUnit)
                .then(argument("value", IntegerArgumentType.integer(1))
                        .executes(ctx -> {
                            int value = IntegerArgumentType.getInteger(ctx, "value");
                            NotSoHardcore.CONFIG.timeToRegainLife = value * multiplierToTick;
                            applyAndSave(ctx.getSource());
                            ctx.getSource().sendFeedback(() -> Text.literal("timeToRegainLife = " + value + " " + timeUnit +" = " + NSHHelper.getTimeStringFromTicks((long) value * multiplierToTick)), true);
                            return 1;
                        })
                );
    }

    private static void applyAndSave(ServerCommandSource src)
    {
        // Recompute effective values from CONFIG
        NotSoHardcore.MAX_LIVES = Math.max(NotSoHardcore.CONFIG.maxLives, 1);
        NotSoHardcore.TIME_TO_REGAIN_LIFE = NotSoHardcore.CONFIG.timeToRegainLife >= 0 ? NotSoHardcore.CONFIG.timeToRegainLife : Integer.MAX_VALUE;
        NotSoHardcore.CREATIVE_RESETS_LIFE_COUNT = NotSoHardcore.CONFIG.creativeResetsLifeCount;
        NotSoHardcore.USE_REALTIME_REGAIN = NotSoHardcore.CONFIG.useRealtimeRegain;

        // Persist to disk
        NSHConfigLoader.saveConfig(NotSoHardcore.CONFIG);

        // Update server-side store
        ServerLoadedConfig.storeServerParams(
                NotSoHardcore.MAX_LIVES,
                NotSoHardcore.TIME_TO_REGAIN_LIFE,
                NotSoHardcore.CREATIVE_RESETS_LIFE_COUNT,
                NotSoHardcore.USE_REALTIME_REGAIN
        );

        // Broadcast new values to players
        if (src.getServer() != null)
        {
            for (ServerPlayerEntity p : src.getServer().getPlayerManager().getPlayerList())
            {
                ServerPlayNetworking.send(p, new NSHS2CSynchData(
                        "sync_nsh_data",
                        NotSoHardcore.MAX_LIVES,
                        NotSoHardcore.TIME_TO_REGAIN_LIFE,
                        NotSoHardcore.CREATIVE_RESETS_LIFE_COUNT,
                        p.getDataTracker().get(NSHTrackedData.LIVES),
                        p.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_TICK_MARKER),
                        NotSoHardcore.USE_REALTIME_REGAIN,
                        p.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER)
                ));
            }
        }
    }

    private static void clampAllPlayersLives(ServerCommandSource src, int maxLives)
    {
        if (src.getServer() == null) return;
        for (ServerPlayerEntity p : src.getServer().getPlayerManager().getPlayerList())
        {
            int lives = p.getDataTracker().get(NSHTrackedData.LIVES);
            if (lives > maxLives) {p.getDataTracker().set(NSHTrackedData.LIVES, maxLives);}
        }
    }
}

