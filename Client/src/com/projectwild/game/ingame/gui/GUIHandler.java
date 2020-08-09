package com.projectwild.game.ingame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.HashMap;

public class GUIHandler {
    
    private GUIInputAdapter guiInputAdapter;
    private HashMap<String, GUIWindow> windows;
    
    public GUIHandler() {
        guiInputAdapter = new GUIInputAdapter();
        windows = new HashMap<>();
    }
    
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        for(GUIWindow window : windows.values())
            window.render(sb, sr);
    }
    
    public GUIWindow createWindow(String key, int width, int height) {
        return createWindow(key, Gdx.graphics.getWidth() / 2 - width / 2, Gdx.graphics.getHeight() / 2 - height / 2, width, height);
    }
    
    public GUIWindow createWindow(String key, int x, int y, int width, int height) {
        GUIWindow window = new GUIWindow(x, y, width, height);
        windows.put(key, window);
        return window;
    }
    
    public void destroyWindow(String key) {
        windows.remove(key);
    }
    
    public GUIWindow getWindow(String key) {
        if(windows.containsKey(key))
            return windows.get(key);
        return null;
    }
    
    public GUIInputAdapter getInputAdapter() {
        return guiInputAdapter;
    }
    
    public class GUIInputAdapter extends InputAdapter {
    
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            for(GUIWindow window : windows.values()) {
                if(screenX < window.getX() || screenX > window.getX() + window.getWidth())
                    continue;
                
                if(screenY < window.getY() || screenY > window.getY() + window.getHeight())
                    continue;
    
                window.clicked(screenX, screenY);
                return true;
            }
            
            return false;
        }
        
    }
    
}
