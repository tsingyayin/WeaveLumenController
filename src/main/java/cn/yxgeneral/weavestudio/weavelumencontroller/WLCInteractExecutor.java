package cn.yxgeneral.weavestudio.weavelumencontroller;

import org.bukkit.entity.Player;

public class WLCInteractExecutor {
    public static void consoleExecuteCommand(String cmd){
        //execute the command
        try {
            WeaveLumenController.getInstance().getServer().dispatchCommand(
                    WeaveLumenController.getInstance().getServer().getConsoleSender(), cmd
            );
        }catch (Exception e){
            WeaveLumenController.warning("Command execution with error: " + e.getMessage());
        }
    }
    public static void gInfo(String msg){
        WeaveLumenController.info(WLCUtils.applyConsoleColorCode(WLCUtils.applyPlaceHolder(msg, null)));
        Player player = WLCCommandHandler.getExecutor();
        if (player != null){
            sendPrefixMessage(player, msg);
        }
    }
    public static void gWarning(String msg){
        WeaveLumenController.warning(WLCUtils.applyConsoleColorCode(WLCUtils.applyPlaceHolder(msg, null)));
        Player player = WLCCommandHandler.getExecutor();
        if (player != null){
            sendPrefixMessage(player, msg);
        }
    }
    public static void playerExecuteCommand(Player player, String cmd){
        try {
            WeaveLumenController.getInstance().getServer().dispatchCommand(player, WLCUtils.applyAll(cmd, player));
        }catch (Exception e){
            WeaveLumenController.warning("Command execution with error: " + e.getMessage());
        }
    }
    public static void vanillaBroadcast(String msg){
        WeaveLumenController.getInstance().getServer().broadcastMessage(WLCUtils.applyAll(msg, null));
    }
    public static void displayTitle(Player player, String title, String subtitle, Integer fadeIn, Integer stay, Integer fadeOut){
        player.sendTitle(WLCUtils.applyAll(title, player), WLCUtils.applyAll(subtitle, player), fadeIn, stay, fadeOut);
    }

    public static void sendPrefixMessage(Player sender, String message){
        sender.sendMessage(WLCUtils.applyAll(WLCConfigManager.getPluginPrefix() + message, sender));
    }
    public static void sendNormalMessage(Player sender, String message){
        sender.sendMessage(WLCUtils.applyAll(message, sender));
    }
}
