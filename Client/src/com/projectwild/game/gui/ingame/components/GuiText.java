package com.projectwild.game.gui.ingame.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.WildGame;
import com.projectwild.game.gui.ingame.GuiComponent;
import com.projectwild.shared.utils.Vector2;

public class GuiText extends GuiComponent {

    private GlyphLayout layout;
    private BitmapFont font;

    public GuiText(Vector2 position, GuiComponent parent, String text, String font) {
        super(position, parent);
        this.font = WildGame.getAssetManager().getFont(font);
        layout = new GlyphLayout(this.font, text);
        setSize(new Vector2(layout.width, layout.height));
    }

    public void updateText(String text) {
        layout = new GlyphLayout(this.font, text);
        setSize(new Vector2(layout.width, layout.height));
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sb.begin();
            font.draw(sb, layout, (float) position.getX() - (linearCentered ? layout.width / 2 : 0), Gdx.graphics.getHeight() - (float) position.getY() - (verticalCentered ? layout.height / 2 : 0));
        sb.end();

        renderComponents(sb, sr);
    }
}
