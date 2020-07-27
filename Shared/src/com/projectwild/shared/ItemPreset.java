package com.projectwild.shared;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ItemPreset {

    private int id;
    private String name;
    private int itemType;
    private int blockId;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getItemType() {
        return itemType;
    }

    public int getBlockId() {
        return blockId;
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
