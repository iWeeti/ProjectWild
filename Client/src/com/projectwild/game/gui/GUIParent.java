package com.projectwild.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.concurrent.CopyOnWriteArrayList;

public class GUIParent {

    private int idCounter;
    private int activeComponent;

    private InputAdapter inputAdapter;
    private CopyOnWriteArrayList<GUIComponent> components;

    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;

    public GUIParent() {
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        components = new CopyOnWriteArrayList<>();
        inputAdapter = new InputAdapter(){

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {

                for(GUIComponent component : components) {
                    if(screenX < component.getPosition().getX())
                        continue;

                    if(Gdx.graphics.getHeight() -screenY < component.getPosition().getY())
                        continue;

                    if(screenX > component.getPosition().getX() + component.getSize().getX())
                        continue;

                    if(Gdx.graphics.getHeight() -screenY > component.getPosition().getY() + component.getSize().getY())
                        continue;

                    component.clicked(screenX - (int) component.getPosition().getX(), screenY - (int) component.getPosition().getY());
                    activeComponent = component.getId();
                }

                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                for(GUIComponent component : components) {
                    component.typed(character);
                }
                return false;
            }

        };
    }

    public void render() {
        spriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        for(GUIComponent component : components) {
            component.render(spriteBatch, shapeRenderer);
        }
    }

    public void addComponent(GUIComponent component) {
        component.setId(idCounter++);
        component.setParent(this);
        components.add(component);
    }

    public void removeComponent(GUIComponent component) {
        components.remove(component);
    }

    public InputAdapter getInputAdapter() {
        return inputAdapter;
    }

    public void destroy() {
        Gdx.input.setInputProcessor(null);
    }

    public int getActiveComponent() {
        return activeComponent;
    }

    public void setActiveComponent(int activeComponent) {
        this.activeComponent = activeComponent;
    }
}
