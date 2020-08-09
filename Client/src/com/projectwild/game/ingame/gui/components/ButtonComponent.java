package com.projectwild.game.ingame.gui.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;
import com.projectwild.game.ingame.gui.GUIComponent;
import com.projectwild.game.ingame.gui.GUIUtils;

public class ButtonComponent extends GUIComponent {
    
    private Color backgroundColor;
    private Color clickedColor;
    
    private Callback callback;
    
    private BitmapFont font;
    private GlyphLayout text;
    
    public ButtonComponent(String text) {
        this(text, new Color(71f / 255f, 71f / 255f, 71f / 255f, 1f));
    }
    
    public ButtonComponent(String text, Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        clickedColor = new Color(Math.max(0, backgroundColor.r - 0.1f), Math.max(0, backgroundColor.g - 0.1f), Math.max(0, backgroundColor.b - 0.1f), 1f);
        font = WildGame.getAssetManager().getFont("vcr_osd_32_flipped");
        this.text = new GlyphLayout(font, text);
        setWidth((int) (this.text.width + 40));
        setHeight((int) (this.text.height + 40));
    }
    
    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        boolean hovering = true;
        
        if(Gdx.input.getX() < getX() || Gdx.input.getX() > getX() + getWidth())
            hovering = false;
    
        if(Gdx.input.getY() < getY() || Gdx.input.getY() > getY() + getHeight())
            hovering = false;
        
        if(hovering) {
            GUIUtils.drawRect(sb, sr, clickedColor, getX(), getY(), getWidth(), getHeight());
        } else {
            GUIUtils.drawRect(sb, sr, backgroundColor, getX(), getY(), getWidth(), getHeight());
        }
    
        font.draw(sb, text, getX() + 20, getY() + 22);
    }
    
    @Override
    public void clicked(int x, int y) {
        WildGame.getAssetManager().getSound("select").play();
        if(callback != null)
            callback.callback();
    }
    
    public void setCallback(Callback callback) {
        this.callback = callback;
    }
    
    public interface Callback {
        void callback();
    }
    
}
