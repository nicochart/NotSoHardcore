package fr.factionbedrock.notsohardcore.mixin;

import fr.factionbedrock.notsohardcore.config.LoadedConfig;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Gui.class)
public abstract class InGameHudMixin
{
    @ModifyVariable(method = "extractHearts", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/storage/LevelData;isHardcore()Z"), ordinal = 1)
    private boolean forceHardcoreHearts(boolean original)
    {
        return LoadedConfig.Server.ALWAYS_RENDER_HARDCORE_HEARTS || original;
    }
}