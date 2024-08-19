package cn.yxgeneral.weavestudio.weavelumencontroller;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class WLCCommandBlockStructure {
    public static boolean generateCommandBlockStructure(WLCLumenBlocks data, Player player){
        if (data == null){
            return false;
        }
        int height = data.getBlockCount();
        if (height == 0){
            return false;
        }
        if (height + 2 + player.getLocation().getY() > 320){
            return false;
        }
        Location loc = player.getLocation();
        Location initLoc = loc.clone().add(0, 1, 0);
        Block initBlock = initLoc.getBlock();
        initBlock.setType(Material.SMOOTH_STONE);
        Location redstoneTorchLoc = initLoc.clone().add(1, 0, 0);
        Block redstoneTorchBlock = redstoneTorchLoc.getBlock();
        redstoneTorchBlock.setType(Material.REDSTONE_WALL_TORCH);
        //torch facing east
        Directional directional = (Directional) redstoneTorchBlock.getBlockData();
        directional.setFacing(org.bukkit.block.BlockFace.EAST);
        redstoneTorchBlock.setBlockData(directional);
        Location initCommandBlockLoc = initLoc.clone().add(-1, 0, 0);
        Location replaceCommandBlockLoc = initLoc.clone().add(1, 1, 0);
        ArrayList<String> initCommands = data.getInitCommands();
        ArrayList<String> replaceCommands = data.getReplaceCommands();
        Block initCommandBlock = initCommandBlockLoc.getBlock();
        Block replaceCommandBlock = replaceCommandBlockLoc.getBlock();
        initCommandBlock.setType(Material.COMMAND_BLOCK);
        replaceCommandBlock.setType(Material.COMMAND_BLOCK);
        CommandBlock initCommandBlockState = (CommandBlock) initCommandBlock.getState();
        CommandBlock replaceCommandBlockState = (CommandBlock) replaceCommandBlock.getState();
        initCommandBlockState.setCommand(initCommands.get(0));
        initCommandBlockState.update();
        replaceCommandBlockState.setCommand(replaceCommands.get(0));
        replaceCommandBlockState.update();
        //command block facing top
        directional = (Directional) initCommandBlock.getBlockData();
        directional.setFacing(org.bukkit.block.BlockFace.UP);
        initCommandBlock.setBlockData(directional);
        //command block facing top
        directional = (Directional) replaceCommandBlock.getBlockData();
        directional.setFacing(org.bukkit.block.BlockFace.UP);
        replaceCommandBlock.setBlockData(directional);
        for(int i = 1; i < initCommands.size(); i++){
            Location initChainCommandBlockLoc = initCommandBlockLoc.clone().add(0, i, 0);
            Block initChainCommandBlock = initChainCommandBlockLoc.getBlock();
            initChainCommandBlock.setType(Material.CHAIN_COMMAND_BLOCK);
            CommandBlock initChainCommandBlockState = (CommandBlock) initChainCommandBlock.getState();
            initChainCommandBlockState.setCommand(initCommands.get(i));
            initChainCommandBlockState.update();
            //command block facing top
            directional = (Directional) initChainCommandBlock.getBlockData();
            directional.setFacing(org.bukkit.block.BlockFace.UP);
            initChainCommandBlock.setBlockData(directional);
            Location replaceChainCommandBlockLoc = replaceCommandBlockLoc.clone().add(0, i, 0);
            Block replaceChainCommandBlock = replaceChainCommandBlockLoc.getBlock();
            replaceChainCommandBlock.setType(Material.CHAIN_COMMAND_BLOCK);
            CommandBlock replaceChainCommandBlockState = (CommandBlock) replaceChainCommandBlock.getState();
            replaceChainCommandBlockState.setCommand(replaceCommands.get(i));
            replaceChainCommandBlockState.update();
            //command block facing top
            directional = (Directional) replaceChainCommandBlock.getBlockData();
            directional.setFacing(org.bukkit.block.BlockFace.UP);
            replaceChainCommandBlock.setBlockData(directional);
        }
        return true;
    }
}
