package fr.factionbedrock.notsohardcore.mixin;

import fr.factionbedrock.notsohardcore.config.LoadedConfig;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
    private void read(ValueInput view, CallbackInfo info)
    {
        Player player = (Player) (Object) this;
        player.getEntityData().set(NSHTrackedData.LIVES, view.getIntOr(lives, LoadedConfig.Server.MAX_LIVES));
        player.getEntityData().set(NSHTrackedData.LIFE_REGAIN_TICK_MARKER, view.getLongOr(life_regain_tick_marker, 0));
        player.getEntityData().set(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER, view.getLongOr(life_regain_realtime_marker, 0));
    }

    @Inject(at = @At("RETURN"), method = "addAdditionalSaveData")
    private void write(ValueOutput view, CallbackInfo info)
    {
        Player player = (Player) (Object) this;
        view.putInt(lives, player.getEntityData().get(NSHTrackedData.LIVES));
        view.putLong(life_regain_tick_marker, player.getEntityData().get(NSHTrackedData.LIFE_REGAIN_TICK_MARKER));
        view.putLong(life_regain_realtime_marker, player.getEntityData().get(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER));
    }
}
