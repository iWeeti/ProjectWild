package com.projectwild.game.gui.ingame.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.projectwild.game.gui.ingame.GuiComponent;
import com.projectwild.shared.utils.Vector2;

import java.util.concurrent.CopyOnWriteArrayList;

public class GuiHolder extends GuiComponent {

    private InputAdapter inputAdapter;

    public GuiHolder(double x, double y) {
        super(new Vector2(x, y));
        inputAdapter = new InputAdapter(){

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                hoverComponents(components, screenX, screenY);
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                clickComponents(components, screenX, screenY);

                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                for(GuiComponent component : components) {
                    component.typed(character);
                }
                return false;
            }

        };
    }

    private void hoverComponents(CopyOnWriteArrayList<GuiComponent> components, int screenX, int screenY) {
        for(GuiComponent component : components) {
            if(screenX < component.getPosition().getX())
                continue;

            if(Gdx.graphics.getHeight() -screenY < component.getPosition().getY())
                continue;

            if(screenX > component.getPosition().getX() + component.getSize().getX())
                continue;

            if(Gdx.graphics.getHeight() -screenY > component.getPosition().getY() + component.getSize().getY())
                continue;

            component.setHovering(true);
        }
    }

    private boolean clickComponents(CopyOnWriteArrayList<GuiComponent> components, int screenX, int screenY) {
        for(GuiComponent component : components) {
            if (!component.getComponents().isEmpty())
                if (clickComponents(component.getComponents(), screenX, screenY)){
                    return true;
                }
            if(screenX < component.getPosition().getX())
                continue;

            if(Gdx.graphics.getHeight() -screenY < component.getPosition().getY())
                continue;

            if(screenX > component.getPosition().getX() + component.getSize().getX())
                continue;

            if(Gdx.graphics.getHeight() -screenY > component.getPosition().getY() + component.getSize().getY())
                continue;

            if (component.clicked(screenX - (int) component.getPosition().getX(), screenY - (int) component.getPosition().getY())) {
                System.out.println(component.getClass().getName());
                return true;
            }
        }

        return false;
    }

    public InputProcessor getInputAdapter() {
        return inputAdapter;
    }
}
