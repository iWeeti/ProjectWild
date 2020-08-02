package com.projectwild.game.gui.pregame.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;
import com.projectwild.game.gui.pregame.GUIComponent;
import com.projectwild.shared.utils.Vector2;

public class Text extends GUIComponent {

    private String text;
    private Color bgColor;

    private Vector2 originalPosition;

    private boolean secret;

    public Text(Vector2 position, Color bgColor, String text) {
        super(position, new Vector2());
        this.originalPosition = position.copy();
        this.text = text;
        this.bgColor = bgColor;
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        BitmapFont font = WildGame.getAssetManager().getFont("vcr_osd_32");
        GlyphLayout layout = new GlyphLayout(font, text);

        setSize(new Vector2(layout.width + 20, layout.height + 20));
        getPosition().setX(originalPosition.getX() - getSize().getX() / 2);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(new Color(Math.max(0, bgColor.r - 0.2f), Math.max(0, bgColor.g - 0.2f), Math.max(0, bgColor.b - 0.2f), 1));
        sr.box((float) getPosition().getX() + 5, (float) getPosition().getY() - 5, 1, (float) getSize().getX(), (float) getSize().getY(), 1);

        sr.setColor(bgColor);
        sr.box((float) getPosition().getX() + (isSelected() ? 3 : 0), (float) getPosition().getY() - (isSelected() ? 3 : 0), 1, (float) getSize().getX(), (float) getSize().getY(), 1);

        sr.setColor(new Color(Math.max(0, bgColor.r - 0.2f), Math.max(0, bgColor.g - 0.2f), Math.max(0, bgColor.b - 0.2f), 1));
        sr.box((float) getPosition().getX() + 3 + (isSelected() ? 3 : 0), (float) getPosition().getY() + 3 - (isSelected() ? 3 : 0), 1, (float) getSize().getX() - 6, (float) getSize().getY() - 6, 1);
        sr.end();

        sb.begin();
        font.draw(sb, layout, (int) getPosition().getX() + 10, (int) getPosition().getY() + 10 - 2 + layout.height);
        sb.end();
    }

    @Override
    public void clicked(int x, int y) {

    }

    @Override
    public void typed(char character) {

    }

    private boolean isSelected() {
        return getParent().getActiveComponent() == getId();
    }

    public String getText() {
        return text;
    }

}
