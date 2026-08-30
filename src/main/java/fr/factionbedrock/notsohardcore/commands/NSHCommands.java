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
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.level.GameType;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class NSHCommands
{
    private NSHCommands() {}

    public static void register() {CommandRegistrationCallback.EVENT.register(NSHCommands::registerImpl);}

    private static void registerImpl(CommandDispatcher<CommandSourceStack> dispatcher, net.minecraft.core.HolderLookup.Provider registryAccess, Commands.CommandSelection env) {
        dispatcher.register(
                literal("nsh").requires(src -> src.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                        .then(literal("useRealtime")
                                .then(argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            boolean value = BoolArgumentType.getBool(ctx, "value");
                                            NSHConfigSaver.updateUseRealtimeRegain(value);
                                            NSHNetworking.sendS2CSync(ctx.getSource().getServer());
                                            ctx.getSource().sendSuccess(() -> Component.literal("useRealtimeRegain = " + value), true);
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
                                            ctx.getSource().sendSuccess(() -> Component.literal("creativeResetsLifeCount = " + value), true);
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
                                            ctx.getSource().sendSuccess(() -> Component.literal("alwaysRenderHardcoreHearts = " + value), true);
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
                                            ctx.getSource().sendSuccess(() -> Component.literal("maxLives = " + value), true);
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
                            ctx.getSource().sendSuccess(() -> Component.literal("maxLives = " + LoadedConfig.Server.MAX_LIVES), false);
                            ctx.getSource().sendSuccess(() -> Component.literal("useRealtimeRegain = " + LoadedConfig.Server.USE_REALTIME_REGAIN), false);
                            ctx.getSource().sendSuccess(() -> Component.literal("timeToRegainLife = " + LoadedConfig.Server.TIME_TO_REGAIN_LIFE + " ticks = " + NSHHelper.getTimeStringFromTicks(LoadedConfig.Server.TIME_TO_REGAIN_LIFE)), false);
                            ctx.getSource().sendSuccess(() -> Component.literal("creativeResetsLifeCount = " + LoadedConfig.Server.CREATIVE_RESETS_LIFE_COUNT), false);
                            ctx.getSource().sendSuccess(() -> Component.literal("alwaysRenderHardcoreHearts = " + LoadedConfig.Server.ALWAYS_RENDER_HARDCORE_HEARTS), false);
                            return 1;
                        }))
                        .then(literal("respawn")
                            .then(argument("player", EntityArgument.player())
                                .executes(context ->
                                {
                                    ServerPlayer target = EntityArgument.getPlayer(context, "player");
                                    if (target.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) {throw new SimpleCommandExceptionType(Component.translatable("commands."+ NotSoHardcore.MOD_ID+".respawn.already_alive")).create();}
                                    NSHHelper.forceRespawnPlayer(target);
                                    return 1;
                                })
                            )
                        )
        );
    }

    private static ArgumentBuilder<CommandSourceStack, ?> cooldownArgument(String timeUnit, int multiplierToTick)
    {
        return literal(timeUnit)
                .then(argument("value", IntegerArgumentType.integer(1))
                        .executes(ctx -> {
                            int value = IntegerArgumentType.getInteger(ctx, "value");
                            NSHConfigSaver.updateTimeToRegainLife(value * multiplierToTick);
                            NSHNetworking.sendS2CSync(ctx.getSource().getServer());
                            ctx.getSource().sendSuccess(() -> Component.literal("timeToRegainLife = " + value + " " + timeUnit +" = " + NSHHelper.getTimeStringFromTicks((long) value * multiplierToTick)), true);
                            return 1;
                        })
                );
    }

    private static void clampAllPlayersLives(CommandSourceStack src, int maxLives)
    {
        if (src.getServer() == null) return;
        for (ServerPlayer player : src.getServer().getPlayerList().getPlayers())
        {
            int lives = player.getEntityData().get(NSHTrackedData.LIVES);
            if (lives > maxLives) {player.getEntityData().set(NSHTrackedData.LIVES, maxLives);}
        }
    }
}

