package fr.factionbedrock.notsohardcore.mixin;

import fr.factionbedrock.notsohardcore.config.ServerLoadedConfig;
import fr.factionbedrock.notsohardcore.registry.NSHTrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerNbtMixin
{
    private static String lives = "lives";
    private static String life_regain_time_marker = "life_regain_time_marker";

    @Inject(at = @At("RETURN"), method = "readCustomDataFromNbt")
    private void read(NbtCompound nbt, CallbackInfo info)
    {
        PlayerEntity player = (PlayerEntity) (Object) this;
        player.getDataTracker().set(NSHTrackedData.LIVES, nbt.getInt(lives, ServerLoadedConfig.MAX_LIVES));
        player.getDataTracker().set(NSHTrackedData.LIFE_REGAIN_TIME_MARKER, nbt.getLong(life_regain_time_marker, 0));
    }

    @Inject(at = @At("RETURN"), method = "writeCustomDataToNbt")
    private void write(NbtCompound nbt, CallbackInfo info)
    {
        PlayerEntity player = (PlayerEntity) (Object) this;
        nbt.putInt(lives, player.getDataTracker().get(NSHTrackedData.LIVES));
        nbt.putLong(life_regain_time_marker, player.getDataTracker().get(NSHTrackedData.LIFE_REGAIN_TIME_MARKER));
    }
}
