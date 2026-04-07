package fr.factionbedrock.notsohardcore.client.gui;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import fr.factionbedrock.notsohardcore.util.NSHHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.player.Player;

public class InfoScreen extends Screen
{
    private final Player player;
    private final int max_lives;
    private final int time_to_regain_life;
    private final boolean use_realtime;

    public InfoScreen(Player player, int max_lives, int time_to_regain_life, boolean use_realtime)
    {
        super(Component.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen").withStyle(ChatFormatting.BOLD));
        this.player = player;
        this.max_lives = max_lives;
        this.time_to_regain_life = time_to_regain_life;
        this.use_realtime = use_realtime;
    }

    @Override public void render(GuiGraphics context, int mouseX, int mouseY, float delta)
    {
        int text_height = 20;
        context.drawCenteredString(this.font, this.title, this.width / 2, text_height, CommonColors.WHITE);
        text_height+=30;
        context.drawCenteredString(this.font, Component.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.remaining_lives"), this.width / 2, text_height, CommonColors.RED);

        text_height+=10;
        if (this.max_lives < 21)
        {
            int centerX = this.width / 2;
            int heartWidth = 7;
            int totalWidth = heartWidth * this.max_lives;
            int startX = centerX - totalWidth / 2;
            for (int i = 0; i < this.max_lives; i++)
            {
                int color = (i < player.getEntityData().get(NSHTrackedData.LIVES)) ? CommonColors.RED : CommonColors.DARK_GRAY;
                context.drawString(this.font, "♥", startX + i * heartWidth, text_height, color);
            }
        }
        else
        {
            context.drawCenteredString(this.font, player.getEntityData().get(NSHTrackedData.LIVES) + " / " + this.max_lives , this.width / 2, text_height, CommonColors.RED);
        }


        if (player.getEntityData().get(NSHTrackedData.LIVES) < this.max_lives)
        {
            text_height+=20;
            context.drawCenteredString(this.font, Component.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.time_until_life_regain"), this.width / 2, text_height, CommonColors.WHITE);

            text_height+=10;
            String timeUntilNextRegainString = this.time_to_regain_life != Integer.MAX_VALUE ? NSHHelper.getTimeStringFromTicks(this.getTicksCountToRegainLife(player)) : "∞";

            context.drawCenteredString(this.font, timeUntilNextRegainString, this.width / 2, text_height, CommonColors.WHITE);
             
        }

        text_height+=20;
        if (this.time_to_regain_life != Integer.MAX_VALUE)
        {
            context.drawCenteredString(this.font, Component.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.time_to_regain_life"), this.width / 2, text_height, CommonColors.WHITE);
            text_height += 10;
            context.drawCenteredString(this.font, NSHHelper.getTimeStringFromTicks(this.time_to_regain_life), this.width / 2, text_height, CommonColors.WHITE);
        }
        else
        {
            context.drawCenteredString(this.font, Component.translatable("gui."+NotSoHardcore.MOD_ID+".info_screen.no_life_regain_over_time"), this.width / 2, text_height, CommonColors.WHITE);
        }
    }

    @Override public boolean isPauseScreen() {return false;}

    private long getTicksCountToRegainLife(Player player)
    {
        //dividing by 50 turns milliseconds into ticks
        long now = NSHHelper.getCurrentTime(player, this.use_realtime);
        long marker = NSHHelper.getLiveRegainTimeMarker(player, this.use_realtime);
        long ticksToRegain = this.time_to_regain_life - (now - marker);
        return ticksToRegain;
    }
}
