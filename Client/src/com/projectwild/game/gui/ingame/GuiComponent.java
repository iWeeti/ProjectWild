package com.projectwild.game.gui.ingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.projectwild.shared.utils.Vector2;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class GuiComponent {

    protected Vector2 position;
    protected Vector2 size;
    protected int offset;
    protected CopyOnWriteArrayList<GuiComponent> components;
    private GuiComponent parent;
    protected boolean linearCentered;
    protected boolean verticalCentered;
    protected boolean visible;
    protected boolean hovering;

    public GuiComponent(Vector2 position, GuiComponent parent) {
        this.position = position;
        this.size = new Vector2(0, 0);
        components = new CopyOnWriteArrayList<>();
        offset = 0;
        this.parent = parent;
        parent.addComponent(this);
        linearCentered = false;
        verticalCentered = false;
        visible = false;
        hovering = false;
    }

    public GuiComponent(Vector2 position) {
        this.position = position;
        this.size = new Vector2(0, 0);
        components = new CopyOnWriteArrayList<>();
        offset = 0;
        parent = null;
        linearCentered = false;
        verticalCentered = false;
        visible = false;
        hovering = false;
    }

    public void updateComponents() {
        for (GuiComponent component : components){
            component.update();
            component.setHovering(false);
        }
    }

    public void renderComponents(SpriteBatch sb, ShapeRenderer sr) {
        for (GuiComponent component : components)
            component.render(sb, sr);
    }

    public void update() {
        updateComponents();
    }

    public void render(SpriteBatch sb, ShapeRenderer sr) {
        if (!visible) return;
        renderComponents(sb, sr);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public GuiComponent getParent() {
        return parent;
    }

    public void addComponent(GuiComponent component) {
        components.add(component);
    }

    public void removeComponent(GuiComponent component) {
        components.remove(component);
    }

    public boolean isLinearCentered() {
        return linearCentered;
    }

    public void setLinearCentered(boolean linearCentered) {
        this.linearCentered = linearCentered;
    }

    public boolean isVerticalCentered() {
        return verticalCentered;
    }

    public void setVerticalCentered(boolean verticalCentered) {
        this.verticalCentered = verticalCentered;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public void toggleVisibility() {
        visible = !visible;
    }

    public boolean isVisible() {
        if (parent != null){
            return parent.isVisible();
        }
        return visible;
    }

    public boolean clicked(int x, int y) {
        return false;
    }

    public void typed(char character) {

    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isHovering() {
        return hovering;
    }

    public void setHovering(boolean hovering) {
        this.hovering = hovering;
    }

    public CopyOnWriteArrayList<GuiComponent> getComponents() {
        return components;
    }
}
