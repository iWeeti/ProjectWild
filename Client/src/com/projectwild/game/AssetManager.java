package com.projectwild.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.io.File;
import java.util.HashMap;

public class AssetManager {
    
    private HashMap<String, Texture> assets;
    private HashMap<String, TextureRegion[][]> tilesets;
    private HashMap<String, BitmapFont> fonts;
    
    public AssetManager(){
        assets = loadAssets("data/assets");
        tilesets = loadTilesets("data/assets/tilesets");
        fonts = loadFonts("data/assets/fonts");
    }
    
    private HashMap<String, TextureRegion[][]> loadTilesets(String path){
        HashMap<String, TextureRegion[][]> tilesets = new HashMap<>();
        File[] files = new File(path).listFiles();
        
        if(files == null)
            return tilesets;
        
        for(File f : files){
            Texture texture = new Texture(f.getPath());
            TextureRegion[][] ts = TextureRegion.split(texture, 32, 32);
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
    
    public TextureRegion getTile(String tileset, int x, int y){
        if(tilesets.containsKey(tileset.toLowerCase()))
            return tilesets.get(tileset.toLowerCase())[y][x];
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
    
}
