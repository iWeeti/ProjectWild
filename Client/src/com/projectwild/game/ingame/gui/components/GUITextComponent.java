package com.projectwild.game.ingame.gui.components;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;
import com.projectwild.game.ingame.gui.GUIComponent;

public class GUITextComponent extends GUIComponent {
    
    private String text;
    private BitmapFont font;
    
    public GUITextComponent(String text) {
        this(text, Fonts.NORMAL);
    }
    
    public GUITextComponent(String text, Fonts font) {
        this.text = text;
        this.font = font.getFont();
        this.font.getData().markupEnabled = true;
        
        GlyphLayout glyphLayout = new GlyphLayout(this.font, text);
        setWidth((int) glyphLayout.width);
        setHeight((int) glyphLayout.height);
    }
    
    @Override
    protected void render(SpriteBatch sb, ShapeRenderer sr) {
        font.draw(sb, text, getX(), getY());
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
    
    public enum Fonts {
        SMALL("vcr_osd_16_flipped"),
        NORMAL("vcr_osd_32_flipped"),
        HEADER("vcr_osd_48_flipped");
        
        private BitmapFont font;
        
        Fonts(String font) {
            this.font = WildGame.getAssetManager().getFont(font);
        }
    
        public BitmapFont getFont() {
            return font;
        }
        
    }
    
}
