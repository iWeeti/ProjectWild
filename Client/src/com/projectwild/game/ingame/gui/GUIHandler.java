package com.projectwild.game.ingame.gui;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.HashMap;

public class GUIHandler {

    private GUIWindow activeWindow;
    
    private GUIInputAdapter guiInputAdapter;
    private HashMap<String, GUIWindow> windows;
    private HashMap<String, PresetConstructor> presetWindows;
    
    public GUIHandler() {
        guiInputAdapter = new GUIInputAdapter();
        windows = new HashMap<>();
        presetWindows = new HashMap<>();
    }
    
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        for(GUIWindow window : windows.values())
            if(window != activeWindow)
                window.render(sb, sr);

        if(activeWindow != null)
            activeWindow.render(sb, sr);
    }

    public void registerPresetConstructor(PresetConstructor windowCallback) {
        registerPresetConstructor(windowCallback.constructorFunction(new Object[0]).getName().toLowerCase(), windowCallback);
    }
    
    public void registerPresetConstructor(String name, PresetConstructor windowCallback) {
        presetWindows.put(name, windowCallback);
    }

    public void createFromPreset(String preset, Object... args) {
        if(presetWindows.containsKey(preset.toLowerCase()))
            registerWindow(presetWindows.get(preset.toLowerCase()).constructorFunction(args));
    }

    public void registerWindow(GUIWindow window) {
        if(windows.containsKey(window.getName().toLowerCase()))
            destroyWindow(window.getName().toLowerCase());
        windows.put(window.getName().toLowerCase(), window);
        window.setParent(this);
        activeWindow = window;
    }

    public void destroyWindow(GUIWindow window) {
        destroyWindow(window.getName());
    }
    
    public void destroyWindow(String key) {
        if(windows.containsKey(key.toLowerCase())) {
            if(windows.get(key.toLowerCase()) == activeWindow)
                activeWindow = null;
            
            windows.get(key.toLowerCase()).dispose();
    
            windows.remove(key.toLowerCase());
    
            if(windows.values().size() > 0)
                activeWindow = (GUIWindow) windows.values().toArray()[0];
        }
    }
    
    public GUIWindow getWindow(String key) {
        if(windows.containsKey(key))
            return windows.get(key);
        return null;
    }
    
    public GUIInputAdapter getInputAdapter() {
        return guiInputAdapter;
    }

    public GUIWindow getActiveWindow() {
        return activeWindow;
    }

    public class GUIInputAdapter extends InputAdapter {
    
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            for(GUIWindow window : windows.values()) {
                if(screenX < window.getX() || screenX > window.getX() + window.getWidth())
                    continue;
                
                if(screenY < window.getY() || screenY > window.getY() + window.getHeight())
                    continue;

                activeWindow = window;

                window.clicked(false, screenX, screenY);
                return true;
            }
            
            return activeWindow != null;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            for(GUIWindow window : windows.values()) {
                if(screenX < window.getX() || screenX > window.getX() + window.getWidth())
                    continue;

                if(screenY < window.getY() || screenY > window.getY() + window.getHeight())
                    continue;

                window.clicked(true, screenX, screenY);
                return true;
            }

            return activeWindow != null;
        }

        @Override
        public boolean keyTyped(char character) {
            if(activeWindow != null) {
                activeWindow.keyTyped(character);
                return true;
            }
            return false;
        }

        @Override
        public boolean keyDown(int keycode) {
            if(activeWindow != null)
                return true;
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            if(activeWindow != null)
                return true;
            return false;
        }

    }

    public interface PresetConstructor {

        GUIWindow constructorFunction(Object[] args);

    }
    
}
