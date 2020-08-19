package com.projectwild.game.ingame.gui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;
import com.projectwild.game.ingame.gui.GUIComponent;
import com.projectwild.game.ingame.gui.GUIDraw;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUIInputComponent extends GUIComponent {

    private String value;

    private String placeholder;
    private int maxSize;
    private boolean secret;

    private BitmapFont font;
    
    public GUIInputComponent(String placeholder, int maxSize) {
        this(placeholder, maxSize, false);
    }

    public GUIInputComponent(String placeholder, int maxSize, boolean secret) {
        this.placeholder = placeholder;
        this.maxSize = maxSize;
        this.secret = secret;
        font = WildGame.getAssetManager().getFont("vcr_osd_32_flipped");
        value = "";
        setWidth(maxSize * 19 + 40 + GUIDraw.getRectBorderWidth());
        setHeight(24 + 40 + GUIDraw.getRectBorderWidth());
    }

    @Override
    protected void render(SpriteBatch sb, ShapeRenderer sr) {
        GUIDraw.drawRect(sb, sr, new Color(71f / 255f, 71f / 255f, 71f / 255f, 1f), getX(), getY(), getWidth(), getHeight());

        if(value.equals("") && getParent().getActiveComponent() != this) {
            font.setColor(0.7f, 0.7f, 0.7f, 1);
            font.draw(sb, placeholder, getX() + 20 + GUIDraw.getRectBorderWidth() / 2f, getY() + 22 + GUIDraw.getRectBorderWidth() / 2f);
            font.setColor(1, 1, 1, 1);
        } else {
            if(secret) {
                StringBuilder builder = new StringBuilder();
                for(int i = 0; i < value.length(); i++)
                    builder.append("*");
                font.draw(sb, builder.toString(), getX() + 20 + GUIDraw.getRectBorderWidth() / 2f, getY() + 22 + GUIDraw.getRectBorderWidth() / 2f);
            } else {
                font.draw(sb, value, getX() + 20 + GUIDraw.getRectBorderWidth() / 2f, getY() + 22 + GUIDraw.getRectBorderWidth() / 2f);
            }
        }
    }

    @Override
    protected void mouseDown(int x, int y) {

    }

    @Override
    protected void mouseUp(int x, int y) {

    }

    @Override
    public void typed(char character) {
        WildGame.getAssetManager().getSound("key" + (int) Math.round(Math.random() * 3)).play();
        String input = Character.toString(character);
        String regex = "^[a-zA-Z0-9\b]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if(matcher.matches()) {
            if((int) character == 8) {
                if (!value.isEmpty())
                    value = value.replaceAll(".$", "");
            } else if((int) character != 13) {
                if(value.length() < maxSize)
                    value += input;
            }
        }
    }
    
    public String getValue() {
        return value;
    }
}
