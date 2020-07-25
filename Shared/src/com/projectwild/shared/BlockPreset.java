package com.projectwild.shared;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class BlockPreset {

    private int id;
    private String name;
    private int blockType;
    private int collisionType;
    private int renderType;
    
    private String tileset;
    private int tilesetX, tilesetY;
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getBlockType() {
        return blockType;
    }
    
    public int getCollisionType() {
        return collisionType;
    }
    
    public int getRenderType() {
        return renderType;
    }
    
    public String getTileset() {
        return tileset;
    }
    
    public int getTilesetX() {
        return tilesetX;
    }
    
    public int getTilesetY() {
        return tilesetY;
    }
    
    private static BlockPreset[] blockPresets;
    
    static {
        try {
            blockPresets = new Gson().fromJson(new FileReader("data/blocks.json"), BlockPreset[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static BlockPreset[] getBlockPresets() {
        return blockPresets;
    }
    
    public static BlockPreset getPreset(int id) {
        for(BlockPreset preset : blockPresets) {
            if(preset.getId() == id)
                return preset;
        }
        if(blockPresets.length > 0)
            return blockPresets[0];
        return null;
    }
    
}
