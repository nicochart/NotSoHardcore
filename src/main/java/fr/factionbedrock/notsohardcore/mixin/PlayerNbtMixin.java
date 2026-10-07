package fr.factionbedrock.notsohardcore.mixin;

import fr.factionbedrock.notsohardcore.config.LoadedConfig;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerNbtMixin
{
    private static String lives = "lives";
    private static String life_regain_tick_marker = "life_regain_tick_marker";
    private static String life_regain_realtime_marker = "life_regain_realtime_marker";

    @Inject(at = @At("RETURN"), method = "readAdditionalSaveData")
    private void read(CompoundTag view, CallbackInfo info)
    {
        Player player = (Player) (Object) this;
        player.getEntityData().set(NSHTrackedData.LIVES, view.contains(lives, Tag.TAG_ANY_NUMERIC) ? view.getInt(lives) : LoadedConfig.Server.MAX_LIVES);
        player.getEntityData().set(NSHTrackedData.LIFE_REGAIN_TICK_MARKER, view.contains(life_regain_tick_marker, Tag.TAG_ANY_NUMERIC) ? view.getLong(life_regain_tick_marker) : 0);
        player.getEntityData().set(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER, view.contains(life_regain_realtime_marker, Tag.TAG_ANY_NUMERIC) ? view.getLong(life_regain_realtime_marker) : 0);
    }

    @Inject(at = @At("RETURN"), method = "addAdditionalSaveData")
    private void write(CompoundTag view, CallbackInfo info)
    {
        Player player = (Player) (Object) this;
        view.putInt(lives, player.getEntityData().get(NSHTrackedData.LIVES));
        view.putLong(life_regain_tick_marker, player.getEntityData().get(NSHTrackedData.LIFE_REGAIN_TICK_MARKER));
        view.putLong(life_regain_realtime_marker, player.getEntityData().get(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER));
    }
}
