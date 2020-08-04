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

public abstract class GuiButton extends GuiComponent {

    private String text;
    private GlyphLayout layout;
    private BitmapFont font;
    private Color color;
    private int offset;

    public GuiButton(Vector2 position, GuiComponent parent, String text, String font, Color color, int offset) {
        super(position, parent);
        this.text = text;
        this.font = WildGame.getAssetManager().getFont(font);
        this.color = color;
        this.offset = offset;
        layout = new GlyphLayout(this.font, text);
    }

    @Override
    public void update() {
        super.update();
        if (hovering)
            System.out.println(hovering);
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        super.render(sb, sr);
        sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(color);
            sr.rect((int) position.getX() - (linearCentered ? layout.width / 2 : 0),  Gdx.graphics.getHeight() - (int) position.getY() - (verticalCentered ? layout.height / 2 : 0), offset * 2 + layout.width, offset * 2 + layout.height);
        sr.end();
        sb.begin();
            font.draw(sb, layout, (float) position.getX() + offset - (linearCentered ? layout.width / 2 : 0), Gdx.graphics.getHeight() - (float) position.getY() + offset  - (verticalCentered ? layout.height / 2 : 0));
        sb.end();
    }

    @Override
    public boolean clicked(int x, int y) {
        onClick();
        return true;
    }

    public abstract void onClick();

    public void updateText(String text){
        this.text = text;
        layout = new GlyphLayout(this.font, text);
    }

    public String getText() {
        return text;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
        layout = new GlyphLayout(this.font, text);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
