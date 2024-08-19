package cn.yxgeneral.weavestudio.weavelumencontroller;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

public class WLCEventHandler implements Listener{
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        try(WLCExecutorProtector executor = new WLCExecutorProtector(player)) {
            if (!WLCSessionManager.getPlayerInSelectionMode(player)) {
                return;
            }
            Action action = event.getAction();
            if (!action.equals(Action.LEFT_CLICK_BLOCK)) {
                return;
            }
            Block block = event.getClickedBlock();
            if (block == null) {
                return;
            }
            WLCSessionManager.playerInteractBlock(player, block);
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        try(WLCExecutorProtector executor = new WLCExecutorProtector(player)) {
            WLCSessionManager.refreshSession(player);
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        try(WLCExecutorProtector executor = new WLCExecutorProtector(player)) {
            WLCSessionManager.removeSession(player);
        }
    }
}
