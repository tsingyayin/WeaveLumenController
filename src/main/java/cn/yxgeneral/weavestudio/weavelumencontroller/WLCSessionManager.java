package cn.yxgeneral.weavestudio.weavelumencontroller;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class WLCSessionManager {
    private static HashMap<UUID, WLCLumenBlocks> SessionMap = new HashMap<>();
    private static HashMap<UUID, Boolean> PlayerInSelectionMode = new HashMap<>();
    private static HashMap<UUID, Integer> Timeouts = new HashMap<>();
    private static ArrayList<WLCLumenBlocks> SessionList = new ArrayList<>();
    private static int Interval = 5;
    public WLCSessionManager(){
        new LCSTickLoop().runTaskTimer(WeaveLumenController.getInstance(), 0, 1);
    }
    public static boolean addSession(Player player){
        if (SessionMap.containsKey(player.getUniqueId())){
            WLCInteractExecutor.gWarning(
                    "Session already exists for player: " + player.getName() + ", Please remove it first."
            );
            return false;
        }
        WLCLumenBlocks data = new WLCLumenBlocks(player);
        SessionMap.put(player.getUniqueId(), data);
        SessionList.add(data);
        addPlayerSelectionModeData(player);
        WLCInteractExecutor.gInfo("Session added for player: " + player.getName());
        return true;
    }
    public static boolean hasSession(Player player){
        return SessionMap.containsKey(player.getUniqueId());
    }
    public static void refreshSession(Player player){
        WLCLumenBlocks data = SessionMap.get(player.getUniqueId());
        if (data == null){
            return;
        }
        data.setPlayer(player);
        removeTimeout(player);
    }
    public static void removeSession(Player player){
        if (getBlockCount(player) > 0){
            WLCInteractExecutor.gWarning("Please clear all blocks first, " + player.getName());
            return;
        }
        WLCLumenBlocks data = SessionMap.get(player.getUniqueId());
        SessionList.remove(data);
        SessionMap.remove(player.getUniqueId());
        removePlayerSelectionModeData(player);
        WLCInteractExecutor.gInfo("Session removed for player: " + player.getName());
    }
    public static void addPlayerSelectionModeData(Player player){
        PlayerInSelectionMode.put(player.getUniqueId(), true);
    }
    public static void removePlayerSelectionModeData(Player player){
        PlayerInSelectionMode.remove(player.getUniqueId());
    }
    public static boolean getPlayerInSelectionMode(Player player){
        if (!PlayerInSelectionMode.containsKey(player.getUniqueId())){
            return false;
        }
        return PlayerInSelectionMode.get(player.getUniqueId());
    }
    public static void togglePlayerInSelectionMode(Player player){
        if (PlayerInSelectionMode.get(player.getUniqueId())){
            PlayerInSelectionMode.put(player.getUniqueId(), false);
            WLCInteractExecutor.gInfo("Player " + player.getName() + " exited selection mode");
        }else{
            PlayerInSelectionMode.put(player.getUniqueId(), true);
            WLCInteractExecutor.gInfo("Player " + player.getName() + " entered selection mode");
        }
    }
    public static void addBlock(Player player, Block block){
        WLCLumenBlocks data = SessionMap.get(player.getUniqueId());
        if (data == null){
            return;
        }
        data.addBlock(block);
    }
    public static void removeBlock(Player player, Block block){
        WLCLumenBlocks data = SessionMap.get(player.getUniqueId());
        if (data == null){
            return;
        }
        data.removeBlock(block);
    }
    public static void clearBlocks(Player player){
        WLCLumenBlocks data = SessionMap.get(player.getUniqueId());
        if (data == null){
            return;
        }
        data.clearBlocks();
    }
    public static int getBlockCount(Player player){
        WLCLumenBlocks data = SessionMap.get(player.getUniqueId());
        if (data == null){
            return 0;
        }
        return data.getBlockCount();
    }
    public static void playerInteractBlock(Player player, Block block){
        WLCLumenBlocks data = SessionMap.get(player.getUniqueId());
        if (data == null){
            return;
        }
        data.playerInteractBlock(block);
    }
    public static @Nullable WLCLumenBlocks getSession(Player player){
        return SessionMap.get(player.getUniqueId());
    }
    public static void addTimeout(Player player){
        Timeouts.put(player.getUniqueId(), 0);
    }
    public static void removeTimeout(Player player){
        Timeouts.remove(player.getUniqueId());
    }
    public static class LCSTickLoop extends BukkitRunnable {
        @Override
        public void run(){
            if (SessionList.size() == 0){
                return;
            }
            for (UUID uuid : Timeouts.keySet()){
                int timeout = Timeouts.get(uuid);
                if (timeout >= WLCConfigManager.getSessionTimeout()){
                    Player player = WeaveLumenController.getInstance().getServer().getPlayer(uuid);
                    removeSession(player);
                    WLCInteractExecutor.gWarning("Session timeout for player: " + player.getName());
                }else{
                    Timeouts.put(uuid, timeout + 1);
                }
            }
            Interval--;
            if (Interval == 0){
                Interval = 5;
            }else{
                return;
            }
            for(WLCLumenBlocks data : SessionList){
                Player player = data.getPlayer();
                ArrayList<Block> blockList = data.getBlockList();
                for (Block block : blockList){
                    Location loc = block.getLocation();
                    Location playerLoc = player.getLocation();
                    if (loc.distance(playerLoc) > 16){
                        continue;
                    }
                    double x = loc.getX();
                    double y = loc.getY();
                    double z = loc.getZ();
                    while(x<=loc.getX()+1){
                        player.spawnParticle(Particle.COMPOSTER, x, y, z, 1);
                        player.spawnParticle(Particle.COMPOSTER, x, y+1, z, 1);
                        player.spawnParticle(Particle.COMPOSTER, x, y, z+1, 1);
                        player.spawnParticle(Particle.COMPOSTER, x, y+1, z+1, 1);
                        x += 0.2;
                    }
                    x = loc.getX();
                    while(y<=loc.getY()+1){
                        player.spawnParticle(Particle.COMPOSTER, x, y, z, 1);
                        player.spawnParticle(Particle.COMPOSTER, x+1, y, z, 1);
                        player.spawnParticle(Particle.COMPOSTER, x, y, z+1, 1);
                        player.spawnParticle(Particle.COMPOSTER, x+1, y, z+1, 1);
                        y += 0.2;
                    }
                    y = loc.getY();
                    while(z<=loc.getZ()+1){
                        player.spawnParticle(Particle.COMPOSTER, x, y, z, 1);
                        player.spawnParticle(Particle.COMPOSTER, x+1, y, z, 1);
                        player.spawnParticle(Particle.COMPOSTER, x, y+1, z, 1);
                        player.spawnParticle(Particle.COMPOSTER, x+1, y+1, z, 1);
                        z += 0.2;
                    }
                }
            }
        }
    }
}
