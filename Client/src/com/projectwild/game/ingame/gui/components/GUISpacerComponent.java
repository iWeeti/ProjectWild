package com.projectwild.game.ingame.gui.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.ingame.gui.GUIComponent;

public class GUISpacerComponent extends GUIComponent {
    
    public GUISpacerComponent(int height, int width) {
        setHeight(height);
        setWidth(width);
    }
    
    @Override
    protected void render(SpriteBatch sb, ShapeRenderer sr) {
    
    }
    
    @Override
    protected void mouseDown(int x, int y) {
    
    }
    
    @Override
    protected void mouseUp(int x, int y) {
    
    }
    
    @Override
    protected void typed(char character) {
    
    }
    
}
