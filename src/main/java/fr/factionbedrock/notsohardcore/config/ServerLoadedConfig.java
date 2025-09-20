package fr.factionbedrock.notsohardcore.config;

//accessible client side too. The parameters stored here are the one from the server side (even if player is connected to a dedicated server)
public class ServerLoadedConfig
{
    public static int MAX_LIVES;
    public static int TIME_TO_REGAIN_LIFE;
    public static Boolean CREATIVE_RESETS_LIFE_COUNT;
    // Realtime regain configuration (server authoritative)
    public static Boolean USE_REALTIME_REGAIN;
    public static int TIME_TO_REGAIN_LIFE_SECONDS;

    public static void storeServerParams(int maxLives, int timeToRegainLife, boolean creativeResetsLifeCount)
    {
        MAX_LIVES = maxLives;
        TIME_TO_REGAIN_LIFE = timeToRegainLife;
        CREATIVE_RESETS_LIFE_COUNT = creativeResetsLifeCount;
    }

    public static void storeServerParams(int maxLives, int timeToRegainLife, boolean creativeResetsLifeCount,
                                         boolean useRealtimeRegain, int timeToRegainLifeSeconds)
    {
        MAX_LIVES = maxLives;
        TIME_TO_REGAIN_LIFE = timeToRegainLife;
        CREATIVE_RESETS_LIFE_COUNT = creativeResetsLifeCount;
        USE_REALTIME_REGAIN = useRealtimeRegain;
        TIME_TO_REGAIN_LIFE_SECONDS = timeToRegainLifeSeconds;
    }
}
