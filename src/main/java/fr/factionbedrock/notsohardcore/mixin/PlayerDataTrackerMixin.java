package fr.factionbedrock.notsohardcore.mixin;

import fr.factionbedrock.notsohardcore.config.LoadedConfig;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerDataTrackerMixin
{
	@Inject(at = @At("RETURN"), method = "defineSynchedData")
	private void init(SynchedEntityData.Builder builder, CallbackInfo info)
	{
		builder.define(NSHTrackedData.LIVES, LoadedConfig.Local.MAX_LIVES);
		builder.define(NSHTrackedData.LIFE_REGAIN_TICK_MARKER, (long)0);
		builder.define(NSHTrackedData.LIFE_REGAIN_REALTIME_MARKER, 0L);
	}
}