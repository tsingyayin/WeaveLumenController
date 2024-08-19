package cn.yxgeneral.weavestudio.weavelumencontroller;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WLCCommandHandler implements CommandExecutor, TabCompleter{
    private static Player executor = null;
    protected static void setExecutor(Player player){
        executor = player;
    }
    public static @Nullable Player getExecutor(){
        return executor;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (cmd.getName().equalsIgnoreCase(("weavelumencontroller"))) {
            if (!(sender instanceof Player)) {
                WLCInteractExecutor.gInfo("This plugin can only be operated by players.");
                return true;
            }
            try (WLCExecutorProtector executor = new WLCExecutorProtector(sender)) {
                if (args.length == 0) {
                    sendHelpMessage(sender);
                } else {
                    String arg1_1 = args[0];
                    switch (arg1_1){
                        case "create":
                            if (!WLCPermission.canUse((Player) sender)) {
                                WLCInteractExecutor.gInfo(WLCConfigManager.getTranslation("permission.noPermission"));
                                return true;
                            }
                            WLCSessionManager.addSession((Player) sender);
                            break;
                        case "toggle":
                            if (!WLCPermission.canUse((Player) sender)) {
                                WLCInteractExecutor.gInfo(WLCConfigManager.getTranslation("permission.noPermission"));
                                return true;
                            }
                            WLCSessionManager.togglePlayerInSelectionMode((Player) sender);
                            break;
                        case "auto":
                            if (!WLCPermission.canUse((Player) sender)) {
                                WLCInteractExecutor.gInfo(WLCConfigManager.getTranslation("permission.noPermission"));
                                return true;
                            }
                            int radius = 0;
                            if (args.length == 2) {
                                try {
                                    radius = Integer.parseInt(args[1]);
                                } catch (Exception e) {
                                    radius = 0;
                                }
                            }
                            WLCAutoSelector.doCheck(
                                    WLCSessionManager.getSession((Player) sender), (Player) sender, radius
                                );
                            break;
                        case "clear":
                            if (!WLCPermission.canUse((Player) sender)) {
                                WLCInteractExecutor.gInfo(WLCConfigManager.getTranslation("permission.noPermission"));
                                return true;
                            }
                            WLCSessionManager.clearBlocks((Player) sender);
                            break;
                        case "generate":
                            if (!WLCPermission.isAdministrator((Player) sender)) {
                                WLCInteractExecutor.gInfo(WLCConfigManager.getTranslation("permission.noPermission"));
                                return true;
                            }
                            WLCCommandBlockStructure.generateCommandBlockStructure(
                                    WLCSessionManager.getSession((Player) sender), (Player) sender
                            );
                            break;
                        case "reload":
                            if (!WLCPermission.isAdministrator((Player) sender)) {
                                WLCInteractExecutor.gInfo(WLCConfigManager.getTranslation("permission.noPermission"));
                                return true;
                            }
                            WLCConfigManager.reloadConfig();
                            break;
                        default:
                            sendHelpMessage(sender);
                            break;
                    }
                }
            }
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args){
        List<String> rtn = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase(("weavelumencontroller"))){
            switch(args.length){
                case 1:
                    rtn.add("help");
                    rtn.add("create");
                    rtn.add("toggle");
                    rtn.add("auto");
                    rtn.add("clear");
                    rtn.add("generate");
                    break;
                case 2:
                    if(args[0].equals("auto")){
                        rtn.add("<radius>");
                    }
                    break;
                default:
                    break;
            }
        }
        return rtn;
    }
    private void sendHelpMessage(CommandSender sender){
        for(String line : WLCConfigManager.getTranslationList("command.help")){
            WLCInteractExecutor.gInfo(line);
        }
    }

}
