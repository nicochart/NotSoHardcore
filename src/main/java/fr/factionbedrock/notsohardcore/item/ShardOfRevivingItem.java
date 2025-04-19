package fr.factionbedrock.notsohardcore.item;

import fr.factionbedrock.notsohardcore.config.ServerLoadedConfig;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

public class ShardOfRevivingItem extends Item
{
    public ShardOfRevivingItem(Settings settings) {super(settings);}

    @Override public ActionResult use(World world, PlayerEntity user, Hand hand)
    {
        if (user.getDataTracker().get(NSHTrackedData.LIVES) < ServerLoadedConfig.MAX_LIVES)
        {
            return super.use(world, user, hand);
        }
        else {return ActionResult.PASS;}
    }

    @Override public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user)
    {
        if (user instanceof PlayerEntity player)
        {
            int lives = player.getDataTracker().get(NSHTrackedData.LIVES);
            if (lives < ServerLoadedConfig.MAX_LIVES)
            {
                player.getDataTracker().set(NSHTrackedData.LIVES, lives+1);
            }
        }
        return super.finishUsing(stack, world, user);
    }

    @Override public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type)
    {
        textConsumer.accept(this.getDescription().formatted(Formatting.GRAY));
    }

    public MutableText getDescription()
    {
        return Text.translatable(this.getTranslationKey() + ".desc");
    }
}
