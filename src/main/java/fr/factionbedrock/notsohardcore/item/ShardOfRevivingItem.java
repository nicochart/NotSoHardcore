package fr.factionbedrock.notsohardcore.item;

import fr.factionbedrock.notsohardcore.NotSoHardcore;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class ShardOfRevivingItem extends Item
{
    public ShardOfRevivingItem(Settings settings) {super(settings);}

    @Override public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user)
    {
        if (user instanceof PlayerEntity player)
        {
            int lives = player.getDataTracker().get(NSHTrackedData.LIVES);
            if (lives < NotSoHardcore.MAX_LIVES)
            {
                player.getDataTracker().set(NSHTrackedData.LIVES, lives+1);
            }
        }
        return super.finishUsing(stack, world, user);
    }

    @Override public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type)
    {
        tooltip.add(this.getDescription().formatted(Formatting.GRAY));
    }

    public MutableText getDescription()
    {
        return Text.translatable(this.getTranslationKey() + ".desc");
    }
}
