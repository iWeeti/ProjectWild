package com.projectwild.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.io.File;
import java.util.HashMap;

public class AssetManager {
    
    private HashMap<String, Texture> assets;
    private HashMap<String, TextureRegion[][]> tilesets;
    private HashMap<String, TextureRegion[][]> clothingAssets;
    private HashMap<String, TextureRegion[][]> itemIcons;
    private HashMap<String, BitmapFont> fonts;
    private HashMap<String, Sound> sounds;
    
    public AssetManager(){
        assets = loadAssets("data/assets");
        tilesets = loadTilesets("data/assets/tilesets", 32);
        clothingAssets = loadTilesets("data/assets/clothing",32);
        itemIcons = loadTilesets("data/assets/items", 20);
        fonts = loadFonts("data/assets/fonts");
        sounds = loadSounds("data/assets/sounds");
    }

    private void fixBleeding(TextureRegion region) {
        float fix = 0.01f;

        float x = region.getRegionX();
        float y = region.getRegionY();
        float width = region.getRegionWidth();
        float height = region.getRegionHeight();
        float invTexWidth = 1f / region.getTexture().getWidth();
        float invTexHeight = 1f / region.getTexture().getHeight();
        region.setRegion((x + fix) * invTexWidth, (y + fix) * invTexHeight, (x + width - fix) * invTexWidth, (y + height - fix) * invTexHeight); // Trims
    }
    
    private HashMap<String, TextureRegion[][]> loadTilesets(String path, int tileSize){
        HashMap<String, TextureRegion[][]> tilesets = new HashMap<>();
        File[] files = new File(path).listFiles();
        
        if(files == null)
            return tilesets;
        
        for(File f : files){
            Texture texture = new Texture(f.getPath());
            TextureRegion[][] ts = TextureRegion.split(texture, tileSize, tileSize);

            // Fix Texture Bleeding For Tiles
            for (TextureRegion[] array : ts) {
                for (TextureRegion t : array) {
                    fixBleeding(t);
                }
            }

            tilesets.put(f.getName().replace(".png", "").toLowerCase(), ts);
        }
        return tilesets;
    }
    
    private HashMap<String, Texture> loadAssets(String path){
        HashMap<String, Texture> assets = new HashMap<>();
        File[] files = new File(path).listFiles();

        if(files == null)
            return assets;
        
        for(File f : files){
            if(f.isDirectory()) {
                continue;
            }

            Texture texture = new Texture(f.getPath());
            assets.put(f.getName().replace(".png", "").toLowerCase(), texture);
        }
        return assets;
    }
    
    private HashMap<String, BitmapFont> loadFonts(String path){
        HashMap<String, BitmapFont> fonts = new HashMap<>();
        File[] files = new File(path).listFiles();
        
        if(files == null)
            return fonts;
        
        for(File f : files){
            if(f.isDirectory())
                continue;
            
            if(f.getName().contains(".fnt")){
                FileHandle fileHandle = new FileHandle(f);
                
                BitmapFont font = new BitmapFont(fileHandle);
                fonts.put(f.getName().replace(".fnt", "").toLowerCase(), font);
            }
        }
        return fonts;
    }

    private HashMap<String, Sound> loadSounds(String path) {
        HashMap<String, Sound> sounds = new HashMap<>();
        File[] files = new File(path).listFiles();

        if (files == null)
            return sounds;

        for (File f : files) {
            if (f.isDirectory())
                continue;

            if (f.getName().contains(".mp3") || f.getName().contains(".wav") || f.getName().contains(".ogg")) {
                FileHandle fileHandle = new FileHandle(f);
                Sound sound = Gdx.audio.newSound(fileHandle);
                sounds.put(f.getName().split("\\.")[0].toLowerCase(), sound);
            }
        }
        return sounds;
    }

    public TextureRegion getTile(String tileset, int x, int y){
        if(tilesets.containsKey(tileset.toLowerCase()))
            return tilesets.get(tileset.toLowerCase())[y][x];
        return null;
    }

    public TextureRegion[][] getClothingAsset(String asset){
        if(clothingAssets.containsKey(asset.toLowerCase()))
            return clothingAssets.get(asset.toLowerCase());
        return null;
    }

    public TextureRegion getItemIcon(String iconSet, int x, int y){
        if(itemIcons.containsKey(iconSet.toLowerCase()))
            return itemIcons.get(iconSet.toLowerCase())[y][x];
        return null;
    }

    public Texture getAsset(String asset){
        if(assets.containsKey(asset.toLowerCase()))
            return assets.get(asset.toLowerCase());
        return null;
    }
    
    public BitmapFont getFont(String font){
        if(fonts.containsKey(font.toLowerCase()))
            return fonts.get(font.toLowerCase());
        return null;
    }

    public Sound getSound(String sound){
        if (sounds.containsKey(sound.toLowerCase()))
            return sounds.get(sound.toLowerCase());
        return null;
    }

}
