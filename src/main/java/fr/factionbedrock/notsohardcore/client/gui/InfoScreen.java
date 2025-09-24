package fr.factionbedrock.notsohardcore.client.gui;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import fr.factionbedrock.notsohardcore.util.NSHHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class InfoScreen extends Screen
{
    private final PlayerEntity player;
    private final int max_lives;
    private final int time_to_regain_life;
    private final boolean use_realtime;

    public InfoScreen(PlayerEntity player, int max_lives, int time_to_regain_life, boolean use_realtime)
    {
        super(Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen").formatted(Formatting.BOLD));
        this.player = player;
        this.max_lives = max_lives;
        this.time_to_regain_life = time_to_regain_life;
        this.use_realtime = use_realtime;
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
            String timeUntilNextRegainString = this.time_to_regain_life != Integer.MAX_VALUE ? NSHHelper.getTimeStringFromTicks(this.getTicksCountToRegainLife(player)) : "∞";

            context.drawCenteredTextWithShadow(this.textRenderer, timeUntilNextRegainString, this.width / 2, text_height, 0xFFFFFF);
             
        }

        text_height+=20;
        if (this.time_to_regain_life != Integer.MAX_VALUE)
        {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.time_to_regain_life"), this.width / 2, text_height, 0xFFFFFF);
            text_height += 10;
            context.drawCenteredTextWithShadow(this.textRenderer, NSHHelper.getTimeStringFromTicks(this.time_to_regain_life), this.width / 2, text_height, 0xFFFFFF);
        }
        else
        {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.no_life_regain_over_time"), this.width / 2, text_height, 0xFFFFFF);
        }
    }

    @Override public boolean shouldPause() {return false;}

    private long getTicksCountToRegainLife(PlayerEntity player)
    {
        //dividing by 50 turns milliseconds into ticks
        long now = NSHHelper.getCurrentTime(player, this.use_realtime);
        long marker = NSHHelper.getLiveRegainTimeMarker(player, this.use_realtime);
        long ticksToRegain = this.time_to_regain_life - (now - marker);
        return ticksToRegain;
    }
}
