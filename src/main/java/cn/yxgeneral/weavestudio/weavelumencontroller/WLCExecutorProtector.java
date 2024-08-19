package cn.yxgeneral.weavestudio.weavelumencontroller;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WLCExecutorProtector implements AutoCloseable {
    public WLCExecutorProtector(CommandSender player) {
        if (player instanceof Player) {
            WLCCommandHandler.setExecutor((Player) player);
        }
    }

    @Override
    public void close() {
        WLCCommandHandler.setExecutor(null);
    }
}
