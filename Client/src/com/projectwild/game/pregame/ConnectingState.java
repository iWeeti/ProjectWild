package com.projectwild.game.pregame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.projectwild.game.GameState;
import com.projectwild.game.gui.GUIParent;
import com.projectwild.game.gui.components.Background;
import com.projectwild.game.gui.components.Image;
import com.projectwild.game.gui.components.Text;
import com.projectwild.shared.utils.Vector2;

import java.time.Clock;

public class ConnectingState implements GameState {

    private long lastConnected;
    GUIParent guiParent;
    Text text;

    @Override
    public void initialize() {
        lastConnected = Clock.systemUTC().millis();

        guiParent = new GUIParent();
        Gdx.input.setInputProcessor(guiParent.getInputAdapter());

        // layout
        guiParent.addComponent(new Background(Color.valueOf("2a2a4d")));
        guiParent.addComponent(new Image(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 275), new Vector2(200), "logo128"));
        text = getText();
        guiParent.addComponent(text);
    }

    @Override
    public void update() {
        guiParent.removeComponent(text);
        text = getText();
        guiParent.addComponent(text);
    }

    @Override
    public void render() {
        guiParent.render();
    }

    private Text getText() {
        return new Text(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 475), Color.valueOf("56569c"), String.format("Reconnecting in %s seconds.", (int) Math.floor((lastConnected - System.currentTimeMillis())/1000) +5));
    }

    @Override
    public void dispose() {

    }
}