package com.projectwild.game.ingame.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class GUIComponent {
    
    private GUIWindow parent;
    private int x, y, width, height;
    
    protected void setParent(GUIWindow parent, int x, int y) {
        this.parent = parent;
        this.x = x;
        this.y = y;
    }
    
    protected abstract void render(SpriteBatch sb, ShapeRenderer sr);
    
    protected abstract void mouseDown(int x, int y);

    protected abstract void mouseUp(int x, int y);

    protected abstract void typed(char character);

    public void setWidth(int width) {
        this.width = width;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public GUIWindow getParent() {
        return parent;
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
