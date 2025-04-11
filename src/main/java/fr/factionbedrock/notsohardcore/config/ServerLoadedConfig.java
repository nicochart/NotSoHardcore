package fr.factionbedrock.notsohardcore.config;

//accessible client side too. The parameters stored here are the one from the server side (even if player is connected to a dedicated server)
public class ServerLoadedConfig
{
    public static int MAX_LIVES;
    public static int TIME_TO_REGAIN_LIFE;
    public static Boolean CREATIVE_RESETS_LIFE_COUNT;

    public static void storeServerParams(int maxLives, int timeToRegainLife, boolean creativeResetsLifeCount)
    {
        MAX_LIVES = maxLives;
        TIME_TO_REGAIN_LIFE = timeToRegainLife;
        CREATIVE_RESETS_LIFE_COUNT = creativeResetsLifeCount;
    }
}
