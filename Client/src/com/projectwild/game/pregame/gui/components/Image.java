package com.projectwild.game.pregame.gui.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;
import com.projectwild.game.pregame.gui.PGUIComponent;
import com.projectwild.shared.utils.Vector2;

public class Image extends PGUIComponent {

    private Texture texture;

    public Image(Vector2 position, Vector2 size, String texture) {
        super(position, size);
        this.texture = WildGame.getAssetManager().getAsset(texture);
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sb.begin();
            sb.draw(texture, (int) getPosition().getX() - (int) (getSize().getX() / 2), (int) getPosition().getY(), (int) getSize().getX(), (int) getSize().getY());
        sb.end();
    }

    @Override
    public void clicked(int x, int y) {

    }

    @Override
    public void typed(char character) {

    }

}
