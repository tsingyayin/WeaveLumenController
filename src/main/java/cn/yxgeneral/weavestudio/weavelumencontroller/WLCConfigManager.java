package cn.yxgeneral.weavestudio.weavelumencontroller;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WLCConfigManager {
    private static String PluginPrefix = "";
    private static YamlConfiguration LanguageFile = null;
    private static String CommandTemplate = "";
    private static int SessionTimeout = 0; //in ticks, but config in seconds
    private static ConfigurationSection ReplaceMaterial = null;
    private static List<String> AutoModeWhiteList = null;
    public static void initConfig(){
        WeaveLumenController.getInstance().saveDefaultConfig();
        WeaveLumenController.getInstance().saveResource("lang/en_US.yml", true);
        WeaveLumenController.getInstance().saveResource("lang/zh_SC.yml", true);
        PluginPrefix = getConfig().getString("prefix");
        String langFileName = getConfig().getString("language");
        //check lang/langFileName.yml exists
        //if not, load lang/en_US.yml, else load lang/langFileName.yml
        File langFile = new File(WeaveLumenController.getInstance().getDataFolder(), "lang/"+langFileName+".yml");
        if(!langFile.exists()){
            LanguageFile = YamlConfiguration.loadConfiguration(
                    new File(WeaveLumenController.getInstance().getDataFolder(), "lang/en_US.yml")
            );
            WeaveLumenController.warning(getTranslation("plugin.noSuchLang").formatted(langFileName));
        }else{
            LanguageFile = YamlConfiguration.loadConfiguration(langFile);
        }
        SessionTimeout = getConfig().getInt("session.timeout");
        if (SessionTimeout < 0){
            SessionTimeout = 0;
        }
        if (SessionTimeout > 3600){
            SessionTimeout = 3600;
        }
        SessionTimeout = SessionTimeout * 20;
        CommandTemplate = getConfig().getString("command.template");
        ReplaceMaterial = getConfig().getConfigurationSection("replace");
        AutoModeWhiteList = getConfig().getStringList("whitelist");
    }
    public static FileConfiguration getConfig(){
        return WeaveLumenController.getInstance().getConfig();
    }
    public static String getTranslation(String key){
        String rtn = LanguageFile.getString(key);
        if (rtn != null){
            return LanguageFile.getString(key);
        }else{
            WeaveLumenController.warning("Localized key name '" + key + "' not detected");
            return "";
        }
    }
    public static List<String> getTranslationList(String key){
        List<String> rtn = LanguageFile.getStringList(key);
        if (!rtn.isEmpty()){
            return LanguageFile.getStringList(key);
        }else{
            WeaveLumenController.warning("Localized key name '" + key + "' not detected");
            return new ArrayList<>();
        }
    }
    public static String getPluginPrefix(){
        return PluginPrefix;
    }
    public static int getSessionTimeout(){
        return SessionTimeout;
    }
    public static String getCommandTemplate(){
        return CommandTemplate;
    }
    public static boolean isInAutoModeWhiteList(Material material){
        String materialName = material.name().toLowerCase();
        return AutoModeWhiteList.contains(materialName);
    }
    public static void reloadConfig(){
        WeaveLumenController.getInstance().reloadConfig();
        initConfig();
        WLCInteractExecutor.gInfo(WLCConfigManager.getTranslation("plugin.reloaded"));
    }
    public static int getCommandDefaultRadius(){
        return getConfig().getInt("command.default_radius");
    }
    public static Material getReplaceMaterial(Material material){
        String materialName = material.name();
        String replaceMaterialName = ReplaceMaterial.getString(materialName.toLowerCase());
        if (replaceMaterialName == null){
            return Material.AIR;
        }else{
            Material replaceMaterial = Material.getMaterial(replaceMaterialName.toUpperCase());
            if (replaceMaterial == null){
                return Material.AIR;
            }else{
                return replaceMaterial;
            }
        }
    }
}
