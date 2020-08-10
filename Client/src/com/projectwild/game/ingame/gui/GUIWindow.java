package com.projectwild.game.ingame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.concurrent.CopyOnWriteArrayList;

public class GUIWindow {
    
    private static final Color backgroundColor = new Color(71f / 255f, 71f / 255f, 71f / 255f, 1f);
    private static final int margin = 30;
    private static final int padding = 40;
    
    private CopyOnWriteArrayList<GUIComponent> components;
    private int x, y, width, height;
    
    private int counterY;
    
    public GUIWindow(int x, int y, int width, int height) {
        components = new CopyOnWriteArrayList<>();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        counterY = margin;
    }
    
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        GUIUtils.drawRect(sb, sr, backgroundColor, x, y, width, height);
        for(GUIComponent component : components)
            component.render(sb, sr);
    }
    
    public void clicked(int x, int y) {
        for(GUIComponent component : components) {
            if(x < component.getX() || x > component.getX() + component.getWidth())
                continue;
        
            if(y < component.getY() || y > component.getY() + component.getHeight())
                continue;
            
            component.clicked(x - component.getX(), y - component.getY());
        }
    }
    
    public void addComponent(GUIComponent component, GUIUtils.Align align) {
        int width = component.getWidth();
        int height = component.getHeight();
    
        switch(align) {
            case LEFT:
                component.setParent(this, x + margin, y + counterY);
                break;
            case CENTER:
                component.setParent(this, x + this.width / 2 - width / 2, y + counterY);
                break;
            case RIGHT:
                component.setParent(this, x + this.width - margin - width, y + counterY);
                break;
        }
        counterY += height + padding;
    
        components.add(0, component);
    }
    
    public void removeComponent(GUIComponent component) {
        components.remove(component);
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
}
