package fr.factionbedrock.notsohardcore.item;

import fr.factionbedrock.notsohardcore.config.LoadedConfig;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

public class ShardOfRevivingItem extends Item
{
    public ShardOfRevivingItem(Properties settings) {super(settings);}

    @Override public InteractionResult use(Level world, Player user, InteractionHand hand)
    {
        if (user.getEntityData().get(NSHTrackedData.LIVES) < LoadedConfig.Server.MAX_LIVES)
        {
            return super.use(world, user, hand);
        }
        else {return InteractionResult.PASS;}
    }

    @Override public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user)
    {
        if (user instanceof Player player)
        {
            int lives = player.getEntityData().get(NSHTrackedData.LIVES);
            if (lives < LoadedConfig.Server.MAX_LIVES)
            {
                player.getEntityData().set(NSHTrackedData.LIVES, lives+1);
            }
        }
        return super.finishUsingItem(stack, world, user);
    }

    @Override public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type)
    {
        textConsumer.accept(this.getDescription().withStyle(ChatFormatting.GRAY));
    }

    public MutableComponent getDescription()
    {
        return Component.translatable(this.getDescriptionId() + ".desc");
    }
}
