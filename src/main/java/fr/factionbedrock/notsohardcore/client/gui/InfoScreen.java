package fr.factionbedrock.notsohardcore.client.gui;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class InfoScreen extends Screen
{
    private final PlayerEntity player;

    public InfoScreen(PlayerEntity player)
    {
        super(Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen").formatted(Formatting.BOLD));
        this.player = player;
    }

    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {
        this.renderBackground(context, mouseX, mouseY, delta);

        int text_height = 20;
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, text_height, 0xFFFFFF);
        text_height+=30;
        context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.remaining_lives"), this.width / 2, text_height, 0xFF0000);

        text_height+=10;
        if (NotSoHardcore.MAX_LIVES < 21)
        {
            int centerX = this.width / 2;
            int heartWidth = 7;
            int totalWidth = heartWidth * NotSoHardcore.MAX_LIVES;
            int startX = centerX - totalWidth / 2;
            for (int i = 0; i < NotSoHardcore.MAX_LIVES; i++)
            {
                int color = (i < player.getDataTracker().get(NSHTrackedData.LIVES)) ? 0xFF0000 : 0x202020;
                context.drawTextWithShadow(this.textRenderer, "♥", startX + i * heartWidth, text_height, color);
            }
        }
        else
        {
            context.drawCenteredTextWithShadow(this.textRenderer, player.getDataTracker().get(NSHTrackedData.LIVES) + " / " + NotSoHardcore.MAX_LIVES , this.width / 2, text_height, 0xFF0000);
        }


        if (player.getDataTracker().get(NSHTrackedData.LIVES) < NotSoHardcore.MAX_LIVES)
        {
            text_height+=20;
            context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.time_until_life_regain"), this.width / 2, text_height, 0xFFFFFF);

            text_height+=10;
            long ticksToRegain = NotSoHardcore.TIME_TO_REGAIN_LIVE - (player.getWorld().getTime() - player.getDataTracker().get(NSHTrackedData.LIVE_REGAIN_TIME_MARKER));
            String timeUntilNextRegainString = NotSoHardcore.TIME_TO_REGAIN_LIVE != Integer.MAX_VALUE ? getTimeRemainingStringFromTicks(ticksToRegain) : "∞";
            context.drawCenteredTextWithShadow(this.textRenderer, timeUntilNextRegainString, this.width / 2, text_height, 0xFFFFFF);
        }

        text_height+=20;
        if (NotSoHardcore.TIME_TO_REGAIN_LIVE != Integer.MAX_VALUE)
        {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.time_to_regain_life"), this.width / 2, text_height, 0xFFFFFF);
            text_height+=10;
            context.drawCenteredTextWithShadow(this.textRenderer, getTimeRemainingStringFromTicks(NotSoHardcore.TIME_TO_REGAIN_LIVE), this.width / 2, text_height, 0xFFFFFF);
        }
        else
        {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.no_life_regain_over_time"), this.width / 2, text_height, 0xFFFFFF);
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
}
