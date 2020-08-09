package com.projectwild.game.pregame.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.shared.utils.Vector2;

public abstract class PGUIComponent {

    private int id;
    private PGUIParent parent;

    private Vector2 position, size;

    public PGUIComponent(Vector2 position, Vector2 size) {
        this.position = position;
        this.size = size;
    }

    public abstract void render(SpriteBatch sb, ShapeRenderer sr);

    public abstract void clicked(int x, int y);

    public abstract void typed(char character);

    protected void setId(int id) {
        this.id = id;
    }

    protected void setParent(PGUIParent parent) {
        this.parent = parent;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }

    public int getId() {
        return id;
    }

    public PGUIParent getParent() {
        return parent;
    }

}
