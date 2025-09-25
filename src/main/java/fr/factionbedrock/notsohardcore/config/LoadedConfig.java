package fr.factionbedrock.notsohardcore.config;

public class LoadedConfig
{
    //Local config. The parameters stored here are the one from the local config
    //(even if you are a client connected to a dedicated server)
    //client-side : the parameters here are the parameters in the client config file, they may differ from the parameters in ServerLoadedConfig.
    //server-side : the parameters here and in LocalLoadedConfig are the same
    public static class Local
    {
        public static NSHConfig CONFIG;
        public static int MAX_LIVES;
        public static int TIME_TO_REGAIN_LIFE;
        public static Boolean CREATIVE_RESETS_LIFE_COUNT;
        public static Boolean USE_REALTIME_REGAIN;
        public static Boolean ALWAYS_RENDER_HARDCORE_HEARTS;

        public static void initLocalLoadedConfig()
        {
            CONFIG = NSHConfigLoader.loadConfig();
            computeValuesFromLocalConfig();
        }

        public static void computeValuesFromLocalConfig()
        {
            MAX_LIVES = Math.max(Local.CONFIG.maxLives, 1);
            TIME_TO_REGAIN_LIFE = Local.CONFIG.timeToRegainLife >= 0 ? Local.CONFIG.timeToRegainLife : Integer.MAX_VALUE;
            CREATIVE_RESETS_LIFE_COUNT = Local.CONFIG.creativeResetsLifeCount;
            USE_REALTIME_REGAIN = Local.CONFIG.useRealtimeRegain;
            ALWAYS_RENDER_HARDCORE_HEARTS = Local.CONFIG.alwaysRenderHardcoreHearts;
        }
    }

    //Server config. Accessible client side too. The parameters stored here are the one from the server side
    //(even if player is connected to a dedicated server)
    //client-side : if you are in single player or in the main menu, the parameters here are the one in your local config.
    //              if you are on a dedicated server, the parameters here are overridden by the server config parameters, sent with a S2C packet (in NSHClientNetworking)
    //server-side : the parameters here and in LocalLoadedConfig are the same
    public static class Server
    {
        public static int MAX_LIVES;
        public static int TIME_TO_REGAIN_LIFE;
        public static Boolean CREATIVE_RESETS_LIFE_COUNT;
        public static Boolean USE_REALTIME_REGAIN;
        public static Boolean ALWAYS_RENDER_HARDCORE_HEARTS;

        protected static void storeParams(int maxLives, int timeToRegainLife, boolean creativeResetsLifeCount, boolean useRealtimeRegain, boolean alwaysRenderHardcoreHearts)
        {
            MAX_LIVES = maxLives;
            TIME_TO_REGAIN_LIFE = timeToRegainLife;
            CREATIVE_RESETS_LIFE_COUNT = creativeResetsLifeCount;
            USE_REALTIME_REGAIN = useRealtimeRegain;
            ALWAYS_RENDER_HARDCORE_HEARTS = alwaysRenderHardcoreHearts;
        }
    }

    //Never call this, send a NSHS2CSynchData packet instead (see NSHNetworking "sendS2CSync" methods)
    @Deprecated public static void overrideServerParams(int maxLives, int timeToRegainLife, boolean creativeResetsLifeCount, boolean useRealtimeRegain, boolean alwaysRenderHardcoreHearts)
    {
        Server.storeParams(maxLives, timeToRegainLife, creativeResetsLifeCount, useRealtimeRegain, alwaysRenderHardcoreHearts);
    }
}
