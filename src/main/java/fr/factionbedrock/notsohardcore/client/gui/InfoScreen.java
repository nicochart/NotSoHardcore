package fr.factionbedrock.notsohardcore.client.gui;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.config.ServerLoadedConfig;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import fr.factionbedrock.notsohardcore.config.ServerLoadedConfig;

public class InfoScreen extends Screen
{
    private final PlayerEntity player;
    private final int max_lives;
    private final int time_to_regain_life;

    public InfoScreen(PlayerEntity player, int max_lives, int time_to_regain_life)
    {
        super(Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen").formatted(Formatting.BOLD));
        this.player = player;
        this.max_lives = max_lives;
        this.time_to_regain_life = time_to_regain_life;
    }

    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {
        this.renderBackground(context, mouseX, mouseY, delta);

        int text_height = 20;
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, text_height, 0xFFFFFF);
        text_height+=30;
        context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.remaining_lives"), this.width / 2, text_height, 0xFF0000);

        text_height+=10;
        if (this.max_lives < 21)
        {
            int centerX = this.width / 2;
            int heartWidth = 7;
            int totalWidth = heartWidth * this.max_lives;
            int startX = centerX - totalWidth / 2;
            for (int i = 0; i < this.max_lives; i++)
            {
                int color = (i < player.getDataTracker().get(NSHTrackedData.LIVES)) ? 0xFF0000 : 0x202020;
                context.drawTextWithShadow(this.textRenderer, "♥", startX + i * heartWidth, text_height, color);
            }
        }
        else
        {
            context.drawCenteredTextWithShadow(this.textRenderer, player.getDataTracker().get(NSHTrackedData.LIVES) + " / " + this.max_lives , this.width / 2, text_height, 0xFF0000);
        }


        if (player.getDataTracker().get(NSHTrackedData.LIVES) < this.max_lives)
        {
            text_height+=20;
            context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.time_until_life_regain"), this.width / 2, text_height, 0xFFFFFF);

            text_height+=10;
            String timeUntilNextRegainString;
            if (!ServerLoadedConfig.USE_REALTIME_REGAIN)
            {
                long ticksToRegain = this.time_to_regain_life - (player.getWorld().getTime() - player.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_TIME_MARKER));
                timeUntilNextRegainString = this.time_to_regain_life != Integer.MAX_VALUE ? getTimeRemainingStringFromTicks(ticksToRegain) : "∞";
            }
            else
            {
                long now = System.currentTimeMillis();
                long marker = player.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER);
                long secondsRemaining = (long) ServerLoadedConfig.TIME_TO_REGAIN_LIFE_SECONDS - ((now - marker) / 1000L);
                timeUntilNextRegainString = ServerLoadedConfig.TIME_TO_REGAIN_LIFE_SECONDS != Integer.MAX_VALUE ? formatSeconds(Math.max(0, secondsRemaining)) : "∞";
            }

            context.drawCenteredTextWithShadow(this.textRenderer, timeUntilNextRegainString, this.width / 2, text_height, 0xFFFFFF);
             
        }

        text_height+=20;
        if (ServerLoadedConfig.USE_REALTIME_REGAIN)
        {
            context.drawCenteredTextWithShadow(this.textRenderer,
            Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.time_to_regain_life"),
            this.width / 2, text_height, 0xFFFFFF);
            text_height += 10;

            if (ServerLoadedConfig.TIME_TO_REGAIN_LIFE_SECONDS != Integer.MAX_VALUE)
            {
                context.drawCenteredTextWithShadow(this.textRenderer,
                formatSeconds(ServerLoadedConfig.TIME_TO_REGAIN_LIFE_SECONDS),
                this.width / 2, text_height, 0xFFFFFF);
            }
            else 
            {
                context.drawCenteredTextWithShadow(this.textRenderer,
                Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.no_life_regain_over_time"),
                this.width / 2, text_height, 0xFFFFFF);
            }
        } else
        {
            if (this.time_to_regain_life != Integer.MAX_VALUE)
            {
                context.drawCenteredTextWithShadow(this.textRenderer,
                Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.time_to_regain_life"),
                this.width / 2, text_height, 0xFFFFFF);
                text_height += 10;
                
                context.drawCenteredTextWithShadow(this.textRenderer,
                getTimeRemainingStringFromTicks(this.time_to_regain_life),
                this.width / 2, text_height, 0xFFFFFF);
            }else {
                context.drawCenteredTextWithShadow(this.textRenderer,
                Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.no_life_regain_over_time"),
                this.width / 2, text_height, 0xFFFFFF);
            }
        }

    }

    @Override public boolean shouldPause() {return false;}

    private static String getTimeRemainingStringFromTicks(long ticksToRegain)
    {
        int secondsToRegain = (int) (ticksToRegain / 20);
        long days = secondsToRegain / 86400;
        long hours = (secondsToRegain % 86400) / 3600;
        long minutes = (secondsToRegain % 3600) / 60;
        long seconds = secondsToRegain % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0 || days > 0) sb.append(hours).append("h ");
        if (minutes > 0 || hours > 0 || days > 0) sb.append(minutes).append("m ");
        sb.append(seconds).append("s");

        return sb.toString().trim();
    }

    private static String formatSeconds(long secondsToRegain) {
    long days = secondsToRegain / 86400;
    long hours = (secondsToRegain % 86400) / 3600;
    long minutes = (secondsToRegain % 3600) / 60;
    long seconds = secondsToRegain % 60;
    StringBuilder sb = new StringBuilder();
    if (days > 0) sb.append(days).append("d ");
    if (hours > 0 || days > 0) sb.append(hours).append("h ");
    if (minutes > 0 || hours > 0 || days > 0) sb.append(minutes).append("m ");
    sb.append(seconds).append("s");
    return sb.toString().trim();
    }
}
