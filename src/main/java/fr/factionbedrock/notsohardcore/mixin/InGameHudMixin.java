package fr.factionbedrock.notsohardcore.mixin;

import fr.factionbedrock.notsohardcore.config.LoadedConfig;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.level.storage.LevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Gui.class)
public abstract class InGameHudMixin
{
    //In 1.20.1, "isHardcore" is not stored in a local variable in renderHearts, so the call is redirected instead
    @Redirect(method = "renderHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/LevelData;isHardcore()Z"))
    private boolean forceHardcoreHearts(LevelData levelData)
    {
        return LoadedConfig.Server.ALWAYS_RENDER_HARDCORE_HEARTS || levelData.isHardcore();
    }
}