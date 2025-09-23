package fr.factionbedrock.notsohardcore.config;

public class NSHConfig
{
    public int maxLives = 3;
    public int timeToRegainLife = 72000;
    public boolean creativeResetsLifeCount = false;
    // Optional realtime-based regain (off by default). When enabled, lives
    // regain based on wall-clock seconds instead of in-game ticks.
    public boolean useRealtimeRegain = false;
    public int timeToRegainLifeSeconds = 3600; // 1 hour
}
