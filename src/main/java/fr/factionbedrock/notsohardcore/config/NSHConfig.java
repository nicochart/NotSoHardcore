package fr.factionbedrock.notsohardcore.config;

public class NSHConfig
{
    public int maxLives = 3; //max lives a player can get
    public int timeToRegainLife = 72000; //time to regain life (ticks)
    public boolean creativeResetsLifeCount = false; //true = if a player is in creative game-mode, his life count is reset to maxLives
    public boolean useRealtimeRegain = false; //true = realtime-based regain instead of in-game server ticks, solving offline regain problem in singleplayer

    public NSHConfig() {}
    public NSHConfig(int maxLives, int timeToRegainLife, boolean creativeResetsLifeCount, boolean useRealtimeRegain)
    {
        this.maxLives = maxLives;
        this.timeToRegainLife = timeToRegainLife;
        this.creativeResetsLifeCount = creativeResetsLifeCount;
        this.useRealtimeRegain = useRealtimeRegain;
    }
}
