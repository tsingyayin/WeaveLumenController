package cn.yxgeneral.weavestudio.weavelumencontroller;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WLCAutoSelector {
    public static void doCheck(WLCLumenBlocks set, Player player, int radius){
        if (set == null){
            WLCInteractExecutor.gWarning("No session found, please create one first.");
            return;
        }
        if (radius <= 0){
            radius = WLCConfigManager.getCommandDefaultRadius();
        }
        if (radius > 32768){
            radius = 32768;
            WLCInteractExecutor.gWarning("Radius is too large, set to 32");
        }
        int X = player.getLocation().getBlockX();
        int Y = player.getLocation().getBlockY();
        int Z = player.getLocation().getBlockZ();
        for (int x = X - radius; x <= X + radius; x++){
            for (int y = Y - radius; y <= Y + radius; y++){
                for (int z = Z - radius; z <= Z + radius; z++){
                    Block block = player.getWorld().getBlockAt(x, y, z);
                    if (WLCConfigManager.isInAutoModeWhiteList(block.getType())) {
                        set.addBlock(block);
                    }
                }
            }
        }
    }
}
