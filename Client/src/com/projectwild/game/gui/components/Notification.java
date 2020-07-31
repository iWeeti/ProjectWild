package com.projectwild.game.gui.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;
import com.projectwild.game.gui.GUIComponent;
import com.projectwild.shared.utils.Utils;
import com.projectwild.shared.utils.Vector2;

public class Notification extends GUIComponent {

    private long close;
    private BitmapFont font;
    private GlyphLayout text;

    private Color bgColor;

    private Callback callback;

    public Notification(int time, String text, Color bgColor) {
        super(new Vector2(0, 0), new Vector2());
        this.bgColor = bgColor;
        close = System.currentTimeMillis() + time * 1000;
        font = WildGame.getAssetManager().getFont("vcr_osd_32");
        this.text = new GlyphLayout(font, text);

        this.setSize(new Vector2(this.text.width + 20, this.text.height + 20));
        this.setPosition(new Vector2(Gdx.graphics.getWidth() - this.text.width - 40, Gdx.graphics.getHeight()));
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        if(System.currentTimeMillis() >= close) {
            setPosition(new Vector2(getPosition().getX(), Utils.lerp(getPosition().getY(), Gdx.graphics.getHeight(), 0.1)));
            if(System.currentTimeMillis() >= close + 1000) {
                getParent().removeComponent(this);
                if(callback != null)
                    callback.callback();
            }
        } else {
            setPosition(new Vector2(getPosition().getX(), Utils.lerp(getPosition().getY(), Gdx.graphics.getHeight() - this.getSize().getY() - 20, 0.1)));
        }

        sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(bgColor);
            sr.box((float) getPosition().getX(), (float) getPosition().getY(), 1, (float) getSize().getX(), (float) getSize().getY(), 1);
        sr.end();

        sb.begin();
            font.draw(sb, text, (int) getPosition().getX() + 10, (int) getPosition().getY() + 10 - 2 + text.height);
        sb.end();
    }

    @Override
    public void clicked(int x, int y) {

    }

    @Override
    public void typed(char character) {

    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void callback();

    }

}
