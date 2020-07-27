package com.projectwild.game.gui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;
import com.projectwild.game.gui.GUIComponent;
import com.projectwild.shared.utils.Vector2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextField extends GUIComponent {

    private String placeholder;
    private String text;
    private Color bgColor;
    private int length;
    private int maxCharacters;

    private Vector2 originalPosition;

    private boolean secret;

    public TextField(Vector2 position, int length, String placeholder, Color bgColor, int maxCharacters) {
        super(position, new Vector2());
        this.originalPosition = position.copy();
        this.placeholder = placeholder;
        this.text = "";
        this.bgColor = bgColor;
        this.length = length;
        this.maxCharacters = maxCharacters;
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        BitmapFont font = WildGame.getAssetManager().getFont("vcr_osd_32");
        GlyphLayout layout = new GlyphLayout(font, text.equals("") && !isSelected() ? placeholder : (secret ? generateSecret() : text));

        if(layout.width > length) {
            setSize(new Vector2(layout.width + 20, layout.height + 20));
            getPosition().setX(originalPosition.getX() - getSize().getX() / 2);
        } else {
            setSize(new Vector2(length + 20, layout.height + 20));
            getPosition().setX(originalPosition.getX() - getSize().getX() / 2);
        }

        sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(new Color(Math.max(0, bgColor.r - 0.2f), Math.max(0, bgColor.g - 0.2f), Math.max(0, bgColor.b - 0.2f), 1));
            sr.box((float) getPosition().getX() + 5, (float) getPosition().getY() - 5, 1, (float) getSize().getX(), (float) getSize().getY(), 1);

            sr.setColor(bgColor);
            sr.box((float) getPosition().getX() + (isSelected() ? 3 : 0), (float) getPosition().getY() - (isSelected() ? 3 : 0), 1, (float) getSize().getX(), (float) getSize().getY(), 1);

            sr.setColor(new Color(Math.max(0, bgColor.r - 0.2f), Math.max(0, bgColor.g - 0.2f), Math.max(0, bgColor.b - 0.2f), 1));
            sr.box((float) getPosition().getX() + 3 + (isSelected() ? 3 : 0), (float) getPosition().getY() + 3 - (isSelected() ? 3 : 0), 1, (float) getSize().getX() - 6, (float) getSize().getY() - 6, 1);
        sr.end();

        sb.begin();
            font.draw(sb, layout, (int) getPosition().getX() + 10 + (isSelected() ? 3 : 0), (int) getPosition().getY() + 10 - 2 - (isSelected() ? 3 : 0) + layout.height);
        sb.end();
    }

    @Override
    public void clicked(int x, int y) {

    }

    @Override
    public void typed(char character) {
        if(!isSelected()) return;
        String input = Character.toString(character);
        String regex = "^[a-zA-Z0-9\b]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if(matcher.matches()) {
            if((int) character == 8) {
                if (!text.isEmpty())
                    text = text.replaceAll(".$", "");
            } else if((int) character != 13) {
                if(text.length() < maxCharacters)
                    text += input;
            }
        }
    }

    public void setSecret(boolean secret) {
        this.secret = secret;
    }

    private String generateSecret() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < text.length(); i++) {
            builder.append("*");
        }
        return builder.toString();
    }

    private boolean isSelected() {
        return getParent().getActiveComponent() == getId();
    }

    public String getText() {
        return text;
    }

}
