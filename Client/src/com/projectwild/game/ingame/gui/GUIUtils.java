package com.projectwild.game.ingame.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;

public class GUIUtils {
    
    private final static int borderWidthMult = 4;
    
    private static TextureRegion rectCorner;
    private static TextureRegion rectBorder;
    
    static {
        rectCorner = new TextureRegion(WildGame.getAssetManager().getAsset("gui_corner"));
        rectBorder = new TextureRegion(WildGame.getAssetManager().getAsset("gui_border"));
    }
    
    public static void drawRect(SpriteBatch sb, ShapeRenderer sr, Color backgroundColor, int x, int y, int width, int height) {
        int borderWidth = rectBorder.getRegionWidth() * borderWidthMult;
        int cornerSize = rectCorner.getRegionWidth() * borderWidthMult;
    
        // Draw Background
        sb.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(backgroundColor);
            sr.rect(x, y, width, height);
        sr.end();
        sb.begin();
        
        // Draw Corners
        sb.draw(rectCorner, x - borderWidth, y - borderWidth, cornerSize, cornerSize);
        sb.draw(rectCorner, x + width + borderWidth, y - borderWidth, 0, 0, cornerSize, cornerSize, 1, 1, 90);
        sb.draw(rectCorner, x - borderWidth, y + height + borderWidth, 0, 0, cornerSize, cornerSize, 1, 1, 270);
        sb.draw(rectCorner, x + width + borderWidth, y + height + borderWidth, 0, 0, cornerSize, cornerSize, 1, 1, 180);
        
        // Draw Borders
        sb.draw(rectBorder, x - borderWidth, y + cornerSize - borderWidth, borderWidth, height - cornerSize * 2 + borderWidth * 2);
        sb.draw(rectBorder, x + width - cornerSize + borderWidth, y - borderWidth, 0, 0, borderWidth, width - cornerSize * 2 + borderWidth * 2, 1, 1, 90);
        sb.draw(rectBorder, x + cornerSize - borderWidth, y + height + borderWidth, 0, 0, borderWidth, width - cornerSize * 2 + borderWidth * 2, 1, 1, 270);
        sb.draw(rectBorder, x + width + borderWidth, y + height - cornerSize + borderWidth, 0, 0, borderWidth, height - cornerSize * 2 + borderWidth * 2, 1, 1, 180);
    }
    
    public enum Align {
        LEFT,
        RIGHT,
        CENTER;
    }
    
}
