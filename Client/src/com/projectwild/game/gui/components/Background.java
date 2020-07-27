package com.projectwild.game.gui.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;
import com.projectwild.game.gui.GUIComponent;
import com.projectwild.shared.utils.Vector2;

public class Background extends GUIComponent {

    private String asset;
    private Color bg;

    public Background(String asset) {
        super(new Vector2(0, 0), new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        this.asset = asset;
    }

    public Background(Color bg) {
        super(new Vector2(0, 0), new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        this.bg = bg;
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        if(asset != null) {
            Texture texture = WildGame.getAssetManager().getAsset(asset);
            sb.begin();
                sb.draw(texture, (int) getPosition().getX(), (int) getPosition().getY(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            sb.end();
        }

        if(bg != null) {
            sr.begin(ShapeRenderer.ShapeType.Filled);
                sr.setColor(bg);
                sr.box(0, 0, 1, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 1);
            sr.end();
        }
    }

    @Override
    public void clicked(int x, int y) {

    }

    @Override
    public void typed(char character) {

    }

}
