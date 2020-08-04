package com.projectwild.game.gui.ingame.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.game.gui.ingame.GuiComponent;
import com.projectwild.shared.utils.Vector2;

import java.util.concurrent.CopyOnWriteArrayList;

public class GuiBackground extends GuiComponent {

    private Color color;
    private Vector2 size;
    private int padding;

    public GuiBackground(Vector2 position, GuiComponent parent, Vector2 size, Color color) {
        super(position, parent);
        this.color = color;
        this.size = size;
        padding = 0;
    }
    public GuiBackground(Vector2 position, GuiComponent parent, Vector2 size, Color color, int padding) {
        super(position, parent);
        this.color = color;
        this.size = size;
        this.padding = padding;
    }

    @Override
    public void update() {
        updateComponents();
        setSize(getMaxSize(components));
    }

    private Vector2 getMaxSize(CopyOnWriteArrayList<GuiComponent> components) {
        double maxSizeX = size.getX();
        double height = size.getY();

        for (GuiComponent component : components) {
            maxSizeX = Math.max(maxSizeX, component.getSize().getX() + 2 * offset);
            height = component.getSize().getY() + padding;
            if (!component.getComponents().isEmpty()) {
                Vector2 m = getMaxSize(component.getComponents());
                maxSizeX = Math.max(m.getX(), maxSizeX);
            }
        }
        return new Vector2(maxSizeX, height);
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color);
        sr.rect((int) position.getX(), (int) position.getY(), (int) size.getX(), (int) size.getY());
        sr.end();
        renderComponents(sb, sr);
    }
}
