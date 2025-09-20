package fr.factionbedrock.notsohardcore.registry;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;

public class NSHTrackedData
{
    public static final TrackedData<Integer> LIVES = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Long> LIFE_REGAIN_TIME_MARKER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.LONG);
    public static final TrackedData<Long> LIFE_REGAIN_REALTIME_MARKER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.LONG);
    
    public static void load() {}
}
