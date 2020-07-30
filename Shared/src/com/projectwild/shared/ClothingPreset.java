package com.projectwild.shared;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ClothingPreset {

    private int id;
    private String name;
    private String asset;
    private int slot;
    private int renderType;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAsset() {
        return asset;
    }

    public int getSlot() {
        return slot;
    }

    public int getRenderType() {
        return renderType;
    }

    private static ClothingPreset[] clothingPreset;

    static {
        try {
            clothingPreset = new Gson().fromJson(new FileReader("data/clothing.json"), ClothingPreset[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ClothingPreset[] getClothingPresets() {
        return clothingPreset;
    }

    public static ClothingPreset getPreset(int id) {
        for(ClothingPreset preset : clothingPreset) {
            if(preset.getId() == id)
                return preset;
        }
        if(clothingPreset.length > 0)
            return clothingPreset[0];
        return null;
    }

}
