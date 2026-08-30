package fr.factionbedrock.notsohardcore.registry;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;

public class NSHTrackedData
{
    public static final EntityDataAccessor<Integer> LIVES = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Long> LIFE_REGAIN_TICK_MARKER = SynchedEntityData.defineId(Player.class, EntityDataSerializers.LONG);
    public static final EntityDataAccessor<Long> LIFE_REGAIN_REALTIME_MARKER = SynchedEntityData.defineId(Player.class, EntityDataSerializers.LONG);
    
    public static void load() {}
}
