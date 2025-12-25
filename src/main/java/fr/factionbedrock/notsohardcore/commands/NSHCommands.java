package fr.factionbedrock.notsohardcore.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.config.LoadedConfig;
import fr.factionbedrock.notsohardcore.config.NSHConfigSaver;
import fr.factionbedrock.notsohardcore.packet.NSHNetworking;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import fr.factionbedrock.notsohardcore.util.NSHHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

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
                                            NSHConfigSaver.updateUseRealtimeRegain(value);
                                            NSHNetworking.sendS2CSync(ctx.getSource().getServer());
                                            ctx.getSource().sendFeedback(() -> Text.literal("useRealtimeRegain = " + value), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(literal("creativeResetsLifeCount")
                                .then(argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            boolean value = BoolArgumentType.getBool(ctx, "value");
                                            NSHConfigSaver.updateCreativeResetsLifeCount(value);
                                            NSHNetworking.sendS2CSync(ctx.getSource().getServer());
                                            ctx.getSource().sendFeedback(() -> Text.literal("creativeResetsLifeCount = " + value), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(literal("alwaysRenderHardcoreHearts")
                                .then(argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            boolean value = BoolArgumentType.getBool(ctx, "value");
                                            NSHConfigSaver.updateAlwaysRenderHardcoreHearts(value);
                                            NSHNetworking.sendS2CSync(ctx.getSource().getServer());
                                            ctx.getSource().sendFeedback(() -> Text.literal("alwaysRenderHardcoreHearts = " + value), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(literal("maxLives")
                                .then(argument("value", IntegerArgumentType.integer(1, 1000))
                                        .executes(ctx -> {
                                            int value = IntegerArgumentType.getInteger(ctx, "value");
                                            NSHConfigSaver.updateMaxLives(value);
                                            NSHNetworking.sendS2CSync(ctx.getSource().getServer());
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
                            ctx.getSource().sendFeedback(() -> Text.literal("maxLives = " + LoadedConfig.Server.MAX_LIVES), false);
                            ctx.getSource().sendFeedback(() -> Text.literal("useRealtimeRegain = " + LoadedConfig.Server.USE_REALTIME_REGAIN), false);
                            ctx.getSource().sendFeedback(() -> Text.literal("timeToRegainLife = " + LoadedConfig.Server.TIME_TO_REGAIN_LIFE + " ticks = " + NSHHelper.getTimeStringFromTicks(LoadedConfig.Server.TIME_TO_REGAIN_LIFE)), false);
                            ctx.getSource().sendFeedback(() -> Text.literal("creativeResetsLifeCount = " + LoadedConfig.Server.CREATIVE_RESETS_LIFE_COUNT), false);
                            ctx.getSource().sendFeedback(() -> Text.literal("alwaysRenderHardcoreHearts = " + LoadedConfig.Server.ALWAYS_RENDER_HARDCORE_HEARTS), false);
                            return 1;
                        }))
                        .then(literal("respawn")
                            .then(argument("player", EntityArgumentType.player())
                                .executes(context ->
                                {
                                    ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
                                    if (target.interactionManager.getGameMode() != GameMode.SPECTATOR) {throw new SimpleCommandExceptionType(Text.translatable("commands."+ NotSoHardcore.MOD_ID+".respawn.already_alive")).create();}
                                    NSHHelper.forceRespawnPlayer(target);
                                    return 1;
                                })
                            )
                        )
        );
    }

    private static ArgumentBuilder<ServerCommandSource, ?> cooldownArgument(String timeUnit, int multiplierToTick)
    {
        return literal(timeUnit)
                .then(argument("value", IntegerArgumentType.integer(1))
                        .executes(ctx -> {
                            int value = IntegerArgumentType.getInteger(ctx, "value");
                            NSHConfigSaver.updateTimeToRegainLife(value * multiplierToTick);
                            NSHNetworking.sendS2CSync(ctx.getSource().getServer());
                            ctx.getSource().sendFeedback(() -> Text.literal("timeToRegainLife = " + value + " " + timeUnit +" = " + NSHHelper.getTimeStringFromTicks((long) value * multiplierToTick)), true);
                            return 1;
                        })
                );
    }

    private static void clampAllPlayersLives(ServerCommandSource src, int maxLives)
    {
        if (src.getServer() == null) return;
        for (ServerPlayerEntity player : src.getServer().getPlayerManager().getPlayerList())
        {
            int lives = player.getDataTracker().get(NSHTrackedData.LIVES);
            if (lives > maxLives) {player.getDataTracker().set(NSHTrackedData.LIVES, maxLives);}
        }
    }
}

