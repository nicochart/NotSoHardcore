package fr.factionbedrock.notsohardcore.client.gui;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class InfoScreen extends Screen
{
    private final PlayerEntity player;

    public InfoScreen(PlayerEntity player)
    {
        super(Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen"));
        this.player = player;
    }

    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {
        this.renderBackground(context, mouseX, mouseY, delta);

        StringBuilder hearts = new StringBuilder();
        int text_height = 20;
        for (int i = 0; i < player.getDataTracker().get(NSHTrackedData.LIVES); i++) {hearts.append("♥ ");}

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, text_height, 0xFFFFFF);
        text_height = 50;
        context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.remaining_lives"), this.width / 2, text_height, 0xFF0000);

        text_height = 60;
        int centerX = this.width / 2;
        int heartWidth = 7;
        int totalWidth = heartWidth * NotSoHardcore.MAX_LIVES;
        int startX = centerX - totalWidth / 2;
        for (int i = 0; i < NotSoHardcore.MAX_LIVES; i++)
        {
            int color = (i < player.getDataTracker().get(NSHTrackedData.LIVES)) ? 0xFF0000 : 0x202020;
            context.drawTextWithShadow(this.textRenderer, "♥", startX + i * heartWidth, text_height, color);
        }

        if (player.getDataTracker().get(NSHTrackedData.LIVES) < NotSoHardcore.MAX_LIVES)
        {
            text_height = 80;
            context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.time_until_life_regain"), this.width / 2, text_height, 0xFFFFFF);
            text_height = 90;
            long ticksToRegain = NotSoHardcore.TIME_TO_REGAIN_LIVE - (player.getWorld().getTime() - player.getDataTracker().get(NSHTrackedData.LIVE_REGAIN_TIME_MARKER));
            context.drawCenteredTextWithShadow(this.textRenderer, getTimeRemainingStringFromTicks(ticksToRegain), this.width / 2, text_height, 0xFFFFFF);

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
