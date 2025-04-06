package fr.factionbedrock.notsohardcore.mixin;

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
		builder.add(NSHTrackedData.LIVES, 3);
		builder.add(NSHTrackedData.LIVE_REGAIN_TIME_MARKER, (long)0);
	}
}