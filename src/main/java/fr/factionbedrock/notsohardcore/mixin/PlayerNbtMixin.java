package fr.factionbedrock.notsohardcore.mixin;

import fr.factionbedrock.notsohardcore.config.LoadedConfig;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerNbtMixin
{
    private static String lives = "lives";
    private static String life_regain_tick_marker = "life_regain_tick_marker";
    private static String life_regain_realtime_marker = "life_regain_realtime_marker";

    @Inject(at = @At("RETURN"), method = "readCustomData")
    private void read(ReadView view, CallbackInfo info)
    {
        PlayerEntity player = (PlayerEntity) (Object) this;
        player.getDataTracker().set(NSHTrackedData.LIVES, view.getInt(lives, LoadedConfig.Server.MAX_LIVES));
        player.getDataTracker().set(NSHTrackedData.LIFE_REGAIN_TICK_MARKER, view.getLong(life_regain_tick_marker, 0));
        player.getDataTracker().set(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER, view.getLong(life_regain_realtime_marker, 0));
    }

    @Inject(at = @At("RETURN"), method = "writeCustomData")
    private void write(WriteView view, CallbackInfo info)
    {
        PlayerEntity player = (PlayerEntity) (Object) this;
        view.putInt(lives, player.getDataTracker().get(NSHTrackedData.LIVES));
        view.putLong(life_regain_tick_marker, player.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_TICK_MARKER));
        view.putLong(life_regain_realtime_marker, player.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER));
    }
}
