package com.projectwild.game.gui.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;
import com.projectwild.game.gui.GUIComponent;
import com.projectwild.shared.utils.Vector2;

public class Button extends GUIComponent {

    private Color bgColor;
    private GlyphLayout text;

    private Callback callback;

    public Button(Vector2 position, String text) {
        this(position, text, Color.BLUE);
    }

    public Button(Vector2 position, String text, Color bgColor) {
        super(position, new Vector2());
        this.bgColor = bgColor;

        setText(text);
        position.changeX(-(this.text.width+20)/2);
        position.changeY((this.text.height+20)/2);
        setSize(new Vector2(this.text.width + 20, this.text.height + 20));
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        BitmapFont font = WildGame.getAssetManager().getFont("vcr_osd_32");

        sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(new Color(Math.max(0, bgColor.r - 0.2f), Math.max(0, bgColor.g - 0.2f), Math.max(0, bgColor.b - 0.2f), 1));
            sr.box((float) getPosition().getX() + 5, (float) getPosition().getY() - 5, 1, (float) getSize().getX(), (float) getSize().getY(), 1);
            sr.setColor(bgColor);
            sr.box((float) getPosition().getX() + (isHovering() ? 3 : 0), (float) getPosition().getY() - (isHovering() ? 3 : 0), 1, (float) getSize().getX(), (float) getSize().getY(), 1);
        sr.end();

        sb.begin();
            font.draw(sb, text, (int) getPosition().getX() + 10 + (isHovering() ? 3 : 0), (int) getPosition().getY() + 10 - 2 - (isHovering() ? 3 : 0) + text.height);
        sb.end();
    }

    @Override
    public void clicked(int x, int y) {
        WildGame.getAssetManager().getSound("select").play();
        if(callback != null)
            callback.callback();
    }

    @Override
    public void typed(char character) {

    }

    private boolean isHovering() {
        int screenX = Gdx.input.getX();
        int screenY = Gdx.input.getY();

        if(screenX < getPosition().getX())
            return false;

        if(Gdx.graphics.getHeight() - screenY < getPosition().getY())
            return false;

        if(screenX > getPosition().getX() + getSize().getX())
            return false;

        if(Gdx.graphics.getHeight() - screenY > getPosition().getY() + getSize().getY())
            return false;
        return true;
    }

    public void setText(String text) {
        BitmapFont font = WildGame.getAssetManager().getFont("vcr_osd_32");
        this.text = new GlyphLayout(font, text);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setBGColor(Color color) {
        this.bgColor = color;
    }

    public Color getBGColor() {
        return bgColor;
    }

    public interface Callback {

        void callback();

    }

}
