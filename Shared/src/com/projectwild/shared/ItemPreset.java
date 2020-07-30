package com.projectwild.shared;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ItemPreset {

    private int id;
    private String name;
    private int maxStack;
    private int itemType;

    private String itemSet;
    private int itemSetX;
    private int itemSetY;

    private int blockId;
    private int clothingId;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public String getItemSet() {
        return itemSet;
    }

    public int getItemSetX() {
        return itemSetX;
    }

    public int getItemSetY() {
        return itemSetY;
    }

    public int getItemType() {
        return itemType;
    }

    public int getBlockId() {
        return blockId;
    }

    public int getClothingId() {
        return clothingId;
    }

    private static ItemPreset[] itemPresets;

    static {
        try {
            itemPresets = new Gson().fromJson(new FileReader("data/items.json"), ItemPreset[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ItemPreset[] getItemPresets() {
        return itemPresets;
    }

    public static ItemPreset getPreset(int id) {
        for(ItemPreset preset : itemPresets) {
            if(preset.getId() == id)
                return preset;
        }
        if(itemPresets.length > 0)
            return itemPresets[0];
        return null;
    }

}
