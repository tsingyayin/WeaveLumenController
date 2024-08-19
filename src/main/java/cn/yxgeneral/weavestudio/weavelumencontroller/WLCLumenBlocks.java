package cn.yxgeneral.weavestudio.weavelumencontroller;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Candle;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.entity.Player;

import java.util.regex.*;
import java.util.ArrayList;

public class WLCLumenBlocks {
    Player SetPlayer;
    ArrayList<Block> BlockList = new ArrayList<>();
    public WLCLumenBlocks(Player player){
        SetPlayer = player;
    }
    public void playerInteractBlock(Block block){
        if (BlockList.contains(block)){
            removeBlock(block);
        }else{
            addBlock(block);
        }
    }
    public void addBlock(Block block){
        if (BlockList.contains(block)){
            return;
        }
        WLCInteractExecutor.gInfo("Add block: " + block.getType().name() + " at " + block.getX() + ", " + block.getY() + ", " + block.getZ());
        WLCInteractExecutor.gInfo("BlockList size: " + BlockList.size());
        BlockList.add(block);
    }
    public void removeBlock(Block block){

        WLCInteractExecutor.gInfo("Remove block: " + block.getType().name() + " at " + block.getX() + ", " + block.getY() + ", " + block.getZ());
        WLCInteractExecutor.gInfo("BlockList size: " + BlockList.size());
        BlockList.remove(block);
    }
    public void clearBlocks(){
        WLCInteractExecutor.gInfo("Clear all blocks");
        BlockList.clear();
    }
    public int getBlockCount(){
        return BlockList.size();
    }
    public Player getPlayer(){
        return SetPlayer;
    }
    public void setPlayer(Player player){
        SetPlayer = player;
    }
    public ArrayList<Block> getBlockList(){
        return BlockList;
    }
    public ArrayList<String> getInitCommands(){
        ArrayList<String> commands = new ArrayList<>();
        for (Block block : BlockList){
            Material type = block.getType();
            String typename = type.name();
            if (Pattern.matches("^.*(_CANDLE)$", typename)){ // *_CANDLE, except for *_CANDLE_CAKE
                Candle candle = (Candle) block.getBlockData();
                boolean lit = candle.isLit();
                if (lit){
                    commands.add(getSetBlockCommand(block.getX(), block.getY(), block.getZ(), block.getType(),
                        "[candles=" + candle.getCandles() + ",lit=true]")
                    );
                }else{
                    commands.add(getSetBlockCommand(block.getX(), block.getY(), block.getZ(), block.getType(),
                        "[candles=" + candle.getCandles() + ",lit=false]")
                    );
                }
                continue;
            }
            switch (type) {
                case WALL_TORCH:
                case SOUL_WALL_TORCH:
                case REDSTONE_WALL_TORCH: // with facing
                case END_ROD: // with facing
                    String materialName = type.name().toLowerCase();
                    materialName = materialName.replace("WALL_", "");
                    Directional directional = (Directional) block.getBlockData();
                    commands.add(getSetBlockCommand(block.getX(), block.getY(), block.getZ(), materialName,
                        "[facing=" + directional.getFacing().toString().toLowerCase() + "]")
                    );
                    break;
                case LANTERN:
                case SOUL_LANTERN: // with hanging
                    Lantern lantern = (Lantern) block.getBlockData();
                    if (lantern.isHanging()){
                        commands.add(getSetBlockCommand(block.getX(), block.getY(), block.getZ(), block.getType(),
                            "[hanging=true]")
                        );
                    }else{
                        commands.add(getSetBlockCommand(block.getX(), block.getY(), block.getZ(), block.getType(),
                            "[hanging=false]")
                        );
                    }
                    break;

                default:
                    commands.add(getSetBlockCommand(block.getX(), block.getY(), block.getZ(), block.getType(),
                            "")
                    );
            }
        }
        return commands;
    }
    public ArrayList<String> getReplaceCommands(){
        ArrayList<String> commands = new ArrayList<>();
        for (Block block : BlockList){
            Material ReplacedMaterial = WLCConfigManager.getReplaceMaterial(block.getType());
            commands.add(getSetBlockCommand(block.getX(), block.getY(), block.getZ(), ReplacedMaterial, ""));
        }
        return commands;
    }
    public String getSetBlockCommand(int x, int y, int z, Material material, String data){
        return WLCConfigManager.getCommandTemplate().replace("%x%", String.valueOf(x))
                .replace("%y%", String.valueOf(y))
                .replace("%z%", String.valueOf(z))
                .replace("%name%", "minecraft:"+material.name().toLowerCase())
                .replace("%data%", data);
    }
    public String getSetBlockCommand(int x, int y, int z, String material, String data){
        return WLCConfigManager.getCommandTemplate().replace("%x%", String.valueOf(x))
                .replace("%y%", String.valueOf(y))
                .replace("%z%", String.valueOf(z))
                .replace("%name%", material)
                .replace("%data%", data);
    }
}
