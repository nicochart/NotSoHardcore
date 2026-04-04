package fr.factionbedrock.notsohardcore.mixin;

import fr.factionbedrock.notsohardcore.config.LoadedConfig;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerDataTrackerMixin
{
	@Inject(at = @At("RETURN"), method = "initDataTracker")
	private void init(DataTracker.Builder builder, CallbackInfo info)
	{
		builder.add(NSHTrackedData.LIVES, LoadedConfig.Local.MAX_LIVES);
		builder.add(NSHTrackedData.LIFE_REGAIN_TICK_MARKER, (long)0);
		builder.add(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER, 0L);
	}
}