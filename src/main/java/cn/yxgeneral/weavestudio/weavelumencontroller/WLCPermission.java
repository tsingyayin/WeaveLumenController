package cn.yxgeneral.weavestudio.weavelumencontroller;

import org.bukkit.entity.Player;

public class WLCPermission {
    public static boolean isAdministrator(Player player){
        return player.hasPermission("weavelumencontroller.admin");
    }
    public static boolean canUse(Player player){
        return player.hasPermission("weavelumencontroller.use") || isAdministrator(player);
    }
}
