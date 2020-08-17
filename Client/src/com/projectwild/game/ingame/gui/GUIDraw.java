package com.projectwild.game.ingame.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;

public class GUIDraw {
    
    private final static int defaultBorderWidthMult = 4;

    private static TextureRegion rectBorder;
    private static TextureRegion rectCorner;

    static {
        rectBorder = new TextureRegion(WildGame.getAssetManager().getAsset("gui_border"));
        rectCorner = new TextureRegion(WildGame.getAssetManager().getAsset("gui_corner"));
    }

    public static int getRectBorderWidth() {
        return getRectBorderWidth(defaultBorderWidthMult);
    }

    public static int getRectBorderWidth(int borderWidthMult) {
        return rectBorder.getRegionWidth() * borderWidthMult;
    }

    public static void drawRect(SpriteBatch sb, ShapeRenderer sr, Color backgroundColor, int x, int y, int width, int height) {
        drawRect(sb, sr, backgroundColor, x, y, width, height, defaultBorderWidthMult);
    }

    public static void drawRect(SpriteBatch sb, ShapeRenderer sr, Color backgroundColor, int x, int y, int width, int height, int borderWidthMult) {
        int borderWidth = rectBorder.getRegionWidth() * borderWidthMult;
        int cornerSize = rectCorner.getRegionWidth() * borderWidthMult;
    
        // Draw Background
        sb.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(backgroundColor);
            sr.rect(x + borderWidth, y + borderWidth, width - borderWidth * 2, height - borderWidth * 2);
        sr.end();
        sb.begin();
        
        // Draw Corners
        sb.draw(rectCorner, x, y, cornerSize, cornerSize);
        sb.draw(rectCorner, x + width, y, 0, 0, cornerSize, cornerSize, 1, 1, 90);
        sb.draw(rectCorner, x, y + height , 0, 0, cornerSize, cornerSize, 1, 1, 270);
        sb.draw(rectCorner, x + width , y + height, 0, 0, cornerSize, cornerSize, 1, 1, 180);
        
        // Draw Borders
        sb.draw(rectBorder, x, y + cornerSize, borderWidth, height - cornerSize * 2);
        sb.draw(rectBorder, x + width - cornerSize, y, 0, 0, borderWidth, width - cornerSize * 2, 1, 1, 90);
        sb.draw(rectBorder, x + cornerSize, y + height, 0, 0, borderWidth, width - cornerSize * 2, 1, 1, 270);
        sb.draw(rectBorder, x + width, y + height - cornerSize, 0, 0, borderWidth, height - cornerSize * 2, 1, 1, 180);
    }

}