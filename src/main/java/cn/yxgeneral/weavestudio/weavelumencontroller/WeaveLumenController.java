package cn.yxgeneral.weavestudio.weavelumencontroller;

import org.bukkit.plugin.java.JavaPlugin;

public final class WeaveLumenController extends JavaPlugin {
    static WeaveLumenController Instance;
    static boolean PAPI = false;
    static WLCCommandHandler CHandler = null;
    static WLCEventHandler EHandler = null;
    static boolean JustStarted = true;
    static WLCSessionManager SManager = null;
    @Override
    public void onEnable() {
        // Plugin startup logic
        Instance = this;
        if (getServer().getPluginManager().getPlugin("PlaceHolderAPI")!=null){
            info("PlaceHolderAPI found, enabling PlaceHolderAPI support...");
            PAPI = true;
        }else{
            warning("PlaceHolderAPI not found, disabling PlaceHolderAPI support...");
            PAPI = false;
        }
        CHandler = new WLCCommandHandler();
        getCommand("weavelumencontroller").setExecutor(CHandler);
        getCommand("weavelumencontroller").setTabCompleter(CHandler);
        EHandler = new WLCEventHandler();
        getServer().getPluginManager().registerEvents(EHandler, this);
        WLCConfigManager.initConfig();
        SManager = new WLCSessionManager();
        JustStarted = false;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static boolean isJustStarted(){
        return JustStarted;
    }
    public static WeaveLumenController getInstance() {
        return Instance;
    }
    public static void info(String info){
        Instance.getLogger().info(info);
    }
    public static void warning(String warning){
        Instance.getLogger().warning(warning);
    }
}
